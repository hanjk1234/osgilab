/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.utils;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * OSGi Bundles utilities class
 *
 * @author dpishchukhin
 */
public class BundleUtils {
    /**
     * Utility class. Only static methods are available.
     */
    private BundleUtils() {
    }

    /**
     * Find bundle by ID
     *
     * @param bc       BundleContext
     * @param bundleId bundle id
     * @return Bundle instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> is <code>null</code>
     */
    public static Bundle findBundle(BundleContext bc, long bundleId) {
        return bc.getBundle(bundleId);
    }

    /**
     * Find bundle by SymbolicName
     *
     * @param bc           BundleContext
     * @param symbolicName symbolicName
     * @return Bundle instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> or <code>symbolicName</code> are <code>null</code>
     */
    public static Bundle findBundle(BundleContext bc, String symbolicName) {
        return findBundle(bc, symbolicName, null);
    }

    /**
     * Find bundle by SymbolicName and Version
     *
     * @param bc           BundleContext
     * @param symbolicName symbolicName
     * @param version      version
     * @return Bundle instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> or <code>symbolicName</code> are <code>null</code>
     */
    public static Bundle findBundle(BundleContext bc, String symbolicName, Version version) {
        PackageAdmin packageAdmin = ServiceUtils.getService(bc, PackageAdmin.class);
        if (packageAdmin != null) {
            Bundle[] bundles = packageAdmin.getBundles(symbolicName, version != null ? version.toString() : null);
            if (bundles != null && bundles.length > 0) {
                return bundles[0];
            }
        }
        return null;
    }
}
