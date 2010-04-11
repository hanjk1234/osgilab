/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.jmx.framework.BundleStateMBean;

import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class BundleState implements BundleStateMBean {
    public long[] getRequiredBundles(long l) throws IOException {
        return new long[0];  // todo
    }

    public TabularData listBundles() throws IOException {
        return null;  // todo
    }

    public String[] getExportedPackages(long l) throws IOException {
        return new String[0];  // todo
    }

    public long[] getFragments(long l) throws IOException {
        return new long[0];  // todo
    }

    public TabularData getHeaders(long l) throws IOException {
        return null;  // todo
    }

    public long[] getHosts(long l) throws IOException {
        return new long[0];  // todo
    }

    public String[] getImportedPackages(long l) throws IOException {
        return new String[0];  // todo
    }

    public long getLastModified(long l) throws IOException {
        return 0;  // todo
    }

    public long[] getRegisteredServices(long l) throws IOException {
        return new long[0];  // todo
    }

    public long[] getRequiringBundles(long l) throws IOException {
        return new long[0];  // todo
    }

    public long[] getServicesInUse(long l) throws IOException {
        return new long[0];  // todo
    }

    public int getStartLevel(long l) throws IOException {
        return 0;  // todo
    }

    public String getState(long l) throws IOException {
        return null;  // todo
    }

    public String getSymbolicName(long l) throws IOException {
        return null;  // todo
    }

    public boolean isPersistentlyStarted(long l) throws IOException {
        return false;  // todo
    }

    public boolean isFragment(long l) throws IOException {
        return false;  // todo
    }

    public boolean isRemovalPending(long l) throws IOException {
        return false;  // todo
    }

    public boolean isRequired(long l) throws IOException {
        return false;  // todo
    }

    public String getLocation(long l) throws IOException {
        return null;  // todo
    }

    public String getVersion(long l) throws IOException {
        return null;  // todo
    }
}
