/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.framework;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgilab.bundles.jmx.beans.OsgiVisitor;
import org.osgilab.bundles.jmx.Utils;
import org.osgilab.bundles.jmx.beans.AbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import java.io.IOException;
import java.util.*;

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
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        Version packageVersion = new Version(version);
        ExportedPackage[] packages = Utils.findPackages(packageAdmin.getExportedPackages((Bundle) null),
                packageName, packageVersion);
        Set<Bundle> bundles = new HashSet<Bundle>();
        for (ExportedPackage aPackage : packages) {
            Bundle bundle = aPackage.getExportingBundle();
            if (bundle != null) {
                bundles.add(bundle);
            }
        }
        return Utils.getIds(bundles.toArray(new Bundle[bundles.size()]));
    }

    public long[] getImportingBundles(String packageName, String version, long exportingBundle) throws IOException {
        Bundle bundle = visitor.getBundle(exportingBundle);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + exportingBundle);
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        Version packageVersion = new Version(version);

        ExportedPackage foundPackage = Utils.findPackage(packageAdmin.getExportedPackages(bundle), packageName, packageVersion);
        if (foundPackage == null) {
            throw new IllegalArgumentException("Package name/vesion are wrong: " + packageName + ", " + version);
        }
        return Utils.getIds(foundPackage.getImportingBundles());
    }

    public TabularData listPackages() throws IOException {
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        TabularDataSupport dataSupport = new TabularDataSupport(PACKAGES_TYPE);
        ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages((Bundle) null);
        Map<String, PackageInfo> packages = new HashMap<String, PackageInfo>();
        if (exportedPackages != null) {
            for (ExportedPackage exportedPackage : exportedPackages) {
                String key = exportedPackage.getName() + ";" + exportedPackage.getVersion().toString();
                PackageInfo packageInfo = packages.get(key);
                if (packageInfo == null) {
                    packageInfo = new PackageInfo(exportedPackage.getName(), exportedPackage.getVersion(), exportedPackage.isRemovalPending());
                    packages.put(key, packageInfo);
                }
                packageInfo.exportingBundles.add(exportedPackage.getExportingBundle());
                packageInfo.importingBundles.addAll(Arrays.asList(exportedPackage.getImportingBundles()));
            }
        }
        Collection<PackageInfo> packageInfos = packages.values();
        try {
            for (PackageInfo packageInfo : packageInfos) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put(NAME, packageInfo.name);
                values.put(VERSION, packageInfo.version.toString());
                values.put(REMOVAL_PENDING, packageInfo.isRemovalPending);
                values.put(EXPORTING_BUNDLES, Utils.toLongArray(Utils.getIds(packageInfo.exportingBundles.toArray(new Bundle[packageInfo.exportingBundles.size()]))));
                values.put(IMPORTING_BUNDLES, Utils.toLongArray(Utils.getIds(packageInfo.importingBundles.toArray(new Bundle[packageInfo.importingBundles.size()]))));
                dataSupport.put(new CompositeDataSupport(PACKAGE_TYPE, values));
            }
        } catch (OpenDataException e) {
            e.printStackTrace();
        }
        return dataSupport;
    }

    public boolean isRemovalPending(String packageName, String version, long exportingBundle) throws IOException {
        Bundle bundle = visitor.getBundle(exportingBundle);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + exportingBundle);
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        Version packageVersion = new Version(version);

        ExportedPackage foundPackage = Utils.findPackage(packageAdmin.getExportedPackages(bundle), packageName, packageVersion);
        if (foundPackage == null) {
            throw new IllegalArgumentException("Package name/vesion are wrong: " + packageName + ", " + version);
        }
        return foundPackage.isRemovalPending();
    }

    private static class PackageInfo {
        Set<Bundle> exportingBundles = new HashSet<Bundle>();
        Set<Bundle> importingBundles = new HashSet<Bundle>();
        boolean isRemovalPending;
        String name;
        Version version;

        private PackageInfo(String name, Version version, boolean removalPending) {
            this.name = name;
            this.version = version;
            isRemovalPending = removalPending;
        }
    }
}
