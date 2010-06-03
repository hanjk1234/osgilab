/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.assertions;

import org.junit.Assert;
import org.osgi.framework.BundleContext;

/**
 * Abstract OSGi Asset class with BundleContext Handling
 *
 * @author dpishchukhin
 */
public abstract class OSGiAssert {
    /**
     * BundleContext value
     */
    private static BundleContext bc;

    /**
     * Initialize all OSGi assertions with BundleContext value
     *
     * @param bc BundleContext value
     * @throws IllegalStateException utility class is already initialized
     */
    public static void init(BundleContext bc) {
        if (OSGiAssert.bc != null) {
            throw new IllegalStateException("BundleContext is already initialized");
        }
        OSGiAssert.bc = bc;
    }

    /**
     * Asserts BundleContext before return.
     *
     * @return BundleContext
     */
    protected static BundleContext getBundleContext() {
        Assert.assertNotNull("BundleContext is null", bc);
        return bc;
    }
}
