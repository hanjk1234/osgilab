/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.monitor.*;
import org.osgilab.bundles.monitoradmin.job.AbstractMonitoringJob;
import org.osgilab.bundles.monitoradmin.job.MonitoringJobVisitor;
import org.osgilab.bundles.monitoradmin.job.ScheduledMonitoringJob;
import org.osgilab.bundles.monitoradmin.job.SubscriptionMonitoringJob;
import org.osgilab.bundles.monitoradmin.util.StatusVariablePath;
import org.osgilab.bundles.monitoradmin.util.StatusVariablePathFilter;
import org.osgilab.bundles.monitoradmin.util.Utils;

import java.util.*;

/**
 * MonitorAdmin implementation
 *
 * @author dmytro.pishchukhin
 */
public class MonitorAdminImpl implements MonitorAdmin, MonitorListener, MonitoringJobVisitor {
    /**
     * Set of StatusVariable paths for which events are disabled
     */
    private Set<String> disabledPaths = new HashSet<String>();
    /**
     * List of run jobs
     */
    private final List<AbstractMonitoringJob> jobs = new ArrayList<AbstractMonitoringJob>();
    private OsgiVisitor osgiVisitor;
    private LogVisitor logVisitor;

    /**
     * Initialize MonitorAdmin implementation instance
     *
     * @param osgiVisitor osgi functionality visitor
     * @param logVisitor loggers visitor
     */
    public MonitorAdminImpl(OsgiVisitor osgiVisitor, LogVisitor logVisitor) {
        this.osgiVisitor = osgiVisitor;
        this.logVisitor = logVisitor;
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
     *
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
        logVisitor.debug("ENTRY: getStatusVariable: " + path, null);
        try {
            StatusVariablePath statusVariablePath = new StatusVariablePath(path);
            Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());

            return monitorable.getStatusVariable(statusVariablePath.getStatusVariableId());
        } finally {
            logVisitor.debug("EXIT: getStatusVariable: " + path, null);
        }
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
     *
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
        logVisitor.debug("ENTRY: getDescription: " + path, null);
        try {
            StatusVariablePath statusVariablePath = new StatusVariablePath(path);
            Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());

            return monitorable.getDescription(statusVariablePath.getStatusVariableId());
        } finally {
            logVisitor.debug("EXIT: getDescription: " + path, null);
        }
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
        logVisitor.debug("ENTRY: getMonitorableNames", null);
        try {
            // sorted set that contains Monitorable SERVICE_PIDs
            SortedSet<String> names = new TreeSet<String>();

            ServiceReference[] serviceReferences = osgiVisitor.findMonitorableReferences(null);

            if (serviceReferences != null) {
                for (ServiceReference serviceReference : serviceReferences) {
                    String pid = (String) serviceReference.getProperty(Constants.SERVICE_PID);
                    if (pid != null) {
                        names.add(pid);
                    }
                }
            }

            return names.toArray(new String[names.size()]);
        } finally {
            logVisitor.debug("EXIT: getMonitorableNames", null);
        }
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
     *
     * @throws java.lang.IllegalArgumentException
     *          if <code>monitorableId</code>
     *          is <code>null</code> or otherwise invalid, or points to a
     *          non-existing <code>Monitorable</code>
     */
    public StatusVariable[] getStatusVariables(String monitorableId)
            throws IllegalArgumentException {
        logVisitor.debug("ENTRY: getStatusVariables: " + monitorableId, null);
        try {
            Monitorable monitorable = findMonitorableById(monitorableId);

            String[] names = monitorable.getStatusVariableNames();
            StatusVariable[] variables = new StatusVariable[names.length];

            for (int i = 0; i < names.length; i++) {
                variables[i] = monitorable.getStatusVariable(names[i]);
            }

            return variables;
        } finally {
            logVisitor.debug("EXIT: getStatusVariables: " + monitorableId, null);
        }
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
     *
     * @throws java.lang.IllegalArgumentException
     *          if <code>monitorableId</code>
     *          is <code>null</code> or otherwise invalid, or points to a
     *          non-existing <code>Monitorable</code>
     */
    public String[] getStatusVariableNames(String monitorableId)
            throws IllegalArgumentException {
        logVisitor.debug("ENTRY: getStatusVariableNames: " + monitorableId, null);
        try {
            Monitorable monitorable = findMonitorableById(monitorableId);

            return monitorable.getStatusVariableNames();
        } finally {
            logVisitor.debug("EXIT: getStatusVariableNames: " + monitorableId, null);
        }
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
     *
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
        logVisitor.debug("ENTRY: resetStatusVariable: " + path, null);
        try {
            StatusVariablePath statusVariablePath = new StatusVariablePath(path);
            Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());

            return monitorable.resetStatusVariable(statusVariablePath.getStatusVariableId());
        } finally {
            logVisitor.debug("EXIT: resetStatusVariable: " + path, null);
        }
    }

    /**
     * Switches event sending on or off for the specified
     * <code>StatusVariable</code>s. When the <code>MonitorAdmin</code> is
     * notified about a <code>StatusVariable</code> being updated it sends an
     * event unless this feature is switched off. Note that events within a
     * monitoring job can not be switched off. The event sending state of the
     * <code>StatusVariables</code> must not be persistently stored. When a
     * <code>StatusVariable</code> is registered for the first time in a
     * framework session, its event sending state is set to ON by default.
     * <p/>
     * Usage of the "*" wildcard is allowed in the path argument of this method
     * as a convenience feature. The wildcard can be used in either or both path
     * fragments, but only at the end of the fragments.  The semantics of the
     * wildcard is that it stands for any matching <code>StatusVariable</code>
     * at the time of the method call, it does not affect the event sending
     * status of <code>StatusVariable</code>s which are not yet registered. As
     * an example, when the <code>switchEvents("MyMonitorable/*", false)</code>
     * method is executed, event sending from all <code>StatusVariables</code>
     * of the MyMonitorable service are switched off. However, if the
     * MyMonitorable service starts to publish a new <code>StatusVariable</code>
     * later, it's event sending status is on by default.
     *
     * @param path the identifier of the <code>StatusVariable</code>(s) in
     *             [Monitorable_id]/[StatusVariable_id] format, possibly with the
     *             "*" wildcard at the end of either path fragment
     * @param on   <code>false</code> if event sending should be switched off,
     *             <code>true</code> if it should be switched on for the given path
     * @throws java.lang.SecurityException if the caller does not hold
     *                                     <code>MonitorPermission</code> with the
     *                                     <code>switchevents</code> action or if there is any
     *                                     <code>StatusVariable</code> in the <code>path</code> field for
     *                                     which it is not allowed to switch event sending on or off as per
     *                                     the target field of the permission
     * @throws java.lang.IllegalArgumentException
     *                                     if <code>path</code> is
     *                                     <code>null</code> or otherwise invalid, or points to a
     *                                     non-existing <code>StatusVariable</code>
     */
    public void switchEvents(String path, boolean on)
            throws IllegalArgumentException, SecurityException {
        logVisitor.debug("ENTRY: switchEvents: " + path + ", " + on, null);
        try {
            StatusVariablePathFilter filter = new StatusVariablePathFilter(path);
            // check StatusVariable if filter does not contain wildcards
            if (!filter.isMonitorableWildcard()) {
                Monitorable monitorable = findMonitorableById(filter.getMonitorableId());
                if (!filter.isStatusVariableWildcard()) {
                    monitorable.getStatusVariable(filter.getStatusVariableId());
                }
            }

            if (on) {
                Iterator<String> iterator = disabledPaths.iterator();
                while (iterator.hasNext()) {
                    StatusVariablePath statusVariablePath = new StatusVariablePath(iterator.next());
                    if (filter.match(statusVariablePath.getMonitorableId(), statusVariablePath.getStatusVariableId())) {
                        iterator.remove();
                    }
                }
            } else {
                String[] monitorableNames = getMonitorableNames();
                for (String monitorableId : monitorableNames) {
                    String[] statusVariableNames = getStatusVariableNames(monitorableId);
                    for (String statusVariableId : statusVariableNames) {
                        if (filter.match(monitorableId, statusVariableId)) {
                            disabledPaths.add(monitorableId + '/' + statusVariableId);
                        }
                    }
                }
            }
        } finally {
            logVisitor.debug("EXIT: switchEvents: " + path + ", " + on, null);
        }
    }

    /**
     * Starts a time based <code>MonitoringJob</code> with the parameters
     * provided. Monitoring events will be sent according to the specified
     * schedule. All specified <code>StatusVariable</code>s must exist when the
     * job is started. The initiator string is used in the
     * <code>mon.listener.id</code> field of all events triggered by the job,
     * to allow filtering the events based on the initiator.
     * <p/>
     * The <code>schedule</code> parameter specifies the time in seconds
     * between two measurements, it must be greater than 0.  The first
     * measurement will be taken when the timer expires for the first time, not
     * when this method is called.
     * <p/>
     * The <code>count</code> parameter defines the number of measurements to be
     * taken, and must either be a positive integer, or 0 if the measurement is
     * to run until explicitly stopped.
     * <p/>
     * The entity which initiates a <code>MonitoringJob</code> needs to hold
     * <code>MonitorPermission</code> for all the specified target
     * <code>StatusVariable</code>s with the <code>startjob</code> action
     * present. If the permission's action string specifies a minimal sampling
     * interval then the <code>schedule</code> parameter should be at least as
     * great as the value in the action string.
     *
     * @param initiator       the identifier of the entity that initiated the job
     * @param statusVariables the list of <code>StatusVariable</code>s to be
     *                        monitored, with each <code>StatusVariable</code> name given in
     *                        [Monitorable_PID]/[StatusVariable_ID] format
     * @param schedule        the time in seconds between two measurements
     * @param count           the number of measurements to be taken, or 0 for the
     *                        measurement to run until explicitly stopped
     * @return the successfully started job object, cannot be <code>null</code>
     *
     * @throws java.lang.IllegalArgumentException
     *                                     if the list of
     *                                     <code>StatusVariable</code> names contains an invalid or
     *                                     non-existing <code>StatusVariable</code>; if
     *                                     <code>initiator</code> is <code>null</code> or empty; or if the
     *                                     <code>schedule</code> or <code>count</code> parameters are
     *                                     invalid
     * @throws java.lang.SecurityException if the caller does not hold
     *                                     <code>MonitorPermission</code> for all the specified
     *                                     <code>StatusVariable</code>s, with the <code>startjob</code>
     *                                     action present, or if the permission does not allow starting the
     *                                     job with the given frequency
     */
    public MonitoringJob startScheduledJob(String initiator, String[] statusVariables, int schedule, int count)
            throws IllegalArgumentException, SecurityException {
        logVisitor.debug("ENTRY: startScheduledJob: " + initiator, null);
        try {
            if (initiator == null) {
                throw new IllegalArgumentException("Initiator is null");
            }
            if (statusVariables == null) {
                throw new IllegalArgumentException("StatusVariables are null");
            }
            if (schedule <= 0) {
                throw new IllegalArgumentException("Schedule is invalid: " + count);
            }
            if (count < 0) {
                throw new IllegalArgumentException("Count is invalid: " + count);
            }
            for (String path : statusVariables) {
                StatusVariablePath statusVariablePath = new StatusVariablePath(path);
                Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());
                if (!new HashSet<String>(Arrays.asList(monitorable.getStatusVariableNames()))
                        .contains(statusVariablePath.getStatusVariableId())) {
                    throw new IllegalArgumentException("StatusVariable: " + path + " is non-existing");
                }
            }

            ScheduledMonitoringJob job = new ScheduledMonitoringJob(this, logVisitor, initiator,
                    statusVariables, schedule, count);
            synchronized (jobs) {
                jobs.add(job);
            }
            logVisitor.info("New Scheduled Job is started: " + initiator, null);
            return job;
        } finally {
            logVisitor.debug("EXIT: startScheduledJob: " + initiator, null);
        }
    }

    /**
     * Starts a change based <code>MonitoringJob</code> with the parameters
     * provided. Monitoring events will be sent when the
     * <code>StatusVariable</code>s of this job are updated. All specified
     * <code>StatusVariable</code>s must exist when the job is started, and
     * all must support update notifications. The initiator string is used in
     * the <code>mon.listener.id</code> field of all events triggered by the
     * job, to allow filtering the events based on the initiator.
     * <p/>
     * The <code>count</code> parameter specifies the number of changes that
     * must happen to a <code>StatusVariable</code> before a new notification is
     * sent, this must be a positive integer.
     * <p/>
     * The entity which initiates a <code>MonitoringJob</code> needs to hold
     * <code>MonitorPermission</code> for all the specified target
     * <code>StatusVariable</code>s with the <code>startjob</code> action
     * present.
     *
     * @param initiator       the identifier of the entity that initiated the job
     * @param statusVariables the list of <code>StatusVariable</code>s to be
     *                        monitored, with each <code>StatusVariable</code> name given in
     *                        [Monitorable_PID]/[StatusVariable_ID] format
     * @param count           the number of changes that must happen to a
     *                        <code>StatusVariable</code> before a new notification is sent
     * @return the successfully started job object, cannot be <code>null</code>
     *
     * @throws java.lang.IllegalArgumentException
     *                                     if the list of
     *                                     <code>StatusVariable</code> names contains an invalid or
     *                                     non-existing <code>StatusVariable</code>, or one that does not
     *                                     support notifications; if the <code>initiator</code> is
     *                                     <code>null</code> or empty; or if <code>count</code> is invalid
     * @throws java.lang.SecurityException if the caller does not hold
     *                                     <code>MonitorPermission</code> for all the specified
     *                                     <code>StatusVariable</code>s, with the <code>startjob</code>
     *                                     action present
     */
    public MonitoringJob startJob(String initiator, String[] statusVariables, int count)
            throws IllegalArgumentException, SecurityException {
        logVisitor.debug("ENTRY: startJob: " + initiator, null);
        try {
            if (initiator == null) {
                throw new IllegalArgumentException("Initiator is null");
            }
            if (statusVariables == null) {
                throw new IllegalArgumentException("StatusVariables are null");
            }
            if (count <= 0) {
                throw new IllegalArgumentException("Count is invalid: " + count);
            }
            for (String path : statusVariables) {
                StatusVariablePath statusVariablePath = new StatusVariablePath(path);
                Monitorable monitorable = findMonitorableById(statusVariablePath.getMonitorableId());
                if (!monitorable.notifiesOnChange(statusVariablePath.getStatusVariableId())) {
                    throw new IllegalArgumentException("StatusVariable: " + path + " does not support notifications");
                }
            }
            SubscriptionMonitoringJob job = new SubscriptionMonitoringJob(this, logVisitor, initiator,
                    statusVariables, count);
            synchronized (jobs) {
                jobs.add(job);
            }
            logVisitor.info("New Subscription Job is started: " + initiator, null);
            return job;
        } finally {
            logVisitor.debug("EXIT: startJob: " + initiator, null);
        }
    }

    /**
     * Returns the list of currently running <code>MonitoringJob</code>s.
     * Jobs are only visible to callers that have the necessary permissions: to
     * receive a Monitoring Job in the returned list, the caller must hold all
     * permissions required for starting the job.  This means that if the caller
     * does not have <code>MonitorPermission</code> with the proper
     * <code>startjob</code> action for all the Status Variables monitored by a
     * job, then that job will be silently omitted from the results.
     * <p/>
     * The returned array cannot be <code>null</code>, an empty array is
     * returned if there are no running jobs visible to the caller at the time
     * of the call.
     *
     * @return the list of running jobs visible to the caller
     */
    public MonitoringJob[] getRunningJobs() {
        logVisitor.debug("ENTRY: getRunningJobs", null);
        try {
            List<MonitoringJob> runningJobs = new ArrayList<MonitoringJob>();
            synchronized (jobs) {
                for (AbstractMonitoringJob job : jobs) {
                    if (job.isRunning()) {
                        runningJobs.add(job);
                    }
                }
            }
            return runningJobs.toArray(new MonitoringJob[runningJobs.size()]);
        } finally {
            logVisitor.debug("EXIT: getRunningJobs", null);
        }
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
        StatusVariablePath path = new StatusVariablePath(monitorableId, statusVariable.getID());
        if (isEventEnabled(path.getPath())) {
            fireEvent(monitorableId, statusVariable, null);
            logVisitor.info("Fire new SV update Event: " + path.getPath(), null);
        }
        // find jobs that handle this StatusVariable update event
        if (!jobs.isEmpty()) {
            synchronized (jobs) {
                for (AbstractMonitoringJob job : jobs) {
                    if (job.isHandleUpdateEvent(path.getPath())) {
                        job.handleUpdateEvent(monitorableId, statusVariable);
                    }
                }
            }
        }
    }

    /**
     * Get array with paths that are disabled for notificatios with switchEvents() method
     *
     * @return array with StatusVariable paths
     */
    public String[] getDisabledNotificationPaths() {
        return disabledPaths.toArray(new String[disabledPaths.size()]);
    }

    /**
     * Check if events are disabled for given path
     * @param path path to check
     * @return if events are disabled - <code>true</code>, otherwise - <code>false</code>
     */
    private boolean isEventEnabled(String path) {
        return !disabledPaths.contains(path);
    }

    /**
     * Fire StatusVariable update event
     *
     * @param monitorableId  monitorableId
     * @param statusVariable status variable
     * @param initiator      initiator. if <code>null</code> - is not added to event
     */
    public void fireEvent(String monitorableId, StatusVariable statusVariable, String initiator) {
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
        osgiVisitor.postEvent(event);
    }

    /**
     * Cancel Job and remove it from jobs list
     *
     * @param job job to cancel
     */
    public void cancelJob(AbstractMonitoringJob job) {
        synchronized (jobs) {
            jobs.remove(job);
            job.cancel();
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
     *
     * @throws IllegalArgumentException monitorableId is <code>null</code> or monitorableId points
     *                                  to non-existing service or monitorableId is invalid
     */
    private Monitorable findMonitorableById(String monitorableId) throws IllegalArgumentException {
        if (monitorableId == null) {
            throw new IllegalArgumentException("MonitorableId is null");
        }

        if (!Utils.validatePathId(monitorableId)) {
            throw new IllegalArgumentException("MonitorableId is invalid");
        }

        ServiceReference mostSuitableMonitorable = null;
        ServiceReference[] serviceReferences = osgiVisitor.findMonitorableReferences(monitorableId);

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
        return osgiVisitor.getService(mostSuitableMonitorable);
    }

    /**
     * Cancel all jobs externaly
     */
    void cancelJobs() {
        logVisitor.debug("ENTRY: cancelJobs", null);
        try {
            if (!jobs.isEmpty()) {
                synchronized (jobs) {
                    Iterator<AbstractMonitoringJob> iterator = jobs.iterator();
                    while (iterator.hasNext()) {
                        AbstractMonitoringJob job = iterator.next();
                        job.cancel();
                        iterator.remove();
                    }
                }
            }
        } finally {
            logVisitor.debug("ENTRY: cancelJobs", null);
        }
    }
}
