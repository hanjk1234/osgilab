/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.jmx.framework.BundleStateMBean;
import org.osgilab.bundles.jmx.OsgiVisitor;

import javax.management.*;
import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * BundleStateMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class BundleState extends AbstractMBean implements BundleStateMBean, NotificationBroadcaster {
    private NotificationBroadcasterSupport nbs;
    private MBeanNotificationInfo[] notificationInfos;

    public BundleState(OsgiVisitor visitor) throws NotCompliantMBeanException {
        super(BundleStateMBean.class, visitor);
        nbs = new NotificationBroadcasterSupport();
    }

    public long[] getRequiredBundles(long l) throws IOException {
        return new long[0];  // todo
    }

    public TabularData listBundles() throws IOException {
        return null;  // todo
    }

    public String[] getExportedPackages(long l) throws IOException {
        return new String[0];  // todo
    }

    public long[] getFragments(long l) throws IOException {
        return new long[0];  // todo
    }

    public TabularData getHeaders(long l) throws IOException {
        return null;  // todo
    }

    public long[] getHosts(long l) throws IOException {
        return new long[0];  // todo
    }

    public String[] getImportedPackages(long l) throws IOException {
        return new String[0];  // todo
    }

    public long getLastModified(long l) throws IOException {
        return 0;  // todo
    }

    public long[] getRegisteredServices(long l) throws IOException {
        return new long[0];  // todo
    }

    public long[] getRequiringBundles(long l) throws IOException {
        return new long[0];  // todo
    }

    public long[] getServicesInUse(long l) throws IOException {
        return new long[0];  // todo
    }

    public int getStartLevel(long l) throws IOException {
        return 0;  // todo
    }

    public String getState(long l) throws IOException {
        return null;  // todo
    }

    public String getSymbolicName(long l) throws IOException {
        return null;  // todo
    }

    public boolean isPersistentlyStarted(long l) throws IOException {
        return false;  // todo
    }

    public boolean isFragment(long l) throws IOException {
        return false;  // todo
    }

    public boolean isRemovalPending(long l) throws IOException {
        return false;  // todo
    }

    public boolean isRequired(long l) throws IOException {
        return false;  // todo
    }

    public String getLocation(long l) throws IOException {
        return null;  // todo
    }

    public String getVersion(long l) throws IOException {
        return null;  // todo
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
                        new MBeanNotificationInfo(new String[]{BundleStateMBean.EVENT},
                                Notification.class.getName(), BundleStateMBean.EVENT)
                };
            }
            return notificationInfos;
        }
    }
}
