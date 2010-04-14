/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.framework.*;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.osgilab.bundles.jmx.OsgiVisitor;
import org.osgilab.bundles.jmx.Utils;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * BundleStateMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class BundleState extends AbstractMBean implements BundleStateMBean, NotificationBroadcaster, BundleListener {
    private NotificationBroadcasterSupport nbs;
    private MBeanNotificationInfo[] notificationInfos;

    private int sequenceNumber = 0;

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

    public long[] getFragments(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        return Utils.getIds(packageAdmin.getFragments(bundle));
    }

    public TabularData getHeaders(long l) throws IOException {
        return null;  // todo
    }

    public long[] getHosts(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        return Utils.getIds(packageAdmin.getHosts(bundle));
    }

    public String[] getImportedPackages(long l) throws IOException {
        return new String[0];  // todo
    }

    public long getLastModified(long id) throws IOException {
        Bundle bundle = visitor.getBundle(id);
        if (bundle == null) {
            throw new IllegalArgumentException("Wrong Bundle ID: " + id);
        }
        return bundle.getLastModified();
    }

    public long[] getRegisteredServices(long id) throws IOException {
        Bundle bundle = visitor.getBundle(id);
        if (bundle == null) {
            throw new IllegalArgumentException("Wrong Bundle ID: " + id);
        }
        return Utils.getIds(bundle.getRegisteredServices());
    }

    public long[] getRequiringBundles(long l) throws IOException {
        return new long[0];  // todo
    }

    public long[] getServicesInUse(long id) throws IOException {
        Bundle bundle = visitor.getBundle(id);
        if (bundle == null) {
            throw new IllegalArgumentException("Wrong Bundle ID: " + id);
        }
        return Utils.getIds(bundle.getServicesInUse());
    }

    public int getStartLevel(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        StartLevel startLevel = visitor.getStartLevel();
        if (startLevel == null) {
            throw new IOException("StartLevel is not available");
        }
        return startLevel.getBundleStartLevel(bundle);
    }

    public String getState(long l) throws IOException {
        return null;  // todo
    }

    public String getSymbolicName(long id) throws IOException {
        Bundle bundle = visitor.getBundle(id);
        if (bundle == null) {
            throw new IllegalArgumentException("Wrong Bundle ID: " + id);
        }
        return bundle.getSymbolicName();
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

    public String getLocation(long id) throws IOException {
        Bundle bundle = visitor.getBundle(id);
        if (bundle == null) {
            throw new IllegalArgumentException("Wrong Bundle ID: " + id);
        }
        return bundle.getLocation();
    }

    public String getVersion(long id) throws IOException {
        Bundle bundle = visitor.getBundle(id);
        if (bundle == null) {
            throw new IllegalArgumentException("Wrong Bundle ID: " + id);
        }
        return bundle.getVersion().toString();
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
                    new MBeanNotificationInfo(new String[]{BundleStateMBean.EVENT},
                            Notification.class.getName(), BundleStateMBean.EVENT)
            };
        }
        return notificationInfos;
    }

    public synchronized void bundleChanged(BundleEvent event) {
        Notification notification = new Notification(BundleStateMBean.EVENT, this, ++sequenceNumber,
                System.currentTimeMillis());

        try {
            Bundle bundle = event.getBundle();
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(IDENTIFIER, bundle.getBundleId());
            values.put(LOCATION, bundle.getLocation());
            values.put(SYMBOLIC_NAME, bundle.getSymbolicName());
            values.put(EVENT, event.getType());
            notification.setUserData(new CompositeDataSupport(BundleStateMBean.BUNDLE_EVENT_TYPE, values));

            nbs.sendNotification(notification);
        } catch (OpenDataException e) {
           // todo: handle somehow
        }
    }
}
