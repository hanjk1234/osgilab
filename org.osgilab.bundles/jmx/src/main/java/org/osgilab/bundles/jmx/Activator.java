/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.framework.*;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.jmx.framework.ServiceStateMBean;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.osgi.util.tracker.ServiceTracker;
import org.osgilab.bundles.jmx.beans.BundleState;
import org.osgilab.bundles.jmx.beans.Framework;
import org.osgilab.bundles.jmx.beans.PackageState;
import org.osgilab.bundles.jmx.beans.ServiceState;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator, OsgiVisitor {
    private MBeanServer server;
    private ObjectName frameworkMBeanObjectName;
    private ObjectName bundleStateMBeanObjectName;
    private ObjectName serviceStateMBeanObjectName;
    private ObjectName packageStateMBeanObjectName;
    private BundleContext bc;
    private BundleState bundleState;
    private ServiceState serviceState;
    private ServiceTracker packageAdminTracker;
    private ServiceTracker startLevelTracker;

    public void start(BundleContext context) throws Exception {
        bc = context;

        packageAdminTracker = new ServiceTracker(bc, PackageAdmin.class.getName(), null);
        packageAdminTracker.open();

        startLevelTracker = new ServiceTracker(bc, StartLevel.class.getName(), null);
        startLevelTracker.open();

        registerJmxBeans();

        bc.addBundleListener(bundleState);
        bc.addServiceListener(serviceState);
    }

    public void stop(BundleContext context) throws Exception {
        bc.removeServiceListener(serviceState);
        bc.removeBundleListener(bundleState);

        unregisterJmxBeans();

        startLevelTracker.close();
        startLevelTracker = null;

        packageAdminTracker.close();
        packageAdminTracker = null;

        bc = null;
    }

    private void unregisterJmxBeans() throws InstanceNotFoundException, MBeanRegistrationException {
        server.unregisterMBean(packageStateMBeanObjectName);
        server.unregisterMBean(serviceStateMBeanObjectName);
        server.unregisterMBean(bundleStateMBeanObjectName);
        server.unregisterMBean(frameworkMBeanObjectName);
    }

    private void registerJmxBeans() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        server = ManagementFactory.getPlatformMBeanServer();

        frameworkMBeanObjectName = new ObjectName(FrameworkMBean.OBJECTNAME);
        server.registerMBean(new Framework(this), frameworkMBeanObjectName);

        bundleStateMBeanObjectName = new ObjectName(BundleStateMBean.OBJECTNAME);
        bundleState = new BundleState(this);
        server.registerMBean(bundleState, bundleStateMBeanObjectName);

        serviceStateMBeanObjectName = new ObjectName(ServiceStateMBean.OBJECTNAME);
        serviceState = new ServiceState(this);
        server.registerMBean(serviceState, serviceStateMBeanObjectName);

        packageStateMBeanObjectName = new ObjectName(PackageStateMBean.OBJECTNAME);
        server.registerMBean(new PackageState(this), packageStateMBeanObjectName);
    }

    public Bundle getBundle(long id) {
        return bc.getBundle(id);
    }

    public ServiceReference getServiceReferenceById(long id) {
        try {
            ServiceReference[] serviceReferences = bc.getAllServiceReferences(null, createServiceIdFilter(id));
            if (serviceReferences != null && serviceReferences.length == 1) {
                return serviceReferences[0];
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();  // todo
        }
        return null;
    }

    public ServiceReference[] getAllServiceReferences() {
        try {
            ServiceReference[] serviceReferences = bc.getAllServiceReferences(null, null);
            if (serviceReferences == null) {
                return new ServiceReference[0];
            }
            return serviceReferences;
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();  // todo
        }
        return new ServiceReference[0];
    }

    public PackageAdmin getPackageAdmin() {
        return (PackageAdmin) packageAdminTracker.getService();
    }

    public StartLevel getStartLevel() {
        return (StartLevel) startLevelTracker.getService();
    }

    private String createServiceIdFilter(long id) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append(Constants.SERVICE_ID);
        builder.append('=');
        builder.append(id);
        builder.append(')');
        return builder.toString();
    }
}
