/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.test.commons.assertions;

import org.junit.Assert;
import org.osgi.framework.BundleContext;
import org.osgilab.testing.test.commons.utils.BundleContextUtil;

/**
 * Abstract OSGi Asset class with BundleContext Handling
 * @author dpishchukhin
 */
abstract class OSGiAssert {
    /**
     * Asserts BundleContext from BundleContextUtil before return.
     * @return BundleContext
     */
    protected static BundleContext getBundleContext() {
        BundleContext bc = BundleContextUtil.getInstance().getBundleContext();
        Assert.assertNotNull("BundleContext is null", bc);
        return bc;
    }
}
