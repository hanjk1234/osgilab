/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.monitor;

import org.osgilab.bundles.jmx.beans.AbstractMBean;
import org.osgilab.bundles.jmx.beans.LogVisitor;
import org.osgilab.bundles.jmx.beans.OsgiVisitor;

import javax.management.NotCompliantMBeanException;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdmin extends AbstractMBean implements MonitorAdminMBean {
    protected MonitorAdmin(OsgiVisitor visitor, LogVisitor logVisitor) throws NotCompliantMBeanException {
        super(MonitorAdminMBean.class, visitor, logVisitor);
    }
}
