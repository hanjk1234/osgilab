/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.it.commons.paxrunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgilab.testing.commons.assertions.OSGiAssert;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.osgilab.testing.commons.assertions.BundleAssert.assertBundleAvailable;
import static org.osgilab.testing.commons.assertions.BundleAssert.assertBundleState;

/**
 * BundleAssert test
 * @author dmytro.pishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class BundlesTest {
    /**
     * Injected BundleContext
     */
    @Inject
    protected BundleContext bc;

    /**
     * Runner config
     * @return config
     */
    @Configuration
    public static Option[] configuration() {
        return options(
                // list of frameworks to test
                allFrameworksVersions(),
                // list of bundles that should be installd
                provision(
                        mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.0.1"),
                        mavenBundle().groupId("org.osgilab.testing").artifactId("commons").version("1.0-SNAPSHOT")
                )
        );
    }


    /**
     * Init OSGiAssert with BundleContext
     */
    @Before
    public void initOSGiAssert() {
        OSGiAssert.init(bc);
    }

    @Test
    public void simpleTest() {
        // assert bundle with id=1 installed into OSGi framework
        assertBundleAvailable(1);
        // assert bundle with id=1 active
        assertBundleState(Bundle.ACTIVE, 1);
    }
}
