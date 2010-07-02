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

package org.osgilab.bundles.monitoradmin.util;

/**
 * StatusVariable path filter with '*' wildcard support
 *
 * @author dmytro.pishchukhin
 */
public class StatusVariablePathFilter extends StatusVariablePath {
    private boolean monitorableWildcard = false;
    private boolean statusVariableWildcard = false;

    public StatusVariablePathFilter(String path) throws IllegalArgumentException {
        String[] ids = parseIds(path);
        this.path = path;
        if (ids[0].indexOf('*') != -1) {
            monitorableId = ids[0].replace("*", "");
            monitorableWildcard = true;
        } else {
            monitorableId = ids[0];
        }
        if (ids[1].indexOf('*') != -1) {
            statusVariableId = ids[1].replace("*", "");
            statusVariableWildcard = true;
        } else {
            statusVariableId = ids[1];
        }
    }

    @Override
    protected boolean validateId(String id) {
        return Utils.validatePathFilterId(id);
    }

    /**
     * Check that given monitorable Id and StatusVarialbe Id match Filter
     * @param monitorableId monitorable Id
     * @param statusVariableId StatusVariable Id
     * @return result
     */
    public boolean match(String monitorableId, String statusVariableId) {
        return (monitorableWildcard ?
                monitorableId.startsWith(this.monitorableId) :
                monitorableId.equals(this.monitorableId))
                &&
                (statusVariableWildcard ?
                        statusVariableId.startsWith(this.statusVariableId) :
                        statusVariableId.endsWith(this.statusVariableId));
    }

    /**
     * Check that filter contains monitorable Id wildcard
     * @return <code>true</code> - contains, otherwise - <code>false</code>
     */
    public boolean isMonitorableWildcard() {
        return monitorableWildcard;
    }

    /**
     * Check that filter contains StatusVariable Id wildcard
     * @return <code>true</code> - contains, otherwise - <code>false</code>
     */
    public boolean isStatusVariableWildcard() {
        return statusVariableWildcard;
    }
}
