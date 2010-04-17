package org.osgilab.bundles.jmx.beans;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;

import java.io.InputStream;

/**
 * OSGi visitor interface. It is used to access OSGi specific services.
 * 
 * @author dpishchukhin
 */
public interface OsgiVisitor {
    Bundle getBundle(long id);

    ServiceReference getServiceReferenceById(long id);

    ServiceReference[] getAllServiceReferences();

    PackageAdmin getPackageAdmin();

    StartLevel getStartLevel();

    Bundle installBundle(String location) throws BundleException;

    Bundle installBundle(String location, InputStream stream) throws BundleException;

    org.osgi.framework.launch.Framework getFramework();

    Bundle[] getBundles();
}
