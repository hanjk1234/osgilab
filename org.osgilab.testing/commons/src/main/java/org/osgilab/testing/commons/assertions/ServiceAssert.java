/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.assertions;

/**
 * A set of OSGi services specific assertion methods useful for writing tests.
 * <p/>
 * Before use it should be initialized
 * {@link org.osgilab.testing.commons.assertions.OSGiAssert#init(org.osgi.framework.BundleContext)}
 *
 * @author dmytro.pishchukhin
 * @version 1.0
 * @see java.lang.AssertionError
 * @see org.osgilab.testing.commons.assertions.OSGiAssert
 */
public class ServiceAssert {
    public static void assertServiceAvailable(String[] classes, String filter) {
    }

    public static void assertServiceUnavailable(String[] classes, String filter) {
    }
}
