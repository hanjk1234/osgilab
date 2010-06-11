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
    /**
     * Get status variable by path
     *
     * @param path path
     * @return StatusVariable by path
     *
     * @throws IllegalArgumentException path is invalid
     * @throws SecurityException        some problems with security permissions
     * @see org.osgi.service.monitor.Monitorable#getStatusVariable(String)
     */
    StatusVariable getStatusVariable(String path) throws IllegalArgumentException, SecurityException;

    /**
     * Cancel Monitoring job
     * @param job job
     */
    void cancelJob(AbstractMonitoringJob job);

    /**
     * Fire event with given parameters
     * @param monitorableId monitorable id
     * @param statusVariable StatusVariable value
     * @param initiator initiator
     */
    void fireEvent(String monitorableId, StatusVariable statusVariable, String initiator);
}
