/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.assertions;

import org.junit.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.osgilab.testing.commons.utils.BundleUtils;

/**
 * A set of OSGi Bundle specific assertion methods useful for writing tests.
 *
 * @author dmytro.pishchukhin
 * @see AssertionError
 */
public class BundleAssert extends OSGiAssert {
    /**
     * Utility class. Only static methods are available.
     */
    private BundleAssert() {
    }

    /**
     * Asserts that Bundle with bundleId has given state value. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param state    bundle state value
     * @param bundleId bundle id
     */
    public static void assertBundleState(int state, long bundleId) {
        assertBundleState(null, state, bundleId);
    }

    /**
     * Asserts that Bundle with bundleId has given state value. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message  message
     * @param state    bundle state value
     * @param bundleId bundle id
     */
    public static void assertBundleState(String message, int state, long bundleId) {
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), bundleId);
        Assert.assertNotNull(String.format("Unknown bundle with ID: %d", bundleId), bundle);
        Assert.assertEquals(message, state, bundle.getState());
    }

    /**
     * Asserts that Bundle with symbolic name has given state value. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param state        bundle state value
     * @param symbolicName symbolic name
     */
    public static void assertBundleState(int state, String symbolicName) {
        assertBundleState(null, state, symbolicName);
    }

    /**
     * Asserts that Bundle with symbolic name has given state value. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message      message
     * @param state        bundle state value
     * @param symbolicName symbolic name
     */
    public static void assertBundleState(String message, int state, String symbolicName) {
        Assert.assertNotNull("SymbolicName is null", symbolicName);
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), symbolicName);
        Assert.assertNotNull(String.format("Unknown bundle with SymbolicName: %s", symbolicName), bundle);
        Assert.assertEquals(message, state, bundle.getState());
    }

    /**
     * Asserts that Bundle with symbolic name and version has given state value. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param state        bundle state value
     * @param symbolicName symbolic name
     * @param version      version
     */
    public static void assertBundleState(int state, String symbolicName, Version version) {
        assertBundleState(null, state, symbolicName, version);
    }

    /**
     * Asserts that Bundle with symbolic name and version has given state value. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message      message
     * @param state        bundle state value
     * @param symbolicName symbolic name
     * @param version      version
     */
    public static void assertBundleState(String message, int state, String symbolicName, Version version) {
        Assert.assertNotNull("SymbolicName is null", symbolicName);
        Assert.assertNotNull("Version is null", version);
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), symbolicName, version);
        Assert.assertNotNull(String.format("Unknown bundle with SymbolicName: %s and version: %s", symbolicName, version), bundle);
        Assert.assertEquals(message, state, bundle.getState());
    }

    /**
     * Asserts that Bundle with bundleId is available in OS. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param bundleId bundle id
     */
    public static void assertBundleAvailable(long bundleId) {
        assertBundleAvailable(null, bundleId);
    }

    /**
     * Asserts that Bundle with bundleId is available in OS. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message  message
     * @param bundleId bundle id
     */
    public static void assertBundleAvailable(String message, long bundleId) {
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), bundleId);
        Assert.assertNotNull(message, bundle);
    }

    /**
     * Asserts that Bundle with symbolic name is available in OS. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param symbolicName symbolic name
     */
    public static void assertBundleAvailable(String symbolicName) {
        assertBundleAvailable(null, symbolicName);
    }

    /**
     * Asserts that Bundle with symbolic name is available in OS. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message      message
     * @param symbolicName symbolic name
     */
    public static void assertBundleAvailable(String message, String symbolicName) {
        Assert.assertNotNull("SymbolicName is null", symbolicName);
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), symbolicName);
        Assert.assertNotNull(message, bundle);
    }

    /**
     * Asserts that Bundle with symbolic name and version is available in OS. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param symbolicName symbolic name
     * @param version      version
     */
    public static void assertBundleAvailable(String symbolicName, Version version) {
        assertBundleAvailable(null, symbolicName, version);
    }

    /**
     * Asserts that Bundle with symbolic name and version is available in OS. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message      message
     * @param symbolicName symbolic name
     * @param version      version
     */
    public static void assertBundleAvailable(String message, String symbolicName, Version version) {
        Assert.assertNotNull("SymbolicName is null", symbolicName);
        Assert.assertNotNull("Version is null", version);
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), symbolicName, version);
        Assert.assertNotNull(message, bundle);
    }

    /**
     * Asserts that Bundle with bundleId is unavailable in OS. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param bundleId bundle id
     */
    public static void assertBundleUnavailable(long bundleId) {
        assertBundleUnavailable(null, bundleId);
    }

    /**
     * Asserts that Bundle with bundleId is unavailable in OS. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message  message
     * @param bundleId bundle id
     */
    public static void assertBundleUnavailable(String message, long bundleId) {
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), bundleId);
        Assert.assertNull(message, bundle);
    }

    /**
     * Asserts that Bundle with symbolic name is unavailable in OS. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param symbolicName symbolic name
     */
    public static void assertBundleUnavailable(String symbolicName) {
        assertBundleUnavailable(null, symbolicName);
    }

    /**
     * Asserts that Bundle with symbolic name is unavailable in OS. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message      message
     * @param symbolicName symbolic name
     */
    public static void assertBundleUnavailable(String message, String symbolicName) {
        Assert.assertNotNull("SymbolicName is null", symbolicName);
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), symbolicName);
        Assert.assertNull(message, bundle);
    }

    /**
     * Asserts that Bundle with symbolic name and version is unavailable in OS. If it not as expected
     * {@link AssertionError} without a message is thrown
     *
     * @param symbolicName symbolic name
     * @param version      version
     */
    public static void assertBundleUnavailable(String symbolicName, Version version) {
        assertBundleUnavailable(null, symbolicName, version);
    }

    /**
     * Asserts that Bundle with symbolic name and version is unavailable in OS. If it not as expected
     * {@link AssertionError} is thrown with the given message
     *
     * @param message      message
     * @param symbolicName symbolic name
     * @param version      version
     */
    public static void assertBundleUnavailable(String message, String symbolicName, Version version) {
        Assert.assertNotNull("SymbolicName is null", symbolicName);
        Assert.assertNotNull("Version is null", version);
        Bundle bundle = BundleUtils.findBundle(getBundleContext(), symbolicName, version);
        Assert.assertNull(message, bundle);
    }

    // todo: add assertBundleEventXXX
}
