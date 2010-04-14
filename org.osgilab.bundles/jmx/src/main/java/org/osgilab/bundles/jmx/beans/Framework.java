/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans;

import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.service.startlevel.StartLevel;
import org.osgilab.bundles.jmx.OsgiVisitor;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import java.io.IOException;

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

    public long installBundle(String s) throws IOException {
        return 0;  // todo
    }

    public long installBundleFromURL(String s, String s1) throws IOException {
        return 0;  // todo
    }

    public CompositeData installBundles(String[] strings) throws IOException {
        return null;  // todo
    }

    public CompositeData installBundlesFromURL(String[] strings, String[] strings1) throws IOException {
        return null;  // todo
    }

    public void refreshBundle(long l) throws IOException {
        // todo
    }

    public void refreshBundles(long[] longs) throws IOException {
        // todo
    }

    public boolean resolveBundle(long l) throws IOException {
        return false;  // todo
    }

    public boolean resolveBundles(long[] longs) throws IOException {
        return false;  // todo
    }

    public void restartFramework() throws IOException {
        // todo
    }

    public void setBundleStartLevel(long l, int i) throws IOException {
        // todo
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
        // todo
    }

    public void startBundle(long l) throws IOException {
        // todo
    }

    public CompositeData startBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public void stopBundle(long l) throws IOException {
        // todo
    }

    public CompositeData stopBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public void uninstallBundle(long l) throws IOException {
        // todo
    }

    public CompositeData uninstallBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public void updateBundle(long l) throws IOException {
        // todo
    }

    public void updateBundleFromURL(long l, String s) throws IOException {
        // todo
    }

    public CompositeData updateBundles(long[] longs) throws IOException {
        return null;  // todo
    }

    public CompositeData updateBundlesFromURL(long[] longs, String[] strings) throws IOException {
        return null;  // todo
    }

    public void updateFramework() throws IOException {
        // todo
    }
}
