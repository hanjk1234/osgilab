package org.knowhowlab.osgi.it.monitoradmin;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.commons.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.commons.assertions.OSGiAssert;
import org.knowhowlab.osgi.testing.commons.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.commons.utils.BundleUtils;
import org.knowhowlab.osgi.testing.commons.utils.ServiceUtils;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.condpermadmin.ConditionInfo;
import org.osgi.service.condpermadmin.ConditionalPermissionAdmin;
import org.osgi.service.condpermadmin.ConditionalPermissionInfo;
import org.osgi.service.condpermadmin.ConditionalPermissionUpdate;
import org.osgi.service.monitor.MonitorPermission;
import org.osgi.service.permissionadmin.PermissionAdmin;
import org.osgi.service.permissionadmin.PermissionInfo;

import java.util.List;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.vmOptions;

/**
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class MonitorAdminSecurityTest {
    @Configuration
    public static Option[] configuration() {
        String basedir = System.getProperty("basedir");
        return options(
                // list of frameworks to test
                customFramework("felix", String.format("file:%s/felix-security-platform.xml", basedir), "felix"),
                // list of bundles that should be installed
                provision(
                        mavenBundle().groupId("org.knowhowlab.osgi.testing").artifactId("commons").version("1.0.1-SNAPSHOT"),
                        mavenBundle().groupId("org.knowhowlab.osgi").artifactId("monitoradmin").version("1.0.2-SNAPSHOT"),
                        mavenBundle().groupId("org.knowhowlab.osgi.manual-tests").artifactId("test-monitorable").version("1.0.0-SNAPSHOT")
                ),
                vmOptions(String.format("-Dorg.osgi.framework.security=\"osgi\" -Djava.security.policy=%s/all.policy", basedir))
        );
    }

    // injected BundleContext
    @Inject
    private BundleContext bc;

    @Before
    public void init() {
        OSGiAssert.init(bc);

        ServiceAssert.assertServiceAvailable(PermissionAdmin.class);
        permissionAdmin = ServiceUtils.getService(bc, PermissionAdmin.class);

        ServiceAssert.assertServiceAvailable(ConditionalPermissionAdmin.class);
        conditionalPermissionAdmin = ServiceUtils.getService(bc, ConditionalPermissionAdmin.class);

        BundleAssert.assertBundleAvailable("org.knowhowlab.osgi.manual-tests.test-monitorable");
        svProviderBundle = BundleUtils.findBundle(bc, "org.knowhowlab.osgi.manual-tests.test-monitorable");

        BundleAssert.assertBundleAvailable("pax-exam-probe");
        svConsumerBundle = BundleUtils.findBundle(bc, "pax-exam-probe");
    }

    private PermissionAdmin permissionAdmin;
    private ConditionalPermissionAdmin conditionalPermissionAdmin;

    private Bundle svProviderBundle;
    private Bundle svConsumerBundle;

    @Test
    @Ignore
    public void test() {
        ConditionalPermissionUpdate update = conditionalPermissionAdmin.newConditionalPermissionUpdate();
        List list = update.getConditionalPermissionInfos();
        for (Object o : list) {
            System.out.println(o);
        }

        ConditionInfo[] conditions = new ConditionInfo[]{new ConditionInfo("org.osgi.service.condpermadmin.BundleLocationCondition", new String[]{svProviderBundle.getLocation()})};
        PermissionInfo[] permissions = new PermissionInfo[] {new PermissionInfo("org.osgi.service.monitor.MonitorPermission", "test.monitorable\\*", MonitorPermission.PUBLISH)};

        ConditionalPermissionInfo conditionalPermissionInfo = conditionalPermissionAdmin.newConditionalPermissionInfo(null,
                conditions, permissions, ConditionalPermissionInfo.DENY);

        update.getConditionalPermissionInfos().add(conditionalPermissionInfo);
        update.commit();
    }
}
