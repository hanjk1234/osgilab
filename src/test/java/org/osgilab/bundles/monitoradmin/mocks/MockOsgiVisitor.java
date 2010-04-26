/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin.mocks;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.monitor.Monitorable;
import org.osgilab.bundles.monitoradmin.OsgiVisitor;

import java.util.*;

/**
 * @author dpishchukhin
 */
public class MockOsgiVisitor implements OsgiVisitor {
    private Map<ServiceReference, Monitorable> serviceReferences = new HashMap<ServiceReference, Monitorable>();

    private List<Event> events = new ArrayList<Event>();

    public Event[] getPostedEvents() {
        return events.toArray(new Event[events.size()]);
    }

    public void cleanPostedEvents() {
        events.clear();
    }

    public void setReferences(Map<ServiceReference, Monitorable> references) {
        serviceReferences.clear();
        serviceReferences.putAll(references);
    }

    public Monitorable getService(ServiceReference reference) {
        return serviceReferences.get(reference);
    }

    public ServiceReference[] findMonitorableReferences(String monitorableId) {
        Set<ServiceReference> references = serviceReferences.keySet();
        List<ServiceReference> result = new ArrayList<ServiceReference>();
        for (ServiceReference reference : references) {
            if (monitorableId == null || reference.getProperty(Constants.SERVICE_PID).equals(monitorableId)) {
                result.add(reference);
            }
        }
        return result.toArray(new ServiceReference[result.size()]);
    }

    public void postEvent(Event event) {
        events.add(event);
    }
}
