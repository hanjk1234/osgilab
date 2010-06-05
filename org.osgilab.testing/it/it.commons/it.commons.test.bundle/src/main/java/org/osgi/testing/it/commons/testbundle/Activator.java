/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgi.testing.it.commons.testbundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.testing.it.commons.testbundle.service.Echo;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Test bundle activator
 *
 * @author dmytro.pishchukhin
 */
public class Activator implements BundleActivator {
    private ServiceRegistration registration;

    public void start(final BundleContext bundleContext) throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // do nothing
                }
                Dictionary<String, String> props = new Hashtable<String, String>();
                props.put("testkey", "testvalue");
                registration = bundleContext.registerService(Echo.class.getName(), new EchoImpl(), props);
            }
        }.start();
    }

    public void stop(BundleContext bundleContext) throws Exception {
        registration.unregister();
    }
}
