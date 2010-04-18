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
import org.osgi.jmx.service.cm.ConfigurationAdminMBean;
import org.osgi.jmx.service.permissionadmin.PermissionAdminMBean;
import org.osgi.jmx.service.provisioning.ProvisioningServiceMBean;
import org.osgi.jmx.service.useradmin.UserAdminMBean;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;
import org.osgi.service.monitor.MonitorAdmin;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.permissionadmin.PermissionAdmin;
import org.osgi.service.provisioning.ProvisioningService;
import org.osgi.service.startlevel.StartLevel;
import org.osgi.service.useradmin.UserAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.osgilab.bundles.jmx.beans.LogVisitor;
import org.osgilab.bundles.jmx.beans.OsgiVisitor;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;
import org.osgilab.bundles.jmx.beans.framework.BundleState;
import org.osgilab.bundles.jmx.beans.framework.Framework;
import org.osgilab.bundles.jmx.beans.framework.PackageState;
import org.osgilab.bundles.jmx.beans.framework.ServiceState;
import org.osgilab.bundles.jmx.service.monitor.MonitorAdminMBean;

import javax.management.*;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JMX Management Model Activator
 *
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator, OsgiVisitor, LogVisitor {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

    /**
     * JMX server instance
     */
    private MBeanServer server;

    /**
     * Bundle context
     */
    private BundleContext bc;

    /**
     * {@link FrameworkMBean} implementation instance
     */
    private Framework framework;
    /**
     * {@link PackageStateMBean} implementation instance
     */
    private PackageState packageState;
    /**
     * {@link BundleStateMBean} implementation instance
     */
    private BundleState bundleState;
    /**
     * {@link ServiceStateMBean} implementation instance
     */
    private ServiceState serviceState;

    /**
     * ServiceTracker for {@link PackageAdmin} services
     */
    private ServiceTracker packageAdminTracker;
    /**
     * ServiceTracker for {@link StartLevel} services
     */
    private ServiceTracker startLevelTracker;
    /**
     * ServiceTracker for {@link org.osgi.framework.launch.Framework} services
     */
    private ServiceTracker frameworkTracker;
    /**
     * ServiceTracker for {@link LogService} services
     */
    private ServiceTracker logServiceTracker;

    /**
     * ServiceTracker for {@link org.osgi.service.cm.ConfigurationAdmin} services
     */
    private ServiceTracker configurationAdminTracker;
    /**
     * ServiceTracker for {@link org.osgi.service.permissionadmin.PermissionAdmin} services
     */
    private ServiceTracker permissionAdminTracker;
    /**
     * ServiceTracker for {@link org.osgi.service.provisioning.ProvisioningService} services
     */
    private ServiceTracker provisioningServiceTracker;
    /**
     * ServiceTracker for {@link org.osgi.service.useradmin.UserAdmin} services
     */
    private ServiceTracker userAdminTracker;
    /**
     * ServiceTracker for {@link org.osgi.service.monitor.MonitorAdmin} services
     */
    private ServiceTracker monitorAdminTracker;

    /**
     * Compendium services MBean registration map.
     */
    private Map<String,ServiceAbstractMBean> compendiumServices = new HashMap<String, ServiceAbstractMBean>();

    public void start(BundleContext context) throws Exception {
        bc = context;

        logServiceTracker = new ServiceTracker(bc, LogService.class.getName(), null);
        logServiceTracker.open();

        server = ManagementFactory.getPlatformMBeanServer();

        registerCoreTrackers();

        registerJmxBeans();

        registerCompendiumTrackers();

        info("JMX Management Model started", null);
    }

    public void stop(BundleContext context) throws Exception {
        unregisterCompendiumTrackers();

        unregisterJmxBeans();

        unregisterCoreTrackers();

        info("JMX Management Model stoppped", null);

        server = null;

        if (logServiceTracker != null) {
            logServiceTracker.close();
            logServiceTracker = null;
        }

        bc = null;
    }

    private void unregisterCompendiumTrackers() {
        if (monitorAdminTracker != null) {
            monitorAdminTracker.close();
            monitorAdminTracker = null;
        }

        if (userAdminTracker != null) {
            userAdminTracker.close();
            userAdminTracker = null;
        }

        if (provisioningServiceTracker != null) {
            provisioningServiceTracker.close();
            provisioningServiceTracker = null;
        }

        if (permissionAdminTracker != null) {
            permissionAdminTracker.close();
            permissionAdminTracker = null;
        }

        if (configurationAdminTracker != null) {
            configurationAdminTracker.close();
            configurationAdminTracker = null;
        }
    }

    private void unregisterCoreTrackers() {
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
    }

    private void registerCompendiumTrackers() {
        configurationAdminTracker = new ServiceTracker(bc, ConfigurationAdmin.class.getName(),
                new CompendiumServiceCustomizer<ConfigurationAdmin>(org.osgilab.bundles.jmx.service.cm.ConfigurationAdmin.class,
                        ConfigurationAdminMBean.OBJECTNAME));
        configurationAdminTracker.open();

        permissionAdminTracker = new ServiceTracker(bc, PermissionAdmin.class.getName(),
                new CompendiumServiceCustomizer<PermissionAdmin>(org.osgilab.bundles.jmx.service.permissionadmin.PermissionAdmin.class,
                        PermissionAdminMBean.OBJECTNAME));
        permissionAdminTracker.open();

        provisioningServiceTracker = new ServiceTracker(bc, ProvisioningService.class.getName(),
                new CompendiumServiceCustomizer<ProvisioningService>(org.osgilab.bundles.jmx.service.provisioning.ProvisioningService.class,
                        ProvisioningServiceMBean.OBJECTNAME));
        provisioningServiceTracker.open();

        userAdminTracker = new ServiceTracker(bc, UserAdmin.class.getName(),
                new CompendiumServiceCustomizer<UserAdmin>(org.osgilab.bundles.jmx.service.useradmin.UserAdmin.class,
                        UserAdminMBean.OBJECTNAME));
        userAdminTracker.open();

        monitorAdminTracker = new ServiceTracker(bc, MonitorAdmin.class.getName(),
                new CompendiumServiceCustomizer<MonitorAdmin>(org.osgilab.bundles.jmx.service.monitor.MonitorAdmin.class,
                        MonitorAdminMBean.OBJECTNAME));
        monitorAdminTracker.open();
    }

    private void registerCoreTrackers() {
        packageAdminTracker = new ServiceTracker(bc, PackageAdmin.class.getName(), null);
        packageAdminTracker.open();

        startLevelTracker = new ServiceTracker(bc, StartLevel.class.getName(), null);
        startLevelTracker.open();

        frameworkTracker = new ServiceTracker(bc, org.osgi.framework.launch.Framework.class.getName(), null);
        frameworkTracker.open();
    }

    private void unregisterJmxBeans()
            throws InstanceNotFoundException, MBeanRegistrationException, MalformedObjectNameException {
        server.unregisterMBean(new ObjectName(PackageStateMBean.OBJECTNAME));
        packageState.uninit();

        bc.removeServiceListener(serviceState);
        server.unregisterMBean(new ObjectName(ServiceStateMBean.OBJECTNAME));
        serviceState.uninit();

        bc.removeBundleListener(bundleState);
        server.unregisterMBean(new ObjectName(BundleStateMBean.OBJECTNAME));
        bundleState.uninit();
        
        server.unregisterMBean(new ObjectName(FrameworkMBean.OBJECTNAME));
        framework.uninit();
    }

    private void registerJmxBeans()
            throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        framework = new Framework();
        framework.setVisitor(this);
        framework.setLogVisitor(this);
        server.registerMBean(framework, new ObjectName(FrameworkMBean.OBJECTNAME));

        bundleState = new BundleState();
        bundleState.setVisitor(this);
        bundleState.setLogVisitor(this);
        bc.addBundleListener(bundleState);
        server.registerMBean(bundleState, new ObjectName(BundleStateMBean.OBJECTNAME));

        serviceState = new ServiceState();
        serviceState.setVisitor(this);
        serviceState.setLogVisitor(this);
        bc.addServiceListener(serviceState);
        server.registerMBean(serviceState, new ObjectName(ServiceStateMBean.OBJECTNAME));

        packageState = new PackageState();
        packageState.setVisitor(this);
        packageState.setLogVisitor(this);
        server.registerMBean(packageState, new ObjectName(PackageStateMBean.OBJECTNAME));
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

    private String createObjectName(String objectNamePrefix, long serviceId) {
        return objectNamePrefix + ",service.id=" + serviceId;
    }

    private class CompendiumServiceCustomizer<T> implements ServiceTrackerCustomizer {
        private Class<? extends ServiceAbstractMBean<T>> beanClass;
        private String objectNamePrefix;

        private CompendiumServiceCustomizer(Class<? extends ServiceAbstractMBean<T>> beanClass, String objectNamePrefix) {
            this.beanClass = beanClass;
            this.objectNamePrefix = objectNamePrefix;
        }

        public Object addingService(ServiceReference reference) {
            long serviceId = (Long) reference.getProperty(Constants.SERVICE_ID);
            T service = (T) bc.getService(reference);
            try {
                ServiceAbstractMBean<T> mBean = beanClass.newInstance();
                mBean.setVisitor(Activator.this);
                mBean.setLogVisitor(Activator.this);
                mBean.setService(service);
                String objectName = createObjectName(objectNamePrefix, serviceId);
                compendiumServices.put(objectName, mBean);
                server.registerMBean(mBean, new ObjectName(objectName));
                info("MBean registered for " + beanClass.getSimpleName() + " service.id: " + serviceId, null);
            } catch (Exception e) {
                warning("Unable to register MBean for " + beanClass.getSimpleName() + " service.id: " + serviceId, e);
            }
            return service;
        }

        public void modifiedService(ServiceReference reference, Object service) {
            // do nothing
        }

        public void removedService(ServiceReference reference, Object service) {
            long serviceId = (Long) reference.getProperty(Constants.SERVICE_ID);
            try {
                String objectName = createObjectName(objectNamePrefix, serviceId);
                ServiceAbstractMBean serviceMBean = compendiumServices.get(objectName);
                server.unregisterMBean(new ObjectName(objectName));
                serviceMBean.uninit();
                info("MBean unregistered for " + beanClass.getSimpleName() + " service.id: " + serviceId, null);
            } catch (Exception e) {
                warning("Unable to unregister MBean for " + beanClass.getSimpleName() + " service.id: " + serviceId, e);
            }

            bc.ungetService(reference);
        }
    }
}
