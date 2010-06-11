/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

/**
 * Logger interface. Is used to decouple loggers.
 *
 * @author dpishchukhin
 */
public interface LogVisitor {
    /**
     * Publish DEBUG message
     *
     * @param message   message
     * @param throwable exception
     */
    void debug(String message, Throwable throwable);

    /**
     * Publish INFO message
     *
     * @param message   message
     * @param throwable exception
     */
    void info(String message, Throwable throwable);

    /**
     * Publish WARNING message
     *
     * @param message   message
     * @param throwable exception
     */
    void warning(String message, Throwable throwable);

    /**
     * Publish ERROR message
     *
     * @param message   message
     * @param throwable exception
     */
    void error(String message, Throwable throwable);
}
