/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.jmx.framework.ServiceStateMBean;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

/**
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator {

    private MBeanServer server;
    private ObjectName frameworkMBeanObjectName;
    private ObjectName bundleStateMBeanObjectName;
    private ObjectName serviceStateMBeanObjectName;
    private ObjectName packageStateMBeanObjectName;

    public void start(BundleContext context) throws Exception {
        server = ManagementFactory.getPlatformMBeanServer();
        frameworkMBeanObjectName = new ObjectName(FrameworkMBean.OBJECTNAME);
        server.registerMBean(new StandardMBean(new Framework(),FrameworkMBean.class),
                frameworkMBeanObjectName);

        bundleStateMBeanObjectName = new ObjectName(BundleStateMBean.OBJECTNAME);
        server.registerMBean(new StandardMBean(new BundleState(),BundleStateMBean.class),
                bundleStateMBeanObjectName);

        serviceStateMBeanObjectName = new ObjectName(ServiceStateMBean.OBJECTNAME);
        server.registerMBean(new StandardMBean(new ServiceState(), ServiceStateMBean.class),
                serviceStateMBeanObjectName);

        packageStateMBeanObjectName = new ObjectName(PackageStateMBean.OBJECTNAME);
        server.registerMBean(new StandardMBean(new PackageState(), PackageStateMBean.class),
                packageStateMBeanObjectName);
    }

    public void stop(BundleContext context) throws Exception {
        server.unregisterMBean(packageStateMBeanObjectName);
        server.unregisterMBean(serviceStateMBeanObjectName);
        server.unregisterMBean(bundleStateMBeanObjectName);
        server.unregisterMBean(frameworkMBeanObjectName);
    }
}
