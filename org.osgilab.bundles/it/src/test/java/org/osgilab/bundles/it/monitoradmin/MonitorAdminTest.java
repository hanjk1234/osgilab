/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.monitoradmin;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.monitor.MonitorAdmin;
import org.osgi.service.monitor.MonitorListener;
import org.osgi.service.monitor.MonitoringJob;

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
                        mavenBundle().groupId("org.osgilab.bundles").artifactId("monitoradmin").version("1.0.1")
                )
        );
    }

    // injected BundleContext
    @Inject
    private BundleContext bc;

    @Test
    public void testServices() {
        // get MonitorAdmin Service reference
        ServiceReference reference = bc.getServiceReference(MonitorAdmin.class.getName());
        Assert.assertNotNull(reference);
        // get MonitorAdmin service
        MonitorAdmin monitorAdmin = (MonitorAdmin) bc.getService(reference);
        Assert.assertNotNull(monitorAdmin);

        // get MonitorListener Service reference
        reference = bc.getServiceReference(MonitorListener.class.getName());
        Assert.assertNotNull(reference);
        // get MonitorListener service
        MonitorListener monitorListener = (MonitorListener) bc.getService(reference);
        Assert.assertNotNull(monitorListener);

        // get list of Monitorable names
        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);

        // get list of running Monitoring jobs
        MonitoringJob[] runningJobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(runningJobs);
        Assert.assertEquals(0, runningJobs.length);
    }
}
