/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.knowhowlab.osgi.it.jmx;

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
                        mavenBundle().groupId("org.knowhowlab.osgi").artifactId("jmx").version("1.0.2")
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
