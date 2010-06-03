/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.it.commons.paxrunner;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import static org.osgilab.testing.commons.assertions.BundleAssert.*;

/**
 * BundleAssert test
 *
 * @author dmytro.pishchukhin
 */
public class BundlesTest extends AbstractTest {
    @Test
    public void simpleTest() {
        // assert bundle with id=1 is installed into OSGi framework
        assertBundleAvailable(1);
        // assert bundle with id=1 active
        assertBundleState(Bundle.ACTIVE, 1);
        // assert bundle with id=100 is not installed into OSGi framework
        assertBundleUnavailable(100);
        // assert bundle with symbolic name "org.osgilab.testing.commons" is installed into OSGi framework
        assertBundleAvailable("org.osgilab.testing.commons");
        // assert bundle with symbolic name "org.osgilab.testing.commons" and version "1.0.0.SNAPSHOT"
        // is installed into OSGi framework
        assertBundleAvailable("org.osgilab.testing.commons", new Version("1.0.0.SNAPSHOT"));
        // assert bundle with symbolic name "org.osgilab.testing.commons" and version "1.0.0"
        // is not installed into OSGi framework
        assertBundleUnavailable("org.osgilab.testing.commons", new Version("2.0.0"));
    }
}
