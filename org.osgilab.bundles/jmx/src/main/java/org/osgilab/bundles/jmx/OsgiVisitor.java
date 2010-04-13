package org.osgilab.bundles.jmx;

import org.osgi.framework.Bundle;

/**
 * @author dpishchukhin
 */
public interface OsgiVisitor {
    Bundle getBundle(long id);
}
