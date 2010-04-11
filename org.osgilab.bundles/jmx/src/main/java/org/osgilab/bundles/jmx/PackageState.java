/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.jmx.framework.PackageStateMBean;

import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class PackageState implements PackageStateMBean {
    public long[] getExportingBundles(String s, String s1) throws IOException {
        return new long[0];  // todo
    }

    public long[] getImportingBundles(String s, String s1, long l) throws IOException {
        return new long[0];  // todo
    }

    public TabularData listPackages() throws IOException {
        return null;  // todo
    }

    public boolean isRemovalPending(String s, String s1, long l) throws IOException {
        return false;  // todo
    }
}
