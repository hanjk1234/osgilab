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

package org.osgilab.tips.logs.jul2osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;

/**
 * @author dmytro.pishchukhin
 */
public class Activator extends Handler implements BundleActivator {
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

    /**
     * System propery for logs buffer size
     */
    public static final String BUFFER_SIZE_PROPERTY_NAME = "org.osgilab.tips.logs.jul2osgi.bufferSize";
    /**
     * System propery for logs level
     */
    public static final String LOGS_LEVEL_PROPERTY_NAME = "org.osgilab.tips.logs.jul2osgi.logsLevel";
    /**
     * System propery for logger names
     */
    public static final String LOGGER_NAMES_PROPERTY_NAME = "org.osgilab.tips.logs.jul2osgi.loggerNames";

    private BundleContext bc;
    private ServiceTracker logServiceTracker;

    /**
     * Logs buffer. It's used when LogService is not available
     */
    private ArrayBlockingQueue<LogRecord> logsBuffer;
    /**
     * JUL Logger names
     */
    private String[] loggerNames;

    /**
     * Lock
     */
    private final ReentrantLock queueLock = new ReentrantLock();

    public void start(BundleContext bundleContext) throws Exception {
        LOG.entering(getClass().getName(), "start");
        try {
            bc = bundleContext;

            logServiceTracker = new ServiceTracker(bundleContext, LogService.class.getName(), new LogServiceCustomizer());
            logServiceTracker.open();

            handleSystemProperties();

            registerLogsHandles();
        } finally {
            LOG.exiting(getClass().getName(), "start");
        }
    }

    private void handleSystemProperties() {
        // init logs buffer
        String bufferSizeProperty = bc.getProperty(BUFFER_SIZE_PROPERTY_NAME);
        if (bufferSizeProperty != null) {
            try {
                int logsBufferSize = Integer.parseInt(bufferSizeProperty);
                if (logsBufferSize > 0) {
                    logsBuffer = new ArrayBlockingQueue<LogRecord>(logsBufferSize);
                    LOG.log(Level.INFO, "Buffer Size property is set: " + bufferSizeProperty);
                } else {
                    LOG.log(Level.INFO, "Buffer Size property is below 0. Buffer is disabled.");
                }
            } catch (NumberFormatException e) {
                LOG.log(Level.WARNING, "Buffer Size property has wrong format: " + bufferSizeProperty +
                        ". Buffer is disabled.", e);
            }
        } else {
            LOG.log(Level.WARNING, "Buffer Size property is not set. Buffer is disabled.");
        }

        // init level property
        String levelProperty = bc.getProperty(LOGS_LEVEL_PROPERTY_NAME);
        if (levelProperty != null) {
            try {
                setLevel(Level.parse(levelProperty));
                LOG.log(Level.INFO, "Level property is set: " + levelProperty);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Level property has wrong format: " + levelProperty +
                        ". Use default: " + Level.ALL, e);
                setLevel(Level.ALL);
            }
        } else {
            LOG.log(Level.INFO, "Level property is not set. Use default: " + Level.ALL);
            setLevel(Level.ALL);
        }

        // init loggers
        String namesProperty = bc.getProperty(LOGGER_NAMES_PROPERTY_NAME);
        if (namesProperty != null) {
            try {
                loggerNames = namesProperty.split(";");
                LOG.log(Level.INFO, "Logger names property is set: " + namesProperty);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Logger names property has wrong format: " + namesProperty +
                        ". Use Root Logger.", e);
            }
        } else {
            LOG.log(Level.INFO, "Logger names property is not set. Use Root Logger.");
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        LOG.entering(getClass().getName(), "stop");
        try {
            unregisterLogsHandles();

            logsBuffer.clear();
            logsBuffer = null;

            logServiceTracker.close();
            logServiceTracker = null;

            bc = null;
        } finally {
            LOG.exiting(getClass().getName(), "stop");
        }
    }

    private void registerLogsHandles() {
        // if no logger names defined - link to Root Logger
        if (loggerNames == null || loggerNames.length == 0) {
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            rootLogger.addHandler(this);
        } else {
            for (String loggerName : loggerNames) {
                Logger logger = LogManager.getLogManager().getLogger(loggerName);
                logger.addHandler(this);
            }
        }
    }

    private void unregisterLogsHandles() {
        if (loggerNames == null || loggerNames.length == 0) {
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            rootLogger.removeHandler(this);
        } else {
            for (String loggerName : loggerNames) {
                Logger logger = LogManager.getLogManager().getLogger(loggerName);
                logger.removeHandler(this);
            }
        }
    }

    @Override
    public void publish(LogRecord record) {
        // if record level is below than defined - ignore the record
        if (!isLoggable(record)) {
            return;
        }

        LogService logService = (LogService) logServiceTracker.getService();
        if (logService != null) {
            forwardToLogService(logService, record);
        } else {
            // if log service is unavailable and buffer is enabled
            if (logsBuffer != null) {
                queueLock.lock();
                try {
                    if (logsBuffer.remainingCapacity() == 0) {
                        // shift elements removing the oldest out of queue
                        logsBuffer.poll();
                    }
                    logsBuffer.offer(record);
                } finally {
                    queueLock.unlock();
                }
            }
        }
    }

    private void forwardToLogService(LogService logService, LogRecord logRecord) {
        logService.log(mapLogRecordLevel(logRecord.getLevel()),
                logRecord.getMessage(), logRecord.getThrown());
    }

    private int mapLogRecordLevel(Level level) {
        if (Level.FINEST == level || Level.FINER == level || Level.FINE == level) {
            return LogService.LOG_DEBUG;
        } else if (Level.INFO == level || Level.CONFIG == level) {
            return LogService.LOG_INFO;
        } else if (Level.WARNING == level) {
            return LogService.LOG_WARNING;
        } else if (Level.SEVERE == level) {
            return LogService.LOG_ERROR;
        }
        return LogService.LOG_INFO;
    }

    @Override
    public void flush() {
        // do nothing
    }

    @Override
    public void close() throws SecurityException {
        // do nothing
    }

    private class LogServiceCustomizer implements ServiceTrackerCustomizer {
        public Object addingService(ServiceReference serviceReference) {
            LogService logService = (LogService) bc.getService(serviceReference);

            // publish logs from buffer if available
            if (logsBuffer != null) {
                ArrayList<LogRecord> logRecords = new ArrayList<LogRecord>();
                logsBuffer.drainTo(logRecords);
                for (LogRecord logRecord : logRecords) {
                    forwardToLogService(logService, logRecord);
                }
            }

            return logService;
        }

        public void modifiedService(ServiceReference serviceReference, Object o) {
            // do nothing
        }

        public void removedService(ServiceReference serviceReference, Object o) {
            // do nothing
        }
    }
}
