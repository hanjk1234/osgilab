/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.assertions;

import org.junit.Assert;
import org.osgi.framework.Filter;
import org.osgilab.testing.commons.utils.ServiceUtils;

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
public class ServiceAssert extends OSGiAssert {
    /**
     * Utility class. Only static methods are available.
     */
    private ServiceAssert() {
    }

    /**
     * Asserts that service with class is available in OSGi registry. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param clazz service class
     */
    public static void assertServiceAvailable(Class clazz) {
        assertServiceAvailable(null, clazz);
    }

    /**
     * Asserts that service with class is available in OSGi registry. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message message
     * @param clazz service class
     */
    public static void assertServiceAvailable(String message, Class clazz) {
        Assert.assertNotNull("Class is null", clazz);
        //noinspection unchecked
        Object service = ServiceUtils.getService(getBundleContext(), clazz);
        Assert.assertNotNull(message, service);
    }

    /**
     * Asserts that service with class name is available in OSGi registry. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param className service class name
     */
    public static void assertServiceAvailable(String className) {
        assertServiceAvailable(null, className);
    }

    /**
     * Asserts that service with class name is available in OSGi registry. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message message
     * @param className service class name
     */
    public static void assertServiceAvailable(String message, String className) {
        Assert.assertNotNull("Class name is null", className);
        //noinspection unchecked
        Object service = ServiceUtils.getService(getBundleContext(), className);
        Assert.assertNotNull(message, service);
    }

    /**
     * Asserts that service with filter is available in OSGi registry. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param filter service filter
     */
    public static void assertServiceAvailable(Filter filter) {
        assertServiceAvailable(null, filter);
    }

    /**
     * Asserts that service with filter is available in OSGi registry. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message message
     * @param filter service filter
     */
    public static void assertServiceAvailable(String message, Filter filter) {
        Assert.assertNotNull("Filter is null", filter);
        //noinspection unchecked
        Object service = ServiceUtils.getService(getBundleContext(), filter);
        Assert.assertNotNull(message, service);
    }

    /**
     * Asserts that service with class is unavailable in OSGi registry. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param clazz service class
     */
    public static void assertServiceUnavailable(Class clazz) {
        assertServiceUnavailable(null, clazz);
    }

    /**
     * Asserts that service with class is unavailable in OSGi registry. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message message
     * @param clazz service class
     */
    public static void assertServiceUnavailable(String message, Class clazz) {
        Assert.assertNotNull("Clas is null", clazz);
        //noinspection unchecked
        Object service = ServiceUtils.getService(getBundleContext(), clazz);
        Assert.assertNull(message, service);
    }

    /**
     * Asserts that service with class name is unavailable in OSGi registry. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param className service class name
     */
    public static void assertServiceUnavailable(String className) {
        assertServiceUnavailable(null, className);
    }

    /**
     * Asserts that service with class name is unavailable in OSGi registry. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message message
     * @param className service class name
     */
    public static void assertServiceUnavailable(String message, String className) {
        Assert.assertNotNull("Class name is null", className);
        //noinspection unchecked
        Object service = ServiceUtils.getService(getBundleContext(), className);
        Assert.assertNull(message, service);
    }

    /**
     * Asserts that service with filter is unavailable in OSGi registry. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param filter service filter
     */
    public static void assertServiceUnavailable(Filter filter) {
        assertServiceUnavailable(null, filter);
    }

    /**
     * Asserts that service with filter is unavailable in OSGi registry. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message message
     * @param filter service filter
     */
    public static void assertServiceUnavailable(String message, Filter filter) {
        Assert.assertNotNull("Filter is null", filter);
        //noinspection unchecked
        Object service = ServiceUtils.getService(getBundleContext(), filter);
        Assert.assertNull(message, service);
    }

    // todo: assertServiceAvail(timouts)
}
