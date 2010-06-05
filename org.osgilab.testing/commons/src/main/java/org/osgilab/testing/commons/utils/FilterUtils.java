/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.utils;

import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;

/**
 * OSGi Filter utilities class
 *
 * @author dmytro.pishchukhin
 * @version 1.0
 * @see org.osgi.framework.Filter
 * @see org.osgi.framework.FrameworkUtil#createFilter(String)
 * @see org.osgi.framework.BundleContext#createFilter(String)
 */
public class FilterUtils {
    /**
     * Equals filter template
     */
    private static final String EQUALS_FILTER_TEMPLATE = "(%s=%s)";
    /**
     * Approximate filter template
     */
    private static final String APPROX_FILTER_TEMPLATE = "(%s~=%s)";
    /**
     * Greater equals filter template
     */
    private static final String GE_FILTER_TEMPLATE = "(%s>=%s)";
    /**
     * Less equals filter template
     */
    private static final String LE_FILTER_TEMPLATE = "(%s<=%s)";
    /**
     * Present filter template
     */
    private static final String PRESENT_FILTER_TEMPLATE = "(%s=*)";

    /**
     * NOT filter template
     */
    private static final String NOT_TEMPLATE = "(!%s)";
    /**
     * AND filter template
     */
    private static final String AND_TEMPLATE = "(&%s%s)";
    /**
     * OR filter template
     */
    private static final String OR_TEMPLATE = "(|%s%s)";

    /**
     * Utility class. Only static methods are available.
     */
    private FilterUtils() {
    }

    /**
     * Create AND filter for service class and custom filter
     *
     * @param clazz  service class
     * @param filter custom filter
     * @return new AND filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>clazz</code> or <code>filter</code> are <code>null</code>
     * @see Constants#OBJECTCLASS
     */
    public static Filter createClassFilter(Class clazz, Filter filter) throws InvalidSyntaxException {
        return createAndFilter(createClassFilter(clazz), filter);
    }

    /**
     * Create AND filter for service class and custom filter
     *
     * @param clazz  service class
     * @param filter custom filter
     * @return new AND filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>clazz</code> or <code>filter</code> are <code>null</code>
     * @see Constants#OBJECTCLASS
     */
    public static Filter createClassFilter(Class clazz, String filter) throws InvalidSyntaxException {
        return createAndFilter(createClassFilter(clazz), filter);
    }

    /**
     * Create filter for service class
     *
     * @param clazz service class
     * @return new AND filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>clazz</code> is <code>null</code>
     * @see Constants#OBJECTCLASS
     */
    public static Filter createClassFilter(Class clazz) throws InvalidSyntaxException {
        return createEqualsFilter(Constants.OBJECTCLASS, clazz.getName());
    }

    /**
     * Create AND filter for two filters
     *
     * @param filter1 filter 1
     * @param filter2 filter 2
     * @return new AND filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter1</code> or <code>filter2</code> are <code>null</code>
     */
    public static Filter createAndFilter(String filter1, String filter2) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(AND_TEMPLATE, filter1, filter2));
    }

    /**
     * Create AND filter for two filters
     *
     * @param filter1 filter 1
     * @param filter2 filter 2
     * @return new AND filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter1</code> or <code>filter2</code> are <code>null</code>
     */
    public static Filter createAndFilter(Filter filter1, String filter2) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(AND_TEMPLATE, filter1, filter2));
    }

    /**
     * Create AND filter for two filters
     *
     * @param filter1 filter 1
     * @param filter2 filter 2
     * @return new AND filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter1</code> or <code>filter2</code> are <code>null</code>
     */
    public static Filter createAndFilter(Filter filter1, Filter filter2) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(AND_TEMPLATE, filter1, filter2));
    }

    /**
     * Create OR filter for two filters
     *
     * @param filter1 filter 1
     * @param filter2 filter 2
     * @return new OR filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter1</code> or <code>filter2</code> are <code>null</code>
     */
    public static Filter createOrFilter(String filter1, String filter2) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(OR_TEMPLATE, filter1, filter2));
    }

    /**
     * Create OR filter for two filters
     *
     * @param filter1 filter 1
     * @param filter2 filter 2
     * @return new OR filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter1</code> or <code>filter2</code> are <code>null</code>
     */
    public static Filter createOrFilter(Filter filter1, String filter2) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(OR_TEMPLATE, filter1, filter2));
    }

    /**
     * Create OR filter for two filters
     *
     * @param filter1 filter 1
     * @param filter2 filter 2
     * @return new OR filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter1</code> or <code>filter2</code> are <code>null</code>
     */
    public static Filter createOrFilter(Filter filter1, Filter filter2) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(OR_TEMPLATE, filter1, filter2));
    }

    /**
     * Create NOT filter
     *
     * @param filter filter
     * @return new NOT filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter</code> is <code>null</code>
     */
    public static Filter createNotFilter(String filter) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(NOT_TEMPLATE, filter));
    }

    /**
     * Create NOT filter
     *
     * @param filter filter
     * @return new NOT filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>filter</code> is <code>null</code>
     */
    public static Filter createNotFilter(Filter filter) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(NOT_TEMPLATE, filter));
    }

    /**
     * Create EQUALS filter
     *
     * @param key   key
     * @param value value. To create filter value.toString() is used.
     * @return new EQUALS filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>key</code> or <code>value</code> are <code>null</code>
     */
    public static Filter createEqualsFilter(String key, Object value) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(EQUALS_FILTER_TEMPLATE, key, value));
    }

    /**
     * Create APPROX filter
     *
     * @param key   key
     * @param value value. To create filter value.toString() is used.
     * @return new APPROX filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>key</code> or <code>value</code> are <code>null</code>
     */
    public static Filter createApproximateFilter(String key, Object value) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(APPROX_FILTER_TEMPLATE, key, value));
    }

    /**
     * Create GREATER-EQUALS filter
     *
     * @param key   key
     * @param value value. To create filter value.toString() is used.
     * @return new GREATER-EQUALS filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>key</code> or <code>value</code> are <code>null</code>
     */
    public static Filter createGreaterEqualsFilter(String key, Object value) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(GE_FILTER_TEMPLATE, key, value));
    }

    /**
     * Create LESS-EQUALS filter
     *
     * @param key   key
     * @param value value. To create filter value.toString() is used.
     * @return new LESS-EQUALS filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>key</code> or <code>value</code> are <code>null</code>
     */
    public static Filter createLessEqualsFilter(String key, Object value) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(LE_FILTER_TEMPLATE, key, value));
    }

    /**
     * Create PRESENT filter
     *
     * @param key key
     * @return new PRESENT filter
     *
     * @throws InvalidSyntaxException If it is unable to create filter
     * @throws NullPointerException   If <code>key</code> is <code>null</code>
     */
    public static Filter createPresentFilter(String key) throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(String.format(PRESENT_FILTER_TEMPLATE, key));
    }
}
