/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.utils;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

/**
 * Bundle utility class
 *
 * @author dpishchukhin
 */
public class BundleUtils {
    /**
     * Find bundle by ID
     * @param bc BundleContext
     * @param bundleId bundle id
     * @return Bundle instance or <code>null</code>
     * @throws NullPointerException BundleContext is <code>null</code>
     */
    public static Bundle findBundle(BundleContext bc, long bundleId) {
        return bc.getBundle(bundleId);
    }

    /**
     * Find bundle by SymbolicName
     * @param bc BundleContext
     * @param symbolicName symbolicName
     * @return Bundle instance or <code>null</code>
     * @throws NullPointerException BundleContext or symbolicName are <code>null</code>
     */
    public static Bundle findBundle(BundleContext bc, String symbolicName) {
        return null;
    }

    /**
     * Find bundle by SymbolicName and Version
     * @param bc BundleContext
     * @param symbolicName symbolicName
     * @param version version
     * @return Bundle instance or <code>null</code>
     * @throws NullPointerException BundleContext, symbolicName or version are <code>null</code>
     */
    public static Bundle findBundle(BundleContext bc, String symbolicName, Version version) {
        return null;
    }
}
