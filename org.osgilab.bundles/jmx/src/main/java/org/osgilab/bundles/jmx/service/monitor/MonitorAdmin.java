/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.monitor;

import org.osgilab.bundles.jmx.beans.AbstractMBean;

import javax.management.NotCompliantMBeanException;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdmin extends AbstractMBean implements MonitorAdminMBean {
    protected MonitorAdmin() throws NotCompliantMBeanException {
        super(MonitorAdminMBean.class);
    }
}
