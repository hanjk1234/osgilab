/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.framework.*;
import org.osgi.jmx.JmxConstants;
import org.osgi.jmx.framework.ServiceStateMBean;
import org.osgilab.bundles.jmx.OsgiVisitor;
import org.osgilab.bundles.jmx.Utils;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ServiceStateMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class ServiceState extends AbstractMBean implements ServiceStateMBean, NotificationBroadcaster, ServiceListener {
    private NotificationBroadcasterSupport nbs;
    private MBeanNotificationInfo[] notificationInfos;
    private int sequenceNumber = 0;

    public ServiceState(OsgiVisitor visitor) throws NotCompliantMBeanException {
        super(ServiceStateMBean.class, visitor);
        nbs = new NotificationBroadcasterSupport();
    }

    public String[] getObjectClass(long id) throws IOException {
        ServiceReference serviceReference = visitor.getServiceReferenceById(id);
        if (serviceReference == null) {
            throw new IllegalArgumentException("Wrong Service ID: " + id);
        }
        return (String[]) serviceReference.getProperty(Constants.OBJECTCLASS);
    }

    public long getBundleIdentifier(long id) throws IOException {
        ServiceReference serviceReference = visitor.getServiceReferenceById(id);
        if (serviceReference == null) {
            throw new IllegalArgumentException("Wrong Service ID: " + id);
        }
        return serviceReference.getBundle().getBundleId();
    }

    public TabularData getProperties(long id) throws IOException {
        ServiceReference serviceReference = visitor.getServiceReferenceById(id);
        if (serviceReference == null) {
            throw new IllegalArgumentException("Wrong Service ID: " + id);
        }
        TabularDataSupport dataSupport = new TabularDataSupport(JmxConstants.PROPERTIES_TYPE);
        try {
            String[] keys = serviceReference.getPropertyKeys();
            for (String key : keys) {
                Object value = serviceReference.getProperty(key);
                Map<String,Object> values = new HashMap<String, Object>();
                values.put(JmxConstants.KEY, key);
                values.put(JmxConstants.TYPE, Utils.getValueType(value));
                values.put(JmxConstants.VALUE, Utils.serializeToString(value));
                dataSupport.put(new CompositeDataSupport(JmxConstants.PROPERTY_TYPE, values));
            }
        } catch (OpenDataException e) {
            e.printStackTrace(); 
            // todo: handle somehow
        }
        return dataSupport;
    }

    public TabularData listServices() throws IOException {
        TabularDataSupport dataSupport = null;
        try {
            ServiceReference[] serviceReferences = visitor.getAllServiceReferences();
            dataSupport = new TabularDataSupport(SERVICES_TYPE);
            if (serviceReferences != null) {
                for (ServiceReference serviceReference : serviceReferences) {
                    Map<String,Object> values = new HashMap<String, Object>();
                    values.put(BUNDLE_IDENTIFIER, serviceReference.getBundle().getBundleId());
                    values.put(IDENTIFIER, serviceReference.getProperty(Constants.SERVICE_ID));
                    values.put(OBJECT_CLASS, serviceReference.getProperty(Constants.OBJECTCLASS));
                    values.put(USING_BUNDLES, Utils.toLongArray(Utils.getIds(serviceReference.getUsingBundles())));
                    dataSupport.put(new CompositeDataSupport(SERVICE_TYPE, values));
                }
            }
        } catch (OpenDataException e) {
            e.printStackTrace();
            // todo: handle somehow
        }
        return dataSupport;
    }

    public long[] getUsingBundles(long id) throws IOException {
        ServiceReference serviceReference = visitor.getServiceReferenceById(id);
        if (serviceReference == null) {
            throw new IllegalArgumentException("Wrong Service ID: " + id);
        }
        return Utils.getIds(serviceReference.getUsingBundles());
    }

    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        nbs.addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        nbs.removeNotificationListener(listener);
    }

    public synchronized MBeanNotificationInfo[] getNotificationInfo() {
        if (notificationInfos == null) {
            notificationInfos = new MBeanNotificationInfo[]{
                    new MBeanNotificationInfo(new String[]{ServiceStateMBean.EVENT},
                            Notification.class.getName(), ServiceStateMBean.EVENT)
            };
        }
        return notificationInfos;
    }

    public void serviceChanged(ServiceEvent event) {
        Notification notification = new Notification(ServiceStateMBean.EVENT, this, ++sequenceNumber,
                System.currentTimeMillis());

        try {
            ServiceReference serviceReference = event.getServiceReference();
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(IDENTIFIER, serviceReference.getProperty(Constants.SERVICE_ID));
            values.put(OBJECT_CLASS, serviceReference.getProperty(Constants.OBJECTCLASS));
            values.put(BUNDLE_LOCATION, serviceReference.getBundle().getLocation());
            values.put(BUNDLE_SYMBOLIC_NAME, serviceReference.getBundle().getSymbolicName());
            values.put(EVENT, event.getType());
            notification.setUserData(new CompositeDataSupport(ServiceStateMBean.SERVICE_EVENT_TYPE, values));

            nbs.sendNotification(notification);
        } catch (OpenDataException e) {
            // todo: handle somehow
        }
    }
}
