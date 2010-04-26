/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.jmx;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.jmx.framework.ServiceStateMBean;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * JMX Integration test
 *
 * @author dmytro.pishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class JmxTest {
    // OSGi framework test configutation
    @Configuration
    public static Option[] configuration() {
        return options(
                // list of frameworks to test
                frameworks(equinox(), felix(), knopflerfish().version("3.0.0")),
                // list of bundles that should be installd
                provision(
                        mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.2.0"),
                        mavenBundle().groupId("org.osgi").artifactId("org.osgi.enterprise").version("4.2.0"),
                        mavenBundle().groupId("org.osgilab.bundles").artifactId("jmx").version("1.0.1")
                )
        );
    }

    @Test
    public void testBeans() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        Assert.assertNotNull(server.getMBeanInfo(new ObjectName(FrameworkMBean.OBJECTNAME)));
        Assert.assertNotNull(server.getMBeanInfo(new ObjectName(PackageStateMBean.OBJECTNAME)));
        Assert.assertNotNull(server.getMBeanInfo(new ObjectName(BundleStateMBean.OBJECTNAME)));
        Assert.assertNotNull(server.getMBeanInfo(new ObjectName(ServiceStateMBean.OBJECTNAME)));
    }
}
