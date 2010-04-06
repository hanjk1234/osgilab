/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.log.LogService;
import org.osgi.service.monitor.*;
import org.osgi.util.tracker.ServiceTracker;

import java.util.*;

/**
 * MonitorAdmin implementation
 *
 * @author dmytro.pishchukhin
 */
public class MonitorAdminImpl implements MonitorAdmin, MonitorListener {
    private BundleContext bc;

    /**
     * Set of StatusVariable paths for which events are disabled
     */
    private Set<String> disabledPaths = new HashSet<String>();
    private ServiceTracker eventAdminTracker;
    private ServiceTracker logServiceTracker;

    public MonitorAdminImpl(BundleContext bc) {
        this.bc = bc;
    }

    public void init() {
        logServiceTracker = new ServiceTracker(bc, LogService.class.getName(), null);
        logServiceTracker.open();

        eventAdminTracker = new ServiceTracker(bc, EventAdmin.class.getName(), null);
        eventAdminTracker.open();
    }

    public void uninit() {
        // todo: stop running jobs
        eventAdminTracker.close();
        eventAdminTracker = null;

        logServiceTracker.close();
        logServiceTracker = null;
    }

    /**
     * Returns a <code>StatusVariable</code> addressed by its full path.
     * The entity which queries a <code>StatusVariable</code> needs to hold
     * <code>MonitorPermission</code> for the given target with the
     * <code>read</code> action present.
     *
     * @param path the full path of the <code>StatusVariable</code> in
     *             [Monitorable_ID]/[StatusVariable_ID] format
     * @return the <code>StatusVariable</code> object
     * @throws java.lang.IllegalArgumentException
     *                                     if <code>path</code> is
     *                                     <code>null</code> or otherwise invalid, or points to a
     *                                     non-existing <code>StatusVariable</code>
     * @throws java.lang.SecurityException if the caller does not hold a
     *                                     <code>MonitorPermission</code> for the
     *                                     <code>StatusVariable</code> specified by <code>path</code>
     *                                     with the <code>read</code> action present
     */
    public StatusVariable getStatusVariable(String path)
            throws IllegalArgumentException, SecurityException {
        StatusVariablePath statusVariablePath = new StatusVariablePath(path);
        Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());

        return monitorable.getStatusVariable(statusVariablePath.getStatusVariableId());
    }

    /**
     * Returns a human readable description of the given
     * <code>StatusVariable</code>. The <code>null</code> value may be returned
     * if there is no description for the given <code>StatusVariable</code>.
     * <p/>
     * The entity that queries a <code>StatusVariable</code> needs to hold
     * <code>MonitorPermission</code> for the given target with the
     * <code>read</code> action present.
     *
     * @param path the full path of the <code>StatusVariable</code> in
     *             [Monitorable_ID]/[StatusVariable_ID] format
     * @return the human readable description of this
     *         <code>StatusVariable</code> or <code>null</code> if it is not
     *         set
     * @throws java.lang.IllegalArgumentException
     *                                     if <code>path</code> is
     *                                     <code>null</code> or otherwise invalid, or points to a
     *                                     non-existing <code>StatusVariable</code>
     * @throws java.lang.SecurityException if the caller does not hold a
     *                                     <code>MonitorPermission</code> for the
     *                                     <code>StatusVariable</code> specified by <code>path</code>
     *                                     with the <code>read</code> action present
     */
    public String getDescription(String path)
            throws IllegalArgumentException, SecurityException {
        StatusVariablePath statusVariablePath = new StatusVariablePath(path);
        Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());

        return monitorable.getDescription(statusVariablePath.getStatusVariableId());
    }

    /**
     * Returns the names of the <code>Monitorable</code> services that are
     * currently registered. The <code>Monitorable</code> instances are not
     * accessible through the <code>MonitorAdmin</code>, so that requests to
     * individual status variables can be filtered with respect to the
     * publishing rights of the <code>Monitorable</code> and the reading
     * rights of the caller.
     * <p/>
     * The returned array contains the names in alphabetical order. It cannot be
     * <code>null</code>, an empty array is returned if no
     * <code>Monitorable</code> services are registered.
     *
     * @return the array of <code>Monitorable</code> names
     */
    public String[] getMonitorableNames() {
        // sorted set that contains Monitorable SERVICE_PIDs
        SortedSet<String> names = new TreeSet<String>();

        ServiceReference[] serviceReferences = null;
        try {
            serviceReferences = bc.getServiceReferences(Monitorable.class.getName(), null);
        } catch (InvalidSyntaxException e) {
            // illegal state as filter is null
        }

        if (serviceReferences != null) {
            for (ServiceReference serviceReference : serviceReferences) {
                String pid = (String) serviceReference.getProperty(Constants.SERVICE_PID);
                if (pid != null) {
                    names.add(pid);
                }
            }
        }

        return names.toArray(new String[names.size()]);
    }

    /**
     * Returns the <code>StatusVariable</code> objects published by a
     * <code>Monitorable</code> instance. The <code>StatusVariables</code>
     * will hold the values taken at the time of this method call. Only those
     * status variables are returned where the following two conditions are met:
     * <ul>
     * <li>the specified <code>Monitorable</code> holds a
     * <code>MonitorPermission</code> for the status variable with the
     * <code>publish</code> action present
     * <li>the caller holds a <code>MonitorPermission</code> for the status
     * variable with the <code>read</code> action present
     * </ul>
     * All other status variables are silently ignored, they are omitted from
     * the result.
     * <p/>
     * The elements in the returned array are in no particular order. The return
     * value cannot be <code>null</code>, an empty array is returned if no
     * (authorized and readable) Status Variables are provided by the given
     * <code>Monitorable</code>.
     *
     * @param monitorableId the identifier of a <code>Monitorable</code>
     *                      instance
     * @return a list of <code>StatusVariable</code> objects published
     *         by the specified <code>Monitorable</code>
     * @throws java.lang.IllegalArgumentException
     *          if <code>monitorableId</code>
     *          is <code>null</code> or otherwise invalid, or points to a
     *          non-existing <code>Monitorable</code>
     */
    public StatusVariable[] getStatusVariables(String monitorableId)
            throws IllegalArgumentException {
        Monitorable monitorable = findMonitorableById(monitorableId);

        String[] names = monitorable.getStatusVariableNames();
        StatusVariable[] variables = new StatusVariable[names.length];

        for (int i = 0; i < names.length; i++) {
            variables[i] = monitorable.getStatusVariable(names[i]);
        }

        return variables;
    }

    /**
     * Returns the list of <code>StatusVariable</code> names published by a
     * <code>Monitorable</code> instance. Only those status variables are
     * listed where the following two conditions are met:
     * <ul>
     * <li>the specified <code>Monitorable</code> holds a
     * <code>MonitorPermission</code> for the status variable with the
     * <code>publish</code> action present
     * <li>the caller holds a <code>MonitorPermission</code> for
     * the status variable with the <code>read</code> action present
     * </ul>
     * All other status variables are silently ignored, their names are omitted
     * from the list.
     * <p/>
     * The returned array does not contain duplicates, and the elements are in
     * alphabetical order. It cannot be <code>null</code>, an empty array is
     * returned if no (authorized and readable) Status Variables are provided
     * by the given <code>Monitorable</code>.
     *
     * @param monitorableId the identifier of a <code>Monitorable</code>
     *                      instance
     * @return a list of <code>StatusVariable</code> objects names
     *         published by the specified <code>Monitorable</code>
     * @throws java.lang.IllegalArgumentException
     *          if <code>monitorableId</code>
     *          is <code>null</code> or otherwise invalid, or points to a
     *          non-existing <code>Monitorable</code>
     */
    public String[] getStatusVariableNames(String monitorableId)
            throws IllegalArgumentException {
        Monitorable monitorable = findMonitorableById(monitorableId);

        return monitorable.getStatusVariableNames();
    }

    /**
     * Issues a request to reset a given <code>StatusVariable</code>.
     * Depending on the semantics of the <code>StatusVariable</code> this call
     * may or may not succeed: it makes sense to reset a counter to its starting
     * value, but e.g. a <code>StatusVariable</code> of type String might not
     * have a meaningful default value. Note that for numeric
     * <code>StatusVariable</code>s the starting value may not necessarily be
     * 0. Resetting a <code>StatusVariable</code> triggers a monitor event if
     * the <code>StatusVariable</code> supports update notifications.
     * <p/>
     * The entity that wants to reset the <code>StatusVariable</code> needs to
     * hold <code>MonitorPermission</code> with the <code>reset</code>
     * action present. The target field of the permission must match the
     * <code>StatusVariable</code> name to be reset.
     *
     * @param path the identifier of the <code>StatusVariable</code> in
     *             [Monitorable_id]/[StatusVariable_id] format
     * @return <code>true</code> if the <code>Monitorable</code> could
     *         successfully reset the given <code>StatusVariable</code>,
     *         <code>false</code> otherwise
     * @throws java.lang.IllegalArgumentException
     *                                     if <code>path</code> is
     *                                     <code>null</code> or otherwise invalid, or points to a
     *                                     non-existing <code>StatusVariable</code>
     * @throws java.lang.SecurityException if the caller does not hold
     *                                     <code>MonitorPermission</code> with the <code>reset</code>
     *                                     action or if the specified <code>StatusVariable</code> is not
     *                                     allowed to be reset as per the target field of the permission
     */
    public boolean resetStatusVariable(String path)
            throws IllegalArgumentException, SecurityException {
        StatusVariablePath statusVariablePath = new StatusVariablePath(path);
        Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());

        return monitorable.resetStatusVariable(statusVariablePath.getStatusVariableId());
    }

    public void switchEvents(String path, boolean on)
            throws IllegalArgumentException, SecurityException {
        // todo
        throw new UnsupportedOperationException("Method is not implemented");
    }

    public MonitoringJob startScheduledJob(String initiator,
                                           String[] statusVariables, int schedule, int count)
            throws IllegalArgumentException, SecurityException {
        // todo
        throw new UnsupportedOperationException("Method is not implemented");
    }

    public MonitoringJob startJob(String initiator, String[] statusVariables, int count)
            throws IllegalArgumentException, SecurityException {
        // todo
        throw new UnsupportedOperationException("Method is not implemented");
    }

    public MonitoringJob[] getRunningJobs() {
        // todo
        throw new UnsupportedOperationException("Method is not implemented");
    }

    /**
     * Callback for notification of a <code>StatusVariable</code> change.
     *
     * @param monitorableId  the identifier of the <code>Monitorable</code>
     *                       instance reporting the change
     * @param statusVariable the <code>StatusVariable</code> that has changed
     * @throws java.lang.IllegalArgumentException
     *          if the specified monitorable
     *          ID is invalid (<code>null</code>, empty, or contains illegal
     *          characters) or points to a non-existing <code>Monitorable</code>,
     *          or if <code>statusVariable</code> is <code>null</code>
     */
    public void updated(String monitorableId, StatusVariable statusVariable) throws IllegalArgumentException {
        // validate monitorableId
        findMonitorableById(monitorableId);
        if (statusVariable == null) {
            throw new IllegalArgumentException("StatusVariable is null");
        }
        if (isEventEnabled(monitorableId + '/' + statusVariable.getID())) {
            fireEvent(monitorableId, statusVariable, null);
        }
        // todo: fire event for jobs
    }

    private boolean isEventEnabled(String path) {
        return !disabledPaths.contains(path);
    }

    private void fireEvent(String monitorableId, StatusVariable statusVariable, String initiator) {
        Dictionary<String, String> eventProperties = new Hashtable<String, String>();
        eventProperties.put(ConstantsMonitorAdmin.MON_MONITORABLE_PID, monitorableId);
        eventProperties.put(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME, statusVariable.getID());

        String value = null;
        switch (statusVariable.getType()) {
            case StatusVariable.TYPE_BOOLEAN:
                value = Boolean.toString(statusVariable.getBoolean());
                break;
            case StatusVariable.TYPE_FLOAT:
                value = Float.toString(statusVariable.getFloat());
                break;
            case StatusVariable.TYPE_INTEGER:
                value = Integer.toString(statusVariable.getInteger());
                break;
            case StatusVariable.TYPE_STRING:
                value = statusVariable.getString();
                break;
        }
        eventProperties.put(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE, value);
        if (initiator != null) {
            eventProperties.put(ConstantsMonitorAdmin.MON_LISTENER_ID, initiator);
        }

        Event event = new Event(ConstantsMonitorAdmin.TOPIC, eventProperties);
        EventAdmin eventAdmin = (EventAdmin) eventAdminTracker.getService();
        if (eventAdmin != null) {
            eventAdmin.postEvent(event);
        }
    }

    /**
     * Find Monitorable service by monitorable Id. Returns Monitorable service or
     * throws exception.
     * If multiple services exist for the same monitorableId,
     * the service with the highest ranking (as specified in its Constants.SERVICE_RANKING property)
     * is returned.
     * If there is a tie in ranking, the service with the lowest service ID (as specified
     * in its Constants.SERVICE_ID property); that is, the service that was regis-
     * tered first is returned.
     *
     * @param monitorableId id that is userd to filter services
     * @return Monitorable service with specified monitorableId.
     * @throws IllegalArgumentException monitorableId is <code>null</code> or monitorableId points
     *                                  to non-existing service or monitorableId is invalid
     */
    private Monitorable findMonitorableById(String monitorableId) throws IllegalArgumentException {
        if (monitorableId == null) {
            throw new IllegalArgumentException("MonitorableId is null");
        }

        if (!Utils.validateId(monitorableId)) {
            throw new IllegalArgumentException("MonitorableId is invalid");
        }

        ServiceReference mostSuitableMonitorable = null;
        ServiceReference[] serviceReferences = null;

        try {
            serviceReferences = bc.getServiceReferences(Monitorable.class.getName(), Utils.createServicePidFilter(monitorableId));
        } catch (InvalidSyntaxException e) {
            // illegal state as filter should be ok (id is valid)
        }

        if (serviceReferences != null) {
            for (ServiceReference serviceReference : serviceReferences) {
                if (mostSuitableMonitorable == null ||
                        mostSuitableMonitorable.compareTo(serviceReference) < 0) {
                    mostSuitableMonitorable = serviceReference;
                }
            }
        }
        if (mostSuitableMonitorable == null) {
            throw new IllegalArgumentException("Monitorable ID: " + monitorableId + " points to non-existing service");
        }
        return (Monitorable) bc.getService(mostSuitableMonitorable);
    }
}
