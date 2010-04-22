package org.osgilab.bundles.it;

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
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class MonitorAdminTest {
    @Configuration
    public static Option[] configuration() {
        return options(
                frameworks(equinox(), felix(), knopflerfish().version("3.0.0")),
                provision(
                        mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.1.0"),
                        mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.eventadmin").version("1.2.2"),
                        mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.log").version("1.0.0"),
                        mavenBundle().groupId("org.osgilab.bundles").artifactId("monitoradmin").version("1.0.1")
                )
        );
    }

    @Inject
    private BundleContext bc;

    @Test
    public void testServices() {
        ServiceReference reference = bc.getServiceReference(MonitorAdmin.class.getName());
        Assert.assertNotNull(reference);
        MonitorAdmin monitorAdmin = (MonitorAdmin) bc.getService(reference);
        Assert.assertNotNull(monitorAdmin);

        reference = bc.getServiceReference(MonitorListener.class.getName());
        Assert.assertNotNull(reference);
        MonitorListener monitorListener = (MonitorListener) bc.getService(reference);
        Assert.assertNotNull(monitorListener);

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);

        MonitoringJob[] runningJobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(runningJobs);
        Assert.assertEquals(0, runningJobs.length);
    }
}
