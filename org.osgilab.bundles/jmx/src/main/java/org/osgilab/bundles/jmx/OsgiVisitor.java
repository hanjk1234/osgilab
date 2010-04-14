package org.osgilab.bundles.jmx;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;

/**
 * @author dpishchukhin
 */
public interface OsgiVisitor {
    Bundle getBundle(long id);

    ServiceReference getServiceReferenceById(long id);

    ServiceReference[] getAllServiceReferences();

    PackageAdmin getPackageAdmin();

    StartLevel getStartLevel();
}
