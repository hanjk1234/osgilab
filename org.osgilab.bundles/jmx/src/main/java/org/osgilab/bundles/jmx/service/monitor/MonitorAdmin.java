/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.monitor;

import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdmin extends ServiceAbstractMBean<org.osgi.service.monitor.MonitorAdmin>
        implements MonitorAdminMBean {

    public MonitorAdmin() throws NotCompliantMBeanException {
        super(MonitorAdminMBean.class);
    }
}
