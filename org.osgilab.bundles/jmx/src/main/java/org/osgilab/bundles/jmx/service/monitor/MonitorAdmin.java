/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.monitor;

import org.osgi.service.monitor.MonitoringJob;
import org.osgi.service.monitor.StatusVariable;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdmin extends ServiceAbstractMBean<org.osgi.service.monitor.MonitorAdmin>
        implements MonitorAdminMBean {

    public MonitorAdmin() throws NotCompliantMBeanException {
        super(MonitorAdminMBean.class);
    }

    public String getDescription(String path) throws IllegalArgumentException, IOException {
        return service.getDescription(path);
    }

    public CompositeData getStatusVariable(String path) throws IllegalArgumentException, IOException {
        StatusVariable statusVariable = service.getStatusVariable(path);
        return getCompositeData(statusVariable);
    }

    public String[] getMonitorableNames() throws IOException {
        return service.getMonitorableNames();
    }

    public TabularData getStatusVariables(String monitorableId) throws IllegalArgumentException, IOException {
        StatusVariable[] statusVariables = service.getStatusVariables(monitorableId);
        TabularDataSupport dataSupport = new TabularDataSupport(STATUS_VARIABLES_TYPE);
        for (StatusVariable statusVariable : statusVariables) {
            dataSupport.put(getCompositeData(statusVariable));
        }
        return dataSupport;
    }

    public String[] getStatusVariableNames(String monitorableId) throws IllegalArgumentException, IOException {
        return service.getStatusVariableNames(monitorableId);
    }

    public void switchEvents(String path, boolean on) throws IllegalArgumentException, IOException {
        service.switchEvents(path, on);
    }

    public boolean resetStatusVariable(String path) throws IllegalArgumentException, IOException {
        return service.resetStatusVariable(path);
    }

    public TabularData getRunningJobs() throws IOException {
        MonitoringJob[] monitoringJobs = service.getRunningJobs();
        TabularDataSupport dataSupport = new TabularDataSupport(MONITORING_JOBS_TYPE);
        for (MonitoringJob monitoringJob : monitoringJobs) {
            dataSupport.put(getCompositeData(monitoringJob));
        }
        return dataSupport;
    }

    private String getValueAsString(StatusVariable statusVariable) {
        switch (statusVariable.getType()) {
            case StatusVariable.TYPE_BOOLEAN:
                return Boolean.toString(statusVariable.getBoolean());
            case StatusVariable.TYPE_FLOAT:
                return Float.toString(statusVariable.getFloat());
            case StatusVariable.TYPE_INTEGER:
                return Integer.toString(statusVariable.getInteger());
            case StatusVariable.TYPE_STRING:
                return statusVariable.getString();
        }
        return "";
    }

    private CompositeData getCompositeData(StatusVariable statusVariable) throws IOException {
        Map<String, Object> values = new HashMap<String, Object>();
        try {
            values.put(NAME, statusVariable.getID());
            values.put(TYPE, statusVariable.getType());
            values.put(COLLECTION_METHOD, statusVariable.getCollectionMethod());
            values.put(TIMESTAMP, statusVariable.getTimeStamp().getTime());
            values.put(VALUE, getValueAsString(statusVariable));
            return new CompositeDataSupport(STATUS_VARIABLE_TYPE, values);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private CompositeData getCompositeData(MonitoringJob monitoringJob) throws IOException {
        Map<String, Object> values = new HashMap<String, Object>();
        try {
            values.put(INITIATOR, monitoringJob.getInitiator());
            values.put(REPORT_COUNT, monitoringJob.getReportCount());
            values.put(SCHEDULE, monitoringJob.getSchedule());
            values.put(STATUS_VARIABLE_NAMES, monitoringJob.getStatusVariableNames());
            values.put(LOCAL, monitoringJob.isLocal());
            values.put(RUNNING, monitoringJob.isRunning());
            return new CompositeDataSupport(MONITORING_JOB_TYPE, values);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
