/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgilab.bundles.jmx.beans.service.monitor;

import org.osgi.service.monitor.MonitoringJob;
import org.osgi.service.monitor.StatusVariable;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;
import org.osgilab.bundles.jmx.service.monitor.MonitorAdminMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.*;
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
        try {
            return service.getDescription(path);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getDescription error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getDescription error", e);
            throw new IOException(e.getMessage());
        }
    }

    public CompositeData getStatusVariable(String path) throws IllegalArgumentException, IOException {
        try {
            StatusVariable statusVariable = service.getStatusVariable(path);
            return getCompositeData(statusVariable);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getStatusVariable error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getStatusVariable error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String[] getMonitorableNames() throws IOException {
        try {
            return service.getMonitorableNames();
        } catch (Exception e) {
            logVisitor.warning("getMonitorableNames error", e);
            throw new IOException(e.getMessage());
        }
    }

    public TabularData getStatusVariables(String monitorableId) throws IllegalArgumentException, IOException {
        try {
            StatusVariable[] statusVariables = service.getStatusVariables(monitorableId);
            TabularDataSupport dataSupport = new TabularDataSupport(STATUS_VARIABLES_TYPE);
            for (StatusVariable statusVariable : statusVariables) {
                dataSupport.put(getCompositeData(statusVariable));
            }
            return dataSupport;
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getStatusVariables error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getStatusVariables error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String[] getStatusVariableNames(String monitorableId) throws IllegalArgumentException, IOException {
        try {
            return service.getStatusVariableNames(monitorableId);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getStatusVariableNames error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getStatusVariableNames error", e);
            throw new IOException(e.getMessage());
        }
    }

    public void switchEvents(String path, boolean on) throws IllegalArgumentException, IOException {
        try {
            service.switchEvents(path, on);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("switchEvents error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("switchEvents error", e);
            throw new IOException(e.getMessage());
        }
    }

    public boolean resetStatusVariable(String path) throws IllegalArgumentException, IOException {
        try {
            return service.resetStatusVariable(path);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("resetStatusVariable error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("resetStatusVariable error", e);
            throw new IOException(e.getMessage());
        }
    }

    public TabularData getRunningJobs() throws IOException {
        try {
            MonitoringJob[] monitoringJobs = service.getRunningJobs();
            TabularDataSupport dataSupport = new TabularDataSupport(MONITORING_JOBS_TYPE);
            for (MonitoringJob monitoringJob : monitoringJobs) {
                dataSupport.put(getCompositeData(monitoringJob));
            }
            return dataSupport;
        } catch (Exception e) {
            logVisitor.warning("getRunningJobs error", e);
            throw new IOException(e.getMessage());
        }
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

    private CompositeData getCompositeData(StatusVariable statusVariable) throws OpenDataException {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(NAME, statusVariable.getID());
        values.put(TYPE, statusVariable.getType());
        values.put(COLLECTION_METHOD, statusVariable.getCollectionMethod());
        values.put(TIMESTAMP, statusVariable.getTimeStamp().getTime());
        values.put(VALUE, getValueAsString(statusVariable));
        return new CompositeDataSupport(STATUS_VARIABLE_TYPE, values);
    }

    private CompositeData getCompositeData(MonitoringJob monitoringJob) throws OpenDataException {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(INITIATOR, monitoringJob.getInitiator());
        values.put(REPORT_COUNT, monitoringJob.getReportCount());
        values.put(SCHEDULE, monitoringJob.getSchedule());
        values.put(STATUS_VARIABLE_NAMES, monitoringJob.getStatusVariableNames());
        values.put(LOCAL, monitoringJob.isLocal());
        values.put(RUNNING, monitoringJob.isRunning());
        return new CompositeDataSupport(MONITORING_JOB_TYPE, values);
    }
}
