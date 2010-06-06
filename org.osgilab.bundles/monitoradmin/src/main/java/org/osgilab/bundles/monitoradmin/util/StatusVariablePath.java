/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin.util;

/**
 * Status Variable path
 *
 * @author dmytro.pishchukhin
 */
public class StatusVariablePath {
    protected String monitorableId;
    protected String statusVariableId;
    protected String path;

    /**
     * Initialize object and parse input path value
     *
     * @param path path value
     * @throws IllegalArgumentException path is <code>null</code> or invalid
     */
    public StatusVariablePath(String path) throws IllegalArgumentException {
        String[] ids = parseIds(path);
        this.path = path;
        monitorableId = ids[0];
        statusVariableId = ids[1];
    }

    /**
     * Initialize object from monitorableId and status variable Id
     *
     * @param monitorableId    monitorableId value
     * @param statusVariableId status variable Id value
     * @throws IllegalArgumentException ids are <code>null</code> or invalid;
     */
    public StatusVariablePath(String monitorableId, String statusVariableId) throws IllegalArgumentException {
        if (monitorableId == null) {
            throw new IllegalArgumentException("MonitorableId is null");
        }
        if (statusVariableId == null) {
            throw new IllegalArgumentException("StatusVariableId is null");
        }
        if (!validateId(monitorableId)) {
            throw new IllegalArgumentException("MonitorableId is invalid");
        }
        if (!validateId(statusVariableId)) {
            throw new IllegalArgumentException("StatusVariableId is invalid");
        }
        this.monitorableId = monitorableId;
        this.statusVariableId = statusVariableId;
        path = this.monitorableId + '/' + this.statusVariableId;
    }

    public StatusVariablePath() {
    }

    public String getMonitorableId() {
        return monitorableId;
    }

    public String getStatusVariableId() {
        return statusVariableId;
    }

    public String getPath() {
        return path;
    }

    /**
     * Parse path and return non-nullable array (lenght = 2) with non-empty IDs values:
     * <li>resultArray[0] = monitorableId
     * <li>resultArray[1] = statusVariableId
     *
     * @param path status variable path
     * @return non-nullable array with ids
     *
     * @throws IllegalArgumentException path is <code>null</code> or invalid
     *                                  (contains more or less than one separator '/' or parsed IDs are empty or invalid)
     */
    protected String[] parseIds(String path) throws IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
        String[] parts = path.split("/");
        if (parts.length != 2 || !validateId(parts[0]) || !validateId(parts[1])) {
            throw new IllegalArgumentException("Path value is invalid: " + path);
        }
        return parts;
    }

    protected boolean validateId(String id) {
        return Utils.validatePathId(id);
    }
}
