/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.framework;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.osgilab.bundles.jmx.Utils;
import org.osgilab.bundles.jmx.beans.AbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * FrameworkMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class Framework extends AbstractMBean implements FrameworkMBean {
    public Framework() throws NotCompliantMBeanException {
        super(FrameworkMBean.class);
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

    public CompositeData installBundles(String[] locations) throws IOException {
        Set<String> remainingLocations = new HashSet<String>();
        if (locations != null) {
            remainingLocations.addAll(Arrays.asList(locations));
        }
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        String errorBundleLocation = null;
        String errorDetails = null;

        Iterator<String> locationsIterator = remainingLocations.iterator();
        while (locationsIterator.hasNext()) {
            String location = locationsIterator.next();
            locationsIterator.remove();
            try {
                Bundle bundle = visitor.installBundle(location);
                completedBundles.add(bundle);
            } catch (BundleException e) {
                isSuccess = false;
                errorBundleLocation = location;
                errorDetails = e.getMessage();
                break;
            }
        }

        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, remainingLocations.toArray(new String[remainingLocations.size()]));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleLocation);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_INSTALL_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CompositeData installBundlesFromURL(String[] locations, String[] urls) throws IOException {
        List<String> remainingLocations = new ArrayList<String>();
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        String errorBundleLocation = null;
        String errorDetails = null;

        if (locations != null && urls != null) {
            if (locations.length != urls.length) {
                throw new IllegalArgumentException("Locations array length is not equal to urls array length");
            }
            remainingLocations.addAll(Arrays.asList(locations));

            for (int i = 0; remainingLocations.size() > 0; i++) {
                String location = remainingLocations.remove(0);
                try {
                    Bundle bundle = visitor.installBundle(location, new URL(urls[i]).openStream());
                    completedBundles.add(bundle);
                } catch (Exception e) {
                    isSuccess = false;
                    errorBundleLocation = location;
                    errorDetails = e.getMessage();
                    break;
                }
            }
        }
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, remainingLocations.toArray(new String[remainingLocations.size()]));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleLocation);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_INSTALL_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
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

    public CompositeData setBundleStartLevels(long[] bundleIdentifiers, int[] newlevels) throws IOException {
        List<Bundle> bundles = new ArrayList<Bundle>();
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        long errorBundleId = 0;
        String errorDetails = null;

        if (bundleIdentifiers != null && newlevels != null) {
            if (bundleIdentifiers.length != newlevels.length) {
                throw new IllegalArgumentException("BundlesId array length is not equal to levels array length");
            }
            for (long bundleIdentifier : bundleIdentifiers) {
                Bundle bundle = visitor.getBundle(bundleIdentifier);
                if (bundle == null) {
                    throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
                }
                bundles.add(bundle);
            }
            StartLevel startLevel = visitor.getStartLevel();
            if (startLevel == null) {
                throw new IOException("StartLevel is not available");
            }

            for (int i = 0; bundles.size() > 0; i++) {
                Bundle bundle = bundles.remove(0);
                try {
                    startLevel.setBundleStartLevel(bundle, newlevels[i]);
                    completedBundles.add(bundle);
                } catch (Exception e) {
                    isSuccess = false;
                    errorBundleId = bundle.getBundleId();
                    errorDetails = e.getMessage();
                    break;
                }
            }
        }
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, Utils.getIds(bundles.toArray(new Bundle[bundles.size()])));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
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

    public CompositeData startBundles(long[] bundleIdentifiers) throws IOException {
        Set<Bundle> bundles = new HashSet<Bundle>();
        if (bundleIdentifiers != null) {
            for (long bundleIdentifier : bundleIdentifiers) {
                Bundle bundle = visitor.getBundle(bundleIdentifier);
                if (bundle == null) {
                    throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
                }
                bundles.add(bundle);
            }
        }
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        long errorBundleId = 0;
        String errorDetails = null;

        Iterator<Bundle> bundleIterator = bundles.iterator();
        while (bundleIterator.hasNext()) {
            Bundle bundle = bundleIterator.next();
            bundleIterator.remove();
            try {
                bundle.update();
                completedBundles.add(bundle);
            } catch (BundleException e) {
                isSuccess = false;
                errorBundleId = bundle.getBundleId();
                errorDetails = e.getMessage();
                break;
            }
        }

        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, Utils.getIds(bundles.toArray(new Bundle[bundles.size()])));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
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

    public CompositeData stopBundles(long[] bundleIdentifiers) throws IOException {
        Set<Bundle> bundles = new HashSet<Bundle>();
        if (bundleIdentifiers != null) {
            for (long bundleIdentifier : bundleIdentifiers) {
                Bundle bundle = visitor.getBundle(bundleIdentifier);
                if (bundle == null) {
                    throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
                }
                bundles.add(bundle);
            }
        }
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        long errorBundleId = 0;
        String errorDetails = null;

        Iterator<Bundle> bundleIterator = bundles.iterator();
        while (bundleIterator.hasNext()) {
            Bundle bundle = bundleIterator.next();
            bundleIterator.remove();
            try {
                bundle.stop();
                completedBundles.add(bundle);
            } catch (BundleException e) {
                isSuccess = false;
                errorBundleId = bundle.getBundleId();
                errorDetails = e.getMessage();
                break;
            }
        }

        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, Utils.getIds(bundles.toArray(new Bundle[bundles.size()])));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
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

    public CompositeData uninstallBundles(long[] bundleIdentifiers) throws IOException {
        Set<Bundle> bundles = new HashSet<Bundle>();
        if (bundleIdentifiers != null) {
            for (long bundleIdentifier : bundleIdentifiers) {
                Bundle bundle = visitor.getBundle(bundleIdentifier);
                if (bundle == null) {
                    throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
                }
                bundles.add(bundle);
            }
        }
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        long errorBundleId = 0;
        String errorDetails = null;

        Iterator<Bundle> bundleIterator = bundles.iterator();
        while (bundleIterator.hasNext()) {
            Bundle bundle = bundleIterator.next();
            bundleIterator.remove();
            try {
                bundle.uninstall();
                completedBundles.add(bundle);
            } catch (BundleException e) {
                isSuccess = false;
                errorBundleId = bundle.getBundleId();
                errorDetails = e.getMessage();
                break;
            }
        }

        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, Utils.getIds(bundles.toArray(new Bundle[bundles.size()])));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
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

    public CompositeData updateBundles(long[] bundleIdentifiers) throws IOException {
        Set<Bundle> bundles = new HashSet<Bundle>();
        if (bundleIdentifiers != null) {
            for (long bundleIdentifier : bundleIdentifiers) {
                Bundle bundle = visitor.getBundle(bundleIdentifier);
                if (bundle == null) {
                    throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
                }
                bundles.add(bundle);
            }
        }
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        long errorBundleId = 0;
        String errorDetails = null;

        Iterator<Bundle> bundleIterator = bundles.iterator();
        while (bundleIterator.hasNext()) {
            Bundle bundle = bundleIterator.next();
            bundleIterator.remove();
            try {
                bundle.update();
                completedBundles.add(bundle);
            } catch (BundleException e) {
                isSuccess = false;
                errorBundleId = bundle.getBundleId();
                errorDetails = e.getMessage();
                break;
            }
        }

        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, Utils.getIds(bundles.toArray(new Bundle[bundles.size()])));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CompositeData updateBundlesFromURL(long[] bundleIdentifiers, String[] urls) throws IOException {
        List<Bundle> bundles = new ArrayList<Bundle>();
        Set<Bundle> completedBundles = new HashSet<Bundle>();
        boolean isSuccess = true;
        long errorBundleId = 0;
        String errorDetails = null;

        if (bundleIdentifiers != null && urls != null) {
            if (bundleIdentifiers.length != urls.length) {
                throw new IllegalArgumentException("BundlesId array length is not equal to urls array length");
            }
            for (long bundleIdentifier : bundleIdentifiers) {
                Bundle bundle = visitor.getBundle(bundleIdentifier);
                if (bundle == null) {
                    throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
                }
                bundles.add(bundle);
            }

            for (int i = 0; bundles.size() > 0; i++) {
                Bundle bundle = bundles.remove(0);
                try {
                    bundle.update(new URL(urls[i]).openStream());
                    completedBundles.add(bundle);
                } catch (Exception e) {
                    isSuccess = false;
                    errorBundleId = bundle.getBundleId();
                    errorDetails = e.getMessage();
                    break;
                }
            }
        }
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(REMAINING, Utils.getIds(bundles.toArray(new Bundle[bundles.size()])));
        values.put(COMPLETED, Utils.getIds(completedBundles.toArray(new Bundle[completedBundles.size()])));
        if (!isSuccess) {
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
        }
        values.put(SUCCESS, isSuccess);
        try {
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (OpenDataException e) {
            e.printStackTrace();
            return null;
        }
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
