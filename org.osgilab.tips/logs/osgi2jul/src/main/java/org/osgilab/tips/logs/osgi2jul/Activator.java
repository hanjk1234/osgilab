/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgilab.tips.logs.osgi2jul;

import org.osgi.framework.*;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Class that redirects OSGi LogService entities to Java util logger.
 * It registers LogListener in LogReaderService, map OSGi LogEntity objects to
 * JUL LogRecord objects and publish them with JUL Logger
 * <p/>
 * To customize logger name use system property
 * {@link org.osgilab.tips.logs.osgi2jul.Activator#LOGGER_NAME_PROPERTY_NAME}
 *
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator, LogListener {
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

    /**
     * System propery for logger name
     */
    public static final String LOGGER_NAME_PROPERTY_NAME = "org.osgilab.tips.logs.osgi2jul.loggerName";

    private ServiceTracker logReaderServiceTracker;
    private BundleContext bc;
    private String loggerName;

    public void start(BundleContext bundleContext) throws Exception {
        LOG.entering(getClass().getName(), "start");
        try {
            bc = bundleContext;

            loggerName = bc.getProperty(LOGGER_NAME_PROPERTY_NAME);
            // register ServiceTracker to track LogReaderService
            logReaderServiceTracker = new ServiceTracker(bundleContext,
                    LogReaderService.class.getName(), new LogReaderServiceCustomizer());
            logReaderServiceTracker.open();
        } finally {
            LOG.exiting(getClass().getName(), "start");
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        LOG.entering(getClass().getName(), "stop");
        try {
            logReaderServiceTracker.close();
            logReaderServiceTracker = null;
            bc = null;
        } finally {
            LOG.exiting(getClass().getName(), "stop");
        }
    }

    public void logged(LogEntry logEntry) {
        LOG.log(mapLogEntry(logEntry));
    }

    /**
     * Map OSGi LogEntry to JUL LogRecord
     *
     * @param logEntry OSGi LogEntry
     * @return JUL LogRecord
     */
    private LogRecord mapLogEntry(LogEntry logEntry) {
        LogRecord logRecord = new LogRecord(mapLogEntryLevel(logEntry.getLevel()), composeLogRecordMessage(logEntry));
        logRecord.setMillis(logEntry.getTime());
        logRecord.setThrown(logEntry.getException());
        logRecord.setLoggerName(loggerName == null ? LOG.getName() : loggerName);
        return logRecord;
    }

    /**
     * Compose JUL LogRecord message based on OSGi LogEntry properties
     *
     * @param logEntry OSGi LogEntry
     * @return message
     */
    private String composeLogRecordMessage(LogEntry logEntry) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(logEntry.getMessage());
        Bundle bundle = logEntry.getBundle();
        if (bundle != null) {
            messageBuilder.append("\nBundle [");
            messageBuilder.append(bundle.getBundleId());
            messageBuilder.append("] ");
            messageBuilder.append(bundle.getSymbolicName());
            messageBuilder.append(" (");
            messageBuilder.append(bundle.getVersion().toString());
            messageBuilder.append(")");
        }
        ServiceReference reference = logEntry.getServiceReference();
        if (reference != null) {
            messageBuilder.append("\nServiceReference [");
            messageBuilder.append(reference.getProperty(Constants.SERVICE_ID));
            messageBuilder.append("] PID=");
            messageBuilder.append(reference.getProperty(Constants.SERVICE_PID));
        }
        return messageBuilder.toString();
    }

    /**
     * Map OSGi LogEntry level to JUL LogRecord level
     *
     * @param level OSGi LogEntry level
     * @return JUL LogRecord level
     */
    private Level mapLogEntryLevel(int level) {
        switch (level) {
            case LogService.LOG_DEBUG:
                return Level.FINE;
            case LogService.LOG_INFO:
                return Level.INFO;
            case LogService.LOG_WARNING:
                return Level.WARNING;
            case LogService.LOG_ERROR:
                return Level.SEVERE;
            default:
                return Level.INFO;
        }
    }

    private class LogReaderServiceCustomizer implements ServiceTrackerCustomizer {
        public Object addingService(ServiceReference serviceReference) {
            LogReaderService logReaderService = (LogReaderService) bc.getService(serviceReference);
            logReaderService.addLogListener(Activator.this);
            return logReaderService;
        }

        public void modifiedService(ServiceReference serviceReference, Object o) {
            // ignore
        }

        public void removedService(ServiceReference serviceReference, Object o) {
            LogReaderService logReaderService = (LogReaderService) o;
            logReaderService.removeLogListener(Activator.this);
        }
    }
}
