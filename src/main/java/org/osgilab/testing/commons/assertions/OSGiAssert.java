/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.assertions;

import junit.framework.Assert;
import org.osgi.framework.BundleContext;

/**
 * Abstract OSGi Asset class with BundleContext Handling
 *
 * @author dpishchukhin
 * @version 1.0
 * @see java.lang.AssertionError
 * @see org.osgi.framework.BundleContext
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
