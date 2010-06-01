/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.utils;

import org.osgi.framework.BundleContext;

/**
 * BundleContext utility singleton class
 *
 * @author dpishchukhin
 */
public class BundleContextUtil {
    /**
     * Util class instance
     */
    private static final BundleContextUtil instance = new BundleContextUtil();

    /**
     * BundleContext value
     */
    private BundleContext bc;

    /**
     * Initialize utility class by BundleContext value
     * @param bc BundleContext value
     * @throws IllegalStateException utility class is already initialized
     */
    public static void init(BundleContext bc) {
        if (getInstance().bc != null) {
            throw new IllegalStateException("BundleContext is already initialized");
        }
        getInstance().bc = bc;
    }

    /**
     * Get class instance
     * @return instance
     */
    public static BundleContextUtil getInstance() {
        return instance;
    }

    /**
     * Get BundleContext
     * @return BundleContext value
     */
    public BundleContext getBundleContext() {
        return bc;
    }
}
