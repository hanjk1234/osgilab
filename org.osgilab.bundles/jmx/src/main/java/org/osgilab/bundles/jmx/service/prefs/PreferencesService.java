/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.prefs;

import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;

/**
 * @author dmytro.pishchukhin
 */
public class PreferencesService extends ServiceAbstractMBean<org.osgi.service.prefs.PreferencesService>
        implements PreferencesServiceMBean {
    
    public PreferencesService() throws NotCompliantMBeanException {
        super(PreferencesServiceMBean.class);
    }
}
