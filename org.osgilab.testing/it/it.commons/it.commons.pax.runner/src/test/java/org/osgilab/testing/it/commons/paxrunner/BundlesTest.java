/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.it.commons.paxrunner;

import org.junit.Test;
import org.osgi.framework.Bundle;

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
    }
}
