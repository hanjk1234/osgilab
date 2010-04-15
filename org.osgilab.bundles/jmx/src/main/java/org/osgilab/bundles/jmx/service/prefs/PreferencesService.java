/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.prefs;

import org.osgilab.bundles.jmx.beans.AbstractMBean;
import org.osgilab.bundles.jmx.beans.LogVisitor;
import org.osgilab.bundles.jmx.beans.OsgiVisitor;

import javax.management.NotCompliantMBeanException;

/**
 * @author dmytro.pishchukhin
 */
public class PreferencesService extends AbstractMBean implements PreferencesServiceMBean {
    protected PreferencesService(OsgiVisitor visitor, LogVisitor logVisitor) throws NotCompliantMBeanException {
        super(PreferencesServiceMBean.class, visitor, logVisitor);
    }
}
