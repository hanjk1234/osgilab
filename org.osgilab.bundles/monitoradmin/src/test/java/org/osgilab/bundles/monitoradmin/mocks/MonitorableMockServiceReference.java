/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin.mocks;

import org.osgi.framework.Constants;
import org.osgi.service.monitor.Monitorable;
import org.springframework.osgi.mock.MockServiceReference;

import java.util.Hashtable;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorableMockServiceReference extends MockServiceReference {
    public MonitorableMockServiceReference(String pid) {
        super(new String[]{Monitorable.class.getName()});
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Constants.SERVICE_PID, pid);
        setProperties(props);
    }
}
