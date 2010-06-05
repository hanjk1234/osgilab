/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.testing.commons.utils;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

import java.util.concurrent.TimeUnit;

/**
 * OSGi Services utilities class
 *
 * @author dmytro.pishchukhin
 */
public class ServiceUtils {
    /**
     * Utility class. Only static methods are available.
     */
    private ServiceUtils() {
    }

    /**
     * Get ServiceReference by filter
     *
     * @param bc     BundleContext
     * @param filter filter
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, Filter filter) {
        ServiceTracker tracker = new ServiceTracker(bc, filter, null);
        tracker.open();
        try {
            return tracker.getServiceReference();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by filter with timeout.
     *
     * @param bc      BundleContext
     * @param filter  filter
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, Filter filter, long timeout) {
        return getServiceReference(bc, filter, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get ServiceReference by filter with timeout.
     *
     * @param bc       BundleContext
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, Filter filter, long timeout, TimeUnit timeUnit) {
        return null; // todo
    }

    /**
     * Get ServiceReference by filter
     *
     * @param bc     BundleContext
     * @param filter filter
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws InvalidSyntaxException If <code>filter</code> contains an
     *                                invalid filter string that cannot be parsed
     * @throws NullPointerException   If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, String filter) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FrameworkUtil.createFilter(filter), null);
        tracker.open();
        try {
            return tracker.getServiceReference();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by filter with timeout.
     *
     * @param bc      BundleContext
     * @param filter  filter
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, String filter, long timeout)
            throws InvalidSyntaxException {
        return getServiceReference(bc, filter, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get ServiceReference by filter with timeout.
     *
     * @param bc       BundleContext
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, String filter, long timeout, TimeUnit timeUnit)
            throws InvalidSyntaxException {
        return getServiceReference(bc, FrameworkUtil.createFilter(filter), timeout, timeUnit);
    }

    /**
     * Get ServiceReference by class
     *
     * @param bc    BundleContext
     * @param clazz Class
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> or <code>clazz</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, Class clazz) {
        ServiceTracker tracker = new ServiceTracker(bc, clazz.getName(), null);
        tracker.open();
        try {
            return tracker.getServiceReference();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get ServiceReference by class with timeout.
     *
     * @param bc      BundleContext
     * @param clazz   Class
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, Class clazz, long timeout) {
        return getServiceReference(bc, clazz, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get ServiceReference by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return ServiceReference instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     */
    public static ServiceReference getServiceReference(BundleContext bc, Class clazz, long timeout, TimeUnit timeUnit) {
        return null; // todo
    }

    /**
     * Get service instance by filter
     *
     * @param bc     BundleContext
     * @param filter filter
     * @return service instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static Object getService(BundleContext bc, Filter filter) {
        ServiceTracker tracker = new ServiceTracker(bc, filter, null);
        tracker.open();
        try {
            return tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by filter with timeout.
     *
     * @param bc      BundleContext
     * @param filter  filter
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static Object getService(BundleContext bc, Filter filter, long timeout) {
        return getService(bc, filter, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by filter with timeout.
     *
     * @param bc       BundleContext
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     */
    public static Object getService(BundleContext bc, Filter filter, long timeout, TimeUnit timeUnit) {
        ServiceTracker tracker = new ServiceTracker(bc, filter, null);
        tracker.open();
        try {
            return tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by filter
     *
     * @param bc     BundleContext
     * @param filter filter
     * @return service instance or <code>null</code>
     *
     * @throws InvalidSyntaxException If <code>filter</code> contains an
     *                                invalid filter string that cannot be parsed
     * @throws NullPointerException   If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static Object getService(BundleContext bc, String filter) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FrameworkUtil.createFilter(filter), null);
        tracker.open();
        try {
            return tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by filter with timeout.
     *
     * @param bc      BundleContext
     * @param filter  filter
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return service instance or <code>null</code>
     *
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>filter</code> are <code>null</code>
     */
    public static Object getService(BundleContext bc, String filter, long timeout) throws InvalidSyntaxException {
        return getService(bc, filter, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by filter with timeout.
     *
     * @param bc       BundleContext
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     *
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>filter</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     */
    public static Object getService(BundleContext bc, String filter, long timeout, TimeUnit timeUnit) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FrameworkUtil.createFilter(filter), null);
        tracker.open();
        try {
            return tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class
     *
     * @param bc    BundleContext
     * @param clazz Class
     * @return service instance instance or <code>null</code>
     *
     * @throws NullPointerException If <code>bc</code> or <code>clazz</code> are <code>null</code>
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz) {
        ServiceTracker tracker = new ServiceTracker(bc, clazz.getName(), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc      BundleContext
     * @param clazz   Class
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>clazz</code> are <code>null</code>
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, long timeout) {
        return getService(bc, clazz, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>clazz</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, long timeout, TimeUnit timeUnit) {
        ServiceTracker tracker = new ServiceTracker(bc, clazz.getName(), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class
     *
     * @param bc     BundleContext
     * @param clazz  Class
     * @param filter filter
     * @return service instance instance or <code>null</code>
     *
     * @throws NullPointerException   If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException If <code>filter</code> contains an
     *                                invalid filter string that cannot be parsed
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, String filter) throws InvalidSyntaxException {
        return getService(bc, clazz, FrameworkUtil.createFilter(filter));
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc      BundleContext
     * @param clazz   Class
     * @param filter  filter
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, String filter, long timeout) throws InvalidSyntaxException {
        return getService(bc, clazz, filter, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>clazz</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, String filter, long timeout, TimeUnit timeUnit) throws InvalidSyntaxException {
        return getService(bc, clazz, FrameworkUtil.createFilter(filter), timeout, timeUnit);
    }

    /**
     * Get service instance by class
     *
     * @param bc     BundleContext
     * @param clazz  Class
     * @param filter filter
     * @return service instance instance or <code>null</code>
     *
     * @throws NullPointerException   If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException If <code>filter</code> contains an
     *                                invalid filter string that cannot be parsed
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, Filter filter) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FilterUtils.createClassFilter(clazz, filter), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.getService();
        } finally {
            tracker.close();
        }
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc      BundleContext
     * @param clazz   Class
     * @param filter  filter
     * @param timeout time interval in milliseconds to wait. If zero, the method will wait indefinately.
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code> or <code>clazz</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, Filter filter, long timeout) throws InvalidSyntaxException {
        return getService(bc, clazz, filter, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Get service instance by class with timeout.
     *
     * @param bc       BundleContext
     * @param clazz    Class
     * @param filter   filter
     * @param timeout  time interval to wait. If zero, the method will wait indefinately.
     * @param timeUnit time unit for the time interval
     * @return service instance or <code>null</code>
     *
     * @throws IllegalArgumentException If the value of timeout is negative
     * @throws NullPointerException     If <code>bc</code>, <code>clazz</code> or
     *                                  <code>timeUnit</code> are <code>null</code>
     * @throws InvalidSyntaxException   If <code>filter</code> contains an
     *                                  invalid filter string that cannot be parsed
     */
    public static <T> T getService(BundleContext bc, Class<T> clazz, Filter filter, long timeout, TimeUnit timeUnit) throws InvalidSyntaxException {
        ServiceTracker tracker = new ServiceTracker(bc, FilterUtils.createClassFilter(clazz, filter), null);
        tracker.open();
        try {
            //noinspection unchecked
            return (T) tracker.waitForService(timeUnit.toMillis(timeout));
        } catch (InterruptedException e) {
            return null;
        } finally {
            tracker.close();
        }
    }
}
