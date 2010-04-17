/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.monitor;

import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdmin extends ServiceAbstractMBean<org.osgi.service.monitor.MonitorAdmin>
        implements MonitorAdminMBean {

    public MonitorAdmin() throws NotCompliantMBeanException {
        super(MonitorAdminMBean.class);
    }

    public String getDescription(String path) throws IllegalArgumentException, IOException {
        return null;  // todo
    }

    public CompositeData getStatusVariable(String path) throws IllegalArgumentException, IOException {
        return null;  // todo
    }

    public String[] getMonitorableNames() throws IOException {
        return new String[0];  // todo
    }

    public TabularData getStatusVariables(String monitorableId) throws IllegalArgumentException, IOException {
        return null;  // todo
    }

    public String[] getStatusVariableNames(String monitorableId) throws IllegalArgumentException, IOException {
        return new String[0];  // todo
    }

    public void switchEvents(String path, boolean on) throws IllegalArgumentException, IOException {
        // todo
    }

    public boolean resetStatusVariable(String path) throws IllegalArgumentException, IOException {
        return false;  // todo
    }

    public TabularData getRunningJobs() throws IOException {
        return null;  // todo
    }
}
