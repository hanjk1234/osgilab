/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgi.testing.it.commons.springosgi;

import org.osgilab.testing.commons.assertions.OSGiAssert;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;

/**
 * Abstract test with all initializations
 *
 * @author dmytro.pishchukhin
 */
public abstract class AbstractTest extends AbstractConfigurableBundleCreatorTests {
    protected String[] getTestBundlesNames() {
        return new String[]{"org.junit, com.springsource.junit, 3.8.2",
                "org.osgilab.testing, commons, 1.0-SNAPSHOT"};
    }

    @Override
    protected void onSetUp() throws Exception {
        OSGiAssert.init(bundleContext);
    }
}
