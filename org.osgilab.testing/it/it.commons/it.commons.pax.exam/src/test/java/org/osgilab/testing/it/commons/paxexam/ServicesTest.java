/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.it.commons.paxexam;

import org.junit.Test;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgilab.testing.commons.assertions.ServiceAssert;
import org.osgilab.testing.commons.utils.FilterUtils;

/**
 * ServiceAssert test
 *
 * @author dmytro.pishchukhin
 */
public class ServicesTest extends AbstractTest {
    @Test
    public void simpleTest() throws InvalidSyntaxException {
        // assert PackageAdmin service is available in OSGi registry
        ServiceAssert.assertServiceAvailable(PackageAdmin.class);
        // assert MonitorAdmin service is unavailable in OSGi registry
        ServiceAssert.assertServiceUnavailable("org.osgi.service.monitor.MonitorAdmin");

        // assert PackageAdmin service is available in OSGi registry
        ServiceAssert.assertServiceAvailable(FilterUtils.create(PackageAdmin.class));
        // assert MonitorAdmin service is unavailable in OSGi registry
        ServiceAssert.assertServiceUnavailable(FilterUtils.create("org.osgi.service.monitor.MonitorAdmin"));
    }
}
