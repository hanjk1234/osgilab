package org.osgilab.bundles.jmx.beans;

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
