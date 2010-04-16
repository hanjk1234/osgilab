/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.cm;

import org.osgi.jmx.service.cm.ConfigurationAdminMBean;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class ConfigurationAdmin extends ServiceAbstractMBean<org.osgi.service.cm.ConfigurationAdmin> 
        implements ConfigurationAdminMBean {
    public ConfigurationAdmin() throws NotCompliantMBeanException {
        super(ConfigurationAdminMBean.class);
    }

    public String createFactoryConfiguration(String s) throws IOException {
        return null;  // todo
    }

    public String createFactoryConfigurationForLocation(String s, String s1) throws IOException {
        return null;  // todo
    }

    public void delete(String s) throws IOException {
        // todo
    }

    public void deleteForLocation(String s, String s1) throws IOException {
        // todo
    }

    public void deleteConfigurations(String s) throws IOException {
        // todo
    }

    public String getBundleLocation(String s) throws IOException {
        return null;  // todo
    }

    public String getFactoryPid(String s) throws IOException {
        return null;  // todo
    }

    public String getFactoryPidForLocation(String s, String s1) throws IOException {
        return null;  // todo
    }

    public TabularData getProperties(String s) throws IOException {
        return null;  // todo
    }

    public TabularData getPropertiesForLocation(String s, String s1) throws IOException {
        return null;  // todo
    }

    public String[][] getConfigurations(String s) throws IOException {
        return new String[0][];  // todo
    }

    public void setBundleLocation(String s, String s1) throws IOException {
        // todo
    }

    public void update(String s, TabularData tabularData) throws IOException {
        // todo
    }

    public void updateForLocation(String s, String s1, TabularData tabularData) throws IOException {
        // todo
    }
}
