/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.monitor.Monitorable;

/**
 * OSGi visitor interface. Is used to decouple BundleContext
 *
 * @author dpishchukhin
 * @see org.osgi.framework.BundleContext
 */
public interface OsgiVisitor {
    /**
     * Get <code>Monitorable</code> service by <code>ServiceReference</code>
     * @param reference <code>ServiceReference</code>
     * @return <code>Monitorable</code> service
     */
    Monitorable getService(ServiceReference reference);

    /**
     * Get list of <code>Monitorable</code> <code>ServiceReference</code> by monitorableId
     * @param monitorableId monitorable Id
     * @return Array of <code>ServiceReference</code>s or <code>null</code>
     */
    ServiceReference[] findMonitorableReferences(String monitorableId);

    /**
     * Post <code>Event</code> via <code>EventAdmin</code>
     * @param event event
     */
    void postEvent(Event event);
}
