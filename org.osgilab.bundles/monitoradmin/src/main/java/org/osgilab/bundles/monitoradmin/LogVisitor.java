/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

/**
 * Logger interface
 *
 * @author dpishchukhin
 */
public interface LogVisitor {
    void debug(String message, Throwable throwable);

    void info(String message, Throwable throwable);

    void warning(String message, Throwable throwable);

    void error(String message, Throwable throwable);
}
