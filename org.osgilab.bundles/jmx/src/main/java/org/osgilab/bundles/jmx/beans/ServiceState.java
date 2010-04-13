/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.jmx.framework.ServiceStateMBean;
import org.osgilab.bundles.jmx.OsgiVisitor;

import javax.management.*;
import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * ServiceStateMBean Implementation
 * 
 * @author dmytro.pishchukhin
 */
public class ServiceState extends AbstractMBean implements ServiceStateMBean, NotificationBroadcaster {
    private NotificationBroadcasterSupport nbs;
    private MBeanNotificationInfo[] notificationInfos;

    public ServiceState(OsgiVisitor visitor) throws NotCompliantMBeanException {
        super(ServiceStateMBean.class, visitor);
        nbs = new NotificationBroadcasterSupport();
    }

    public String[] getObjectClass(long l) throws IOException {
        return new String[0];  // todo
    }

    public long getBundleIdentifier(long l) throws IOException {
        return 0;  // todo
    }

    public TabularData getProperties(long l) throws IOException {
        return null;  // todo
    }

    public TabularData listServices() throws IOException {
        return null;  // todo
    }

    public long[] getUsingBundles(long l) throws IOException {
        return new long[0];  // todo
    }

    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        nbs.addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        nbs.removeNotificationListener(listener);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        synchronized (this) {
            if (notificationInfos == null) {
                notificationInfos = new MBeanNotificationInfo[]{
                        new MBeanNotificationInfo(new String[]{ServiceStateMBean.EVENT},
                                Notification.class.getName(), ServiceStateMBean.EVENT)
                };
            }
            return notificationInfos;
        }
    }
}
