/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.framework.Bundle;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgilab.bundles.jmx.OsgiVisitor;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import java.io.IOException;

/**
 * PackageStateMBean Implementation
 * 
 * @author dmytro.pishchukhin
 */
public class PackageState extends AbstractMBean implements PackageStateMBean {
    public PackageState(OsgiVisitor visitor) throws NotCompliantMBeanException {
        super(PackageStateMBean.class, visitor);
    }

    public long[] getExportingBundles(String packageName, String version) throws IOException {
        return new long[0];  // todo
    }

    public long[] getImportingBundles(String packageName, String version, long exportingBundle) throws IOException {
        return new long[0];  // todo
    }

    public TabularData listPackages() throws IOException {
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        TabularDataSupport dataSupport = new TabularDataSupport(PACKAGES_TYPE);
        ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages((Bundle)null);
        // todo
        return dataSupport;
    }

    public boolean isRemovalPending(String packageName, String version, long exportingBundle) throws IOException {
        return false;  // todo
    }
}
