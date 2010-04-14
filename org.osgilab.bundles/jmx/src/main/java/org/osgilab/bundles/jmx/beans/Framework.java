/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.osgilab.bundles.jmx.OsgiVisitor;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import java.io.IOException;
import java.net.URL;

/**
 * FrameworkMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class Framework extends AbstractMBean implements FrameworkMBean {
    public Framework(OsgiVisitor visitor) throws NotCompliantMBeanException {
        super(FrameworkMBean.class, visitor);
    }

    public int getFrameworkStartLevel() throws IOException {
        StartLevel startLevel = visitor.getStartLevel();
        if (startLevel == null) {
            throw new IOException("StartLevel is not available");
        }
        return startLevel.getStartLevel();
    }

    public int getInitialBundleStartLevel() throws IOException {
        StartLevel startLevel = visitor.getStartLevel();
        if (startLevel == null) {
            throw new IOException("StartLevel is not available");
        }
        return startLevel.getInitialBundleStartLevel();
    }

    public long installBundle(String location) throws IOException {
        try {
            Bundle bundle = visitor.installBundle(location);
            return bundle.getBundleId();
        } catch (Exception e) {
            throw new IOException("Unable to install bundle: " + location, e);
        }
    }

    public long installBundleFromURL(String location, String url) throws IOException {
        try {
            Bundle bundle = visitor.installBundle(location, new URL(url).openStream());
            return bundle.getBundleId();
        } catch (Exception e) {
            throw new IOException("Unable to install bundle: " + location + ", " + url, e);
        }
    }

    public CompositeData installBundles(String[] strings) throws IOException {
        return null;  // todo
    }

    public CompositeData installBundlesFromURL(String[] strings, String[] strings1) throws IOException {
        return null;  // todo
    }

    public void refreshBundle(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        packageAdmin.refreshPackages(new Bundle[]{bundle});
    }

    public void refreshBundles(long[] bundleIdentifiers) throws IOException {
        if (bundleIdentifiers == null) {
            throw new IllegalArgumentException("Bundle IDs are null");
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        Bundle[] bundles = new Bundle[bundleIdentifiers.length];
        for (int i = 0; i < bundleIdentifiers.length; i++) {
            long bundleIdentifier = bundleIdentifiers[i];
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            bundles[i] = bundle;
        }
        packageAdmin.refreshPackages(bundles);
    }

    public boolean resolveBundle(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        return packageAdmin.resolveBundles(new Bundle[]{bundle});
    }

    public boolean resolveBundles(long[] bundleIdentifiers) throws IOException {
        if (bundleIdentifiers == null) {
            throw new IllegalArgumentException("Bundle IDs are null");
        }
        PackageAdmin packageAdmin = visitor.getPackageAdmin();
        if (packageAdmin == null) {
            throw new IOException("PackageAdmin is not available");
        }
        Bundle[] bundles = new Bundle[bundleIdentifiers.length];
        for (int i = 0; i < bundleIdentifiers.length; i++) {
            long bundleIdentifier = bundleIdentifiers[i];
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            bundles[i] = bundle;
        }
        return packageAdmin.resolveBundles(bundles);
    }

    public void restartFramework() throws IOException {
        org.osgi.framework.launch.Framework framework = visitor.getFramework();
        try {
            if (framework != null) {
                framework.update();
            } else {
                Bundle bundle = visitor.getBundle(0);
                if (bundle != null) {
                    bundle.update();
                }
            }
        } catch (Exception e) {
            throw new IOException("Unable to restart Framework", e);
        }
    }

    public void setBundleStartLevel(long bundleIdentifier, int newLevel) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        StartLevel startLevel = visitor.getStartLevel();
        if (startLevel == null) {
            throw new IOException("StartLevel is not available");
        }
        startLevel.setBundleStartLevel(bundle, newLevel);
    }

    public CompositeData setBundleStartLevels(long[] longs, int[] ints) throws IOException {
        return null;  // todo
    }

    public void setFrameworkStartLevel(int level) throws IOException {
        StartLevel startLevel = visitor.getStartLevel();
        if (startLevel == null) {
            throw new IOException("StartLevel is not available");
        }
        startLevel.setStartLevel(level);
    }

    public void setInitialBundleStartLevel(int level) throws IOException {
        StartLevel startLevel = visitor.getStartLevel();
        if (startLevel == null) {
            throw new IOException("StartLevel is not available");
        }
        startLevel.setInitialBundleStartLevel(level);
    }

    public void shutdownFramework() throws IOException {
        org.osgi.framework.launch.Framework framework = visitor.getFramework();
        try {
            if (framework != null) {
                framework.stop();
            } else {
                Bundle bundle = visitor.getBundle(0);
                if (bundle != null) {
                    bundle.stop();
                }
            }
        } catch (Exception e) {
            throw new IOException("Unable to stop Framework", e);
        }
    }

    public void startBundle(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        try {
            bundle.start();
        } catch (Exception e) {
            throw new IOException("Unable to start bundle: " + bundleIdentifier, e);
        }
    }

    public CompositeData startBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public void stopBundle(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        try {
            bundle.stop();
        } catch (Exception e) {
            throw new IOException("Unable to stop bundle: " + bundleIdentifier, e);
        }
    }

    public CompositeData stopBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public void uninstallBundle(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        try {
            bundle.uninstall();
        } catch (Exception e) {
            throw new IOException("Unable to uninstall bundle: " + bundleIdentifier, e);
        }
    }

    public CompositeData uninstallBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public void updateBundle(long bundleIdentifier) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        try {
            bundle.update();
        } catch (Exception e) {
            throw new IOException("Unable to update bundle: " + bundleIdentifier, e);
        }
    }

    public void updateBundleFromURL(long bundleIdentifier, String url) throws IOException {
        Bundle bundle = visitor.getBundle(bundleIdentifier);
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
        }
        try {
            bundle.update(new URL(url).openStream());
        } catch (Exception e) {
            throw new IOException("Unable to update bundle: " + bundleIdentifier, e);
        }
    }

    public CompositeData updateBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public CompositeData updateBundlesFromURL(long[] longs, String[] strings) throws IOException {
        return null;  // todo
    }

    public void updateFramework() throws IOException {
        try {
            Bundle bundle = visitor.getBundle(0);
            if (bundle != null) {
                bundle.update();
            }
        } catch (BundleException e) {
            throw new IOException("Unable to update Framework", e);
        }
    }
}
