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
import org.osgi.service.log.LogService;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.osgi.util.tracker.ServiceTracker;
import org.osgilab.bundles.jmx.beans.LogVisitor;
import org.osgilab.bundles.jmx.beans.OsgiVisitor;
import org.osgilab.bundles.jmx.framework.BundleState;
import org.osgilab.bundles.jmx.framework.Framework;
import org.osgilab.bundles.jmx.framework.PackageState;
import org.osgilab.bundles.jmx.framework.ServiceState;

import javax.management.*;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JMX Management Model Activator
 *
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator, OsgiVisitor, LogVisitor {
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

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
    private ServiceTracker frameworkTracker;

    private ServiceTracker logServiceTracker;
    private Framework framework;
    private PackageState packageState;

    public void start(BundleContext context) throws Exception {
        bc = context;

        logServiceTracker = new ServiceTracker(bc, LogService.class.getName(), null);
        logServiceTracker.open();

        packageAdminTracker = new ServiceTracker(bc, PackageAdmin.class.getName(), null);
        packageAdminTracker.open();

        startLevelTracker = new ServiceTracker(bc, StartLevel.class.getName(), null);
        startLevelTracker.open();

        frameworkTracker = new ServiceTracker(bc, org.osgi.framework.launch.Framework.class.getName(), null);
        frameworkTracker.open();

        server = ManagementFactory.getPlatformMBeanServer();
        registerJmxBeans();

        info("JMX Management Model started", null);
    }

    public void stop(BundleContext context) throws Exception {
        unregisterJmxBeans();

        if (frameworkTracker != null) {
            frameworkTracker.close();
            frameworkTracker = null;
        }

        if (startLevelTracker != null) {
            startLevelTracker.close();
            startLevelTracker = null;
        }

        if (packageAdminTracker != null) {
            packageAdminTracker.close();
            packageAdminTracker = null;
        }

        info("JMX Management Model stoppped", null);

        if (logServiceTracker != null) {
            logServiceTracker.close();
            logServiceTracker = null;
        }

        bc = null;
    }

    private void unregisterJmxBeans() throws InstanceNotFoundException, MBeanRegistrationException {
        server.unregisterMBean(packageStateMBeanObjectName);
        packageState.uninit();

        bc.removeServiceListener(serviceState);
        server.unregisterMBean(serviceStateMBeanObjectName);
        serviceState.uninit();

        bc.removeBundleListener(bundleState);
        server.unregisterMBean(bundleStateMBeanObjectName);
        bundleState.uninit();
        
        server.unregisterMBean(frameworkMBeanObjectName);
        framework.uninit();
    }

    private void registerJmxBeans() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        frameworkMBeanObjectName = new ObjectName(FrameworkMBean.OBJECTNAME);
        framework = new Framework();
        framework.setVisitor(this);
        framework.setLogVisitor(this);
        server.registerMBean(framework, frameworkMBeanObjectName);

        bundleStateMBeanObjectName = new ObjectName(BundleStateMBean.OBJECTNAME);
        bundleState = new BundleState();
        bundleState.setVisitor(this);
        bundleState.setLogVisitor(this);
        bc.addBundleListener(bundleState);
        server.registerMBean(bundleState, bundleStateMBeanObjectName);

        serviceStateMBeanObjectName = new ObjectName(ServiceStateMBean.OBJECTNAME);
        serviceState = new ServiceState();
        serviceState.setVisitor(this);
        serviceState.setLogVisitor(this);
        bc.addServiceListener(serviceState);
        server.registerMBean(serviceState, serviceStateMBeanObjectName);

        packageStateMBeanObjectName = new ObjectName(PackageStateMBean.OBJECTNAME);
        packageState = new PackageState();
        packageState.setVisitor(this);
        packageState.setLogVisitor(this);
        server.registerMBean(packageState, packageStateMBeanObjectName);
    }

    public void debug(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_DEBUG, message, throwable);
        } else {
            LOG.log(Level.FINE, message, throwable);
        }
    }

    public void info(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_INFO, message, throwable);
        } else {
            LOG.log(Level.INFO, message, throwable);
        }
    }

    public void warning(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_WARNING, message, throwable);
        } else {
            LOG.log(Level.WARNING, message, throwable);
        }
    }

    public void error(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_ERROR, message, throwable);
        } else {
            LOG.log(Level.SEVERE, message, throwable);
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return new ServiceReference[0];
    }

    public PackageAdmin getPackageAdmin() {
        return (PackageAdmin) packageAdminTracker.getService();
    }

    public StartLevel getStartLevel() {
        return (StartLevel) startLevelTracker.getService();
    }

    public Bundle installBundle(String location) throws BundleException {
        return bc.installBundle(location);
    }

    public Bundle installBundle(String location, InputStream stream) throws BundleException {
        return bc.installBundle(location, stream);
    }

    public org.osgi.framework.launch.Framework getFramework() {
        return (org.osgi.framework.launch.Framework) frameworkTracker.getService(); 
    }

    public Bundle[] getBundles() {
        return bc.getBundles();
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
