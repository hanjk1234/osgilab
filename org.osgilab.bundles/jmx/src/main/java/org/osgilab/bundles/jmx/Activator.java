/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.jmx.framework.ServiceStateMBean;
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

    public void start(BundleContext context) throws Exception {
        bc = context;

        registerJmxBeans();

        bc.addBundleListener(bundleState);
    }

    public void stop(BundleContext context) throws Exception {
        bc.removeBundleListener(bundleState);

        unregisterJmxBeans();
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
        server.registerMBean(new ServiceState(this), serviceStateMBeanObjectName);

        packageStateMBeanObjectName = new ObjectName(PackageStateMBean.OBJECTNAME);
        server.registerMBean(new PackageState(this), packageStateMBeanObjectName);
    }

    public Bundle getBundle(long id) {
        return bc.getBundle(id);
    }
}
