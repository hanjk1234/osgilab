/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.cm;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.jmx.service.cm.ConfigurationAdminMBean;
import org.osgi.service.cm.Configuration;
import org.osgilab.bundles.jmx.Utils;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.*;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class ConfigurationAdmin extends ServiceAbstractMBean<org.osgi.service.cm.ConfigurationAdmin>
        implements ConfigurationAdminMBean {

    public ConfigurationAdmin() throws NotCompliantMBeanException {
        super(ConfigurationAdminMBean.class);
    }

    public String createFactoryConfiguration(String factoryPid)
            throws IOException {
        Configuration configuration = service.createFactoryConfiguration(factoryPid);
        return configuration.getPid();
    }

    public String createFactoryConfigurationForLocation(String factoryPid, String location)
            throws IOException {
        Configuration configuration = service.createFactoryConfiguration(factoryPid, location);
        return configuration.getPid();
    }

    public void delete(String pid) throws IOException {
        service.getConfiguration(pid).delete();
    }

    public void deleteForLocation(String pid, String location) throws IOException {
        service.getConfiguration(pid, location).delete();
    }

    public void deleteConfigurations(String filter) throws IOException {
        try {
            Configuration[] configurations = service.listConfigurations(filter);
            if (configurations != null) {
                for (Configuration configuration : configurations) {
                    configuration.delete();
                }
            }
        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException("Filter is invalid: " + filter);
        }
    }

    public String getBundleLocation(String pid) throws IOException {
        return service.getConfiguration(pid).getBundleLocation();
    }

    public String getFactoryPid(String pid) throws IOException {
        return service.getConfiguration(pid).getFactoryPid();
    }

    public String getFactoryPidForLocation(String pid, String location) throws IOException {
        return service.getConfiguration(pid, location).getFactoryPid();
    }

    public TabularData getProperties(String pid) throws IOException {
        return Utils.getProperties(service.getConfiguration(pid).getProperties());
    }

    public TabularData getPropertiesForLocation(String pid, String location) throws IOException {
        return Utils.getProperties(service.getConfiguration(pid, location).getProperties());
    }

    public String[][] getConfigurations(String filter) throws IOException {
        try {
            Configuration[] configurations = service.listConfigurations(filter);
            if (configurations != null) {
                String[][] result = new String[0][2];
                for (int i = 0; i < configurations.length; i++) {
                    Configuration configuration = configurations[i];
                    result[i][0] = configuration.getPid();
                    result[i][1] = configuration.getBundleLocation();
                }
                return result;
            }
            return new String[0][];
        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException("Filter is invalid: " + filter);
        }
    }

    public void setBundleLocation(String pid, String location) throws IOException {
        service.getConfiguration(pid).setBundleLocation(location);
    }

    public void update(String pid, TabularData properties) throws IOException {
        Configuration configuration = service.getConfiguration(pid);
        configuration.update(Utils.convertToDictionary(properties, false));
    }

    public void updateForLocation(String pid, String location, TabularData properties) throws IOException {
        Configuration configuration = service.getConfiguration(pid, location);
        configuration.update(Utils.convertToDictionary(properties, false));
    }

}
