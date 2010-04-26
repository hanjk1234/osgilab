/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin.job;

import org.osgi.service.monitor.StatusVariable;

/**
 * Interface to access some internal MonitorAdminImpl from Jobs
 *
 * @author dmytro.pishchukhin
 */
public interface MonitoringJobVisitor {
    StatusVariable getStatusVariable(String path) throws IllegalArgumentException, SecurityException;

    void cancelJob(AbstractMonitoringJob job);

    void fireEvent(String monitorableId, StatusVariable statusVariable, String initiator);
}
