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
                frameworks(equinox()),
                provision(
                        mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.2.0"),
                        bundle("http://mirror.selfnet.de/eclipse/equinox/drops/R-3.5.2-201002111343/org.eclipse.equinox.event_1.1.101.R35x_v20100209.jar"),
                        bundle("http://mirror.selfnet.de/eclipse/equinox/drops/R-3.5.2-201002111343/org.eclipse.equinox.log_1.2.0.v20090520-1800.jar"),
                        mavenBundle().groupId("org.osgilab.bundles").artifactId("monitoradmin").version("1.0.0")
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
