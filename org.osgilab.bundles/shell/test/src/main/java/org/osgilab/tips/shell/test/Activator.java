/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.tips.shell.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator {
    private ServiceRegistration registration;

    public void start(BundleContext context) throws Exception {
        ShellTestService service = new ShellTestService(context);

        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("org.osgilab.tips.shell.group.id", "bundlesinfo");
        props.put("org.osgilab.tips.shell.group.name", "Bundles Information");
        props.put("org.osgilab.tips.shell.commands", new String[][] {
                new String[] {"bndsinfo", "bndsinfo - Print information for all bundles"},
                new String[] {"bndinfo", "bndinfo <bundleId> - Print information for bundle with <bundleId>"}
        });
        registration = context.registerService(ShellTestService.class.getName(), service, props);
    }

    public void stop(BundleContext context) throws Exception {
        registration.unregister();
        registration = null;
    }
}
