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
 * ConfigurationAdminMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class ConfigurationAdmin extends ServiceAbstractMBean<org.osgi.service.cm.ConfigurationAdmin>
        implements ConfigurationAdminMBean {

    public ConfigurationAdmin() throws NotCompliantMBeanException {
        super(ConfigurationAdminMBean.class);
    }

    public String createFactoryConfiguration(String factoryPid)
            throws IOException {
        try {
            Configuration configuration = service.createFactoryConfiguration(factoryPid);
            return configuration.getPid();
        } catch (IOException e) {
            logVisitor.warning("createFactoryConfiguration error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("createFactoryConfiguration error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public String createFactoryConfigurationForLocation(String factoryPid, String location)
            throws IOException {
        try {
            Configuration configuration = service.createFactoryConfiguration(factoryPid, location);
            return configuration.getPid();
        } catch (IOException e) {
            logVisitor.warning("createFactoryConfigurationForLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("createFactoryConfigurationForLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void delete(String pid) throws IOException {
        try {
            service.getConfiguration(pid).delete();
        } catch (IOException e) {
            logVisitor.warning("delete error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("delete error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void deleteForLocation(String pid, String location) throws IOException {
        try {
            service.getConfiguration(pid, location).delete();
        } catch (IOException e) {
            logVisitor.warning("deleteForLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("deleteForLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
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
            logVisitor.warning("deleteConfigurations error", e);
            throw new IllegalArgumentException("Filter is invalid: " + filter);
        } catch (IOException e) {
            logVisitor.warning("deleteConfigurations error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("deleteConfigurations error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public String getBundleLocation(String pid) throws IOException {
        try {
            return service.getConfiguration(pid).getBundleLocation();
        } catch (IOException e) {
            logVisitor.warning("getBundleLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getBundleLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public String getFactoryPid(String pid) throws IOException {
        try {
            return service.getConfiguration(pid).getFactoryPid();
        } catch (IOException e) {
            logVisitor.warning("getFactoryPid error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getFactoryPid error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public String getFactoryPidForLocation(String pid, String location) throws IOException {
        try {
            return service.getConfiguration(pid, location).getFactoryPid();
        } catch (IOException e) {
            logVisitor.warning("getFactoryPidForLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getFactoryPidForLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public TabularData getProperties(String pid) throws IOException {
        try {
            return Utils.getProperties(service.getConfiguration(pid).getProperties());
        } catch (IOException e) {
            logVisitor.warning("getProperties error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getProperties error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public TabularData getPropertiesForLocation(String pid, String location) throws IOException {
        try {
            return Utils.getProperties(service.getConfiguration(pid, location).getProperties());
        } catch (IOException e) {
            logVisitor.warning("getPropertiesForLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getPropertiesForLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
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
            return new String[0][0];
        } catch (InvalidSyntaxException e) {
            logVisitor.warning("getConfigurations error", e);
            throw new IllegalArgumentException("Filter is invalid: " + filter);
        } catch (IOException e) {
            logVisitor.warning("getConfigurations error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getConfigurations error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void setBundleLocation(String pid, String location) throws IOException {
        try {
            service.getConfiguration(pid).setBundleLocation(location);
        } catch (IOException e) {
            logVisitor.warning("setBundleLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("setBundleLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void update(String pid, TabularData properties) throws IOException {
        try {
            Configuration configuration = service.getConfiguration(pid);
            configuration.update(Utils.convertToDictionary(properties, false));
        } catch (IOException e) {
            logVisitor.warning("update error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("update error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void updateForLocation(String pid, String location, TabularData properties) throws IOException {
        try {
            Configuration configuration = service.getConfiguration(pid, location);
            configuration.update(Utils.convertToDictionary(properties, false));
        } catch (IOException e) {
            logVisitor.warning("updateForLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("updateForLocation error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

}
