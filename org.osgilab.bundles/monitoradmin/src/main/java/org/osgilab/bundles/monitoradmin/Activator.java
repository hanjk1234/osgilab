/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.osgi.framework.*;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.log.LogService;
import org.osgi.service.monitor.MonitorAdmin;
import org.osgi.service.monitor.MonitorListener;
import org.osgi.service.monitor.Monitorable;
import org.osgi.util.tracker.ServiceTracker;
import org.osgilab.bundles.monitoradmin.util.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Monitor Admin activator
 *
 * @author dmytro.pishchukhin
 * @see org.osgi.framework.BundleActivator
 */
public class Activator implements BundleActivator, OsgiVisitor, LogVisitor {
    // default logger
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

    // monitor admin instance
    private MonitorAdminImpl monitorAdmin;

    private BundleContext bc;
    // monitor admin service registration
    private ServiceRegistration serviceRegistration;

    // EventAdmin service tracker
    private ServiceTracker eventAdminTracker;
    // LogService service tracker
    private ServiceTracker logServiceTracker;


    public void start(BundleContext bundleContext) throws Exception {
        bc = bundleContext;

        // init LogService tracker
        logServiceTracker = new ServiceTracker(bc, LogService.class.getName(), null);
        logServiceTracker.open();

        // init EventAdmin tracker
        eventAdminTracker = new ServiceTracker(bc, EventAdmin.class.getName(), null);
        eventAdminTracker.open();

        monitorAdmin = new MonitorAdminImpl(this, this);

        // register MonitorAdmin implementation under MonitorAdmin and MonitorListener interfaces
        serviceRegistration = bundleContext.registerService(new String[]{MonitorAdmin.class.getName(),
                MonitorListener.class.getName()}, monitorAdmin, null);

        info("MonitorAdmin started", null);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        // unregister MonitorAdmin service
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            serviceRegistration = null;
        }

        // uninit monitor admin instance
        if (monitorAdmin != null) {
            // cancel started jobs
            monitorAdmin.cancelJobs();
            monitorAdmin = null;
        }

        if (eventAdminTracker != null) {
            eventAdminTracker.close();
            eventAdminTracker = null;
        }

        info("MonitorAdmin stoppped", null);

        if (logServiceTracker != null) {
            logServiceTracker.close();
            logServiceTracker = null;
        }

        bc = null;
    }

    /**
     * Publish DEBUG message. If <code>LogService</code> in unavailable message is published to default JUL logger
     *
     * @param message   message
     * @param throwable exception
     */
    public void debug(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_DEBUG, message, throwable);
        } else {
            LOG.log(Level.FINE, message, throwable);
        }
    }

    /**
     * Publish INFO message. If <code>LogService</code> in unavailable message is published to default JUL logger
     *
     * @param message   message
     * @param throwable exception
     */
    public void info(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_INFO, message, throwable);
        } else {
            LOG.log(Level.INFO, message, throwable);
        }
    }

    /**
     * Publish WARNING message. If <code>LogService</code> in unavailable message is published to default JUL logger
     *
     * @param message   message
     * @param throwable exception
     */
    public void warning(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_WARNING, message, throwable);
        } else {
            LOG.log(Level.WARNING, message, throwable);
        }
    }

    /**
     * Publish ERROR message. If <code>LogService</code> in unavailable message is published to default JUL logger
     *
     * @param message   message
     * @param throwable exception
     */
    public void error(String message, Throwable throwable) {
        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            logService.log(LogService.LOG_ERROR, message, throwable);
        } else {
            LOG.log(Level.SEVERE, message, throwable);
        }
    }

    public Monitorable getService(ServiceReference reference) {
        return (Monitorable) bc.getService(reference);
    }

    public ServiceReference[] findMonitorableReferences(String monitorableId) {
        String filter = null;
        if (monitorableId != null) {
            filter = Utils.createServicePidFilter(monitorableId);
        }
        try {
            return bc.getServiceReferences(Monitorable.class.getName(), filter);
        } catch (InvalidSyntaxException e) {
            warning("Unable to find Monitorable References", e);
            return null;
        }
    }

    public void postEvent(Event event) {
        EventAdmin eventAdmin = (EventAdmin) eventAdminTracker.getService();
        if (eventAdmin != null) {
            eventAdmin.postEvent(event);
        } else {
            warning("EventAdmin is unavailable", null);
        }
    }
}
