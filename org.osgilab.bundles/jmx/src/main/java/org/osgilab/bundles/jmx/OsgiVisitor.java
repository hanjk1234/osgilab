package org.osgilab.bundles.jmx;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/**
 * @author dpishchukhin
 */
public interface OsgiVisitor {
    Bundle getBundle(long id);

    ServiceReference getServiceReferenceById(long id);

    ServiceReference[] getAllServiceReferences();
}
