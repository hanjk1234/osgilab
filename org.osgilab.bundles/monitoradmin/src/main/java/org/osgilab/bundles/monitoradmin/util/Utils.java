/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin.util;

import org.osgi.framework.Constants;

import java.util.regex.Pattern;

/**
 * Utils class
 *
 * @author dmytro.pishchukhin
 */
public class Utils {
    /**
     * MonitorableId and Status variable name validate pattern (OSGi core 1.3.2: symbolic-name)
     */
    private static final Pattern PATH_ID_VALIDATE_PATTERN = Pattern.compile("((\\w|_|-)+)(\\.(\\w|_|-)+)*");
    /**
     * MonitorableId and Status variable name validate pattern (OSGi CMPN 119.6.1: wildcard-pid)
     */
    private static final Pattern FILTER_ID_VALIDATE_PATTERN = Pattern.compile("(\\*)|(((\\w|_|-)+)(\\.(\\w|_|-)*)*)(\\*)?");

    /**
     * Create service filter for given monitorable Id
     * @param monitorableId monitorable id
     * @return filter
     */
    public static String createServicePidFilter(String monitorableId) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append(Constants.SERVICE_PID);
        builder.append('=');
        builder.append(monitorableId);
        builder.append(')');
        return builder.toString();
    }

    /**
     * Validate Path Id
     * @param id id
     * @return validation result
     */
    public static boolean validatePathId(String id) {
        return PATH_ID_VALIDATE_PATTERN.matcher(id).matches();
    }

    /**
     * Validate Path Filter Id
     * @param id id
     * @return validation result
     */
    public static boolean validatePathFilterId(String id) {
        return FILTER_ID_VALIDATE_PATTERN.matcher(id).matches();
    }
}
