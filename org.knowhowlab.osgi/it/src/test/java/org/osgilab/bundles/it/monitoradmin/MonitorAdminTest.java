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

package org.osgilab.bundles.it.monitoradmin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.monitor.MonitorAdmin;
import org.osgi.service.monitor.MonitorListener;
import org.osgi.service.monitor.MonitoringJob;
import org.osgilab.testing.commons.assertions.OSGiAssert;
import org.osgilab.testing.commons.assertions.ServiceAssert;
import org.osgilab.testing.commons.utils.BundleUtils;
import org.osgilab.testing.commons.utils.ServiceUtils;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * MonitorAdmin Integration test
 *
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class MonitorAdminTest {
    // OSGi framework test configutation

    @Configuration
    public static Option[] configuration() {
        return options(
                // list of frameworks to test
                frameworks(equinox(), felix(), knopflerfish().version("3.0.0")),
                // list of bundles that should be installd
                provision(
                        mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.1.0"),
                        mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.eventadmin").version("1.2.2"),
                        mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.log").version("1.0.0"),
                        mavenBundle().groupId("org.osgilab.testing").artifactId("commons").version("1.0.0"),
                        mavenBundle().groupId("org.osgilab.bundles").artifactId("monitoradmin").version("1.0.2-SNAPSHOT")
                )
        );
    }

    // injected BundleContext
    @Inject
    private BundleContext bc;

    @Before
    public void initAsserts() {
        OSGiAssert.init(bc);
    }

    @Test
    public void testServices() throws BundleException {
        // assert MonitorAdmin Service
        ServiceAssert.assertServiceAvailable(MonitorAdmin.class);

        // assert MonitorListener Service
        ServiceAssert.assertServiceAvailable(MonitorListener.class);

        // get MonitorAdmin service
        MonitorAdmin monitorAdmin = ServiceUtils.getService(bc, MonitorAdmin.class);

        // get list of Monitorable names
        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);

        // get list of running Monitoring jobs
        MonitoringJob[] runningJobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(runningJobs);
        Assert.assertEquals(0, runningJobs.length);

        // get bundle
        Bundle bundle = BundleUtils.findBundle(bc, "org.osgilab.bundles.monitoradmin");
        bundle.stop();

        // assert MonitorAdmin Service
        ServiceAssert.assertServiceUnavailable(MonitorAdmin.class);

        // assert MonitorListener Service
        ServiceAssert.assertServiceUnavailable(MonitorListener.class);
    }
}
