/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.jmx.framework.ServiceStateMBean;

import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class ServiceState implements ServiceStateMBean {
    public String[] getObjectClass(long l) throws IOException {
        return new String[0];  // todo
    }

    public long getBundleIdentifier(long l) throws IOException {
        return 0;  // todo
    }

    public TabularData getProperties(long l) throws IOException {
        return null;  // todo
    }

    public TabularData listServices() throws IOException {
        return null;  // todo
    }

    public long[] getUsingBundles(long l) throws IOException {
        return new long[0];  // todo
    }
}
