/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.permissionadmin;

import org.osgi.jmx.service.permissionadmin.PermissionAdminMBean;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class PermissionAdmin extends ServiceAbstractMBean<org.osgi.service.permissionadmin.PermissionAdmin>
        implements PermissionAdminMBean {

    public PermissionAdmin() throws NotCompliantMBeanException {
        super(PermissionAdminMBean.class);
    }

    public String[] listLocations() throws IOException {
        return service.getLocations();
    }

    public String[] getPermissions(String location) throws IOException {
        return getPermissions(service.getPermissions(location));
    }

    public void setDefaultPermissions(String[] encodedPermissions) throws IOException {
        if (encodedPermissions != null) {
            service.setDefaultPermissions(getPermissions(encodedPermissions));
        }
    }

    public String[] listDefaultPermissions() throws IOException {
        return getPermissions(service.getDefaultPermissions());
    }

    public void setPermissions(String location, String[] encodedPermissions) throws IOException {
        if (encodedPermissions != null) {
            service.setPermissions(location, getPermissions(encodedPermissions));
        }
    }

    private PermissionInfo[] getPermissions(String[] encodedPermissions) {
        PermissionInfo[] permissions = new PermissionInfo[encodedPermissions.length];
        for (int i = 0; i < encodedPermissions.length; i++) {
            permissions[i] = new PermissionInfo(encodedPermissions[i]);
        }
        return permissions;
    }

    private String[] getPermissions(PermissionInfo[] permissions) {
        if (permissions != null) {
            String[] result = new String[permissions.length];
            for (int i = 0; i < permissions.length; i++) {
                result[i] = permissions[i].getEncoded();
            }
            return result;
        }
        return new String[0];
    }
}
