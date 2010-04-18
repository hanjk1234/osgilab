/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.beans.service.permissionadmin;

import org.osgi.jmx.service.permissionadmin.PermissionAdminMBean;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import java.io.IOException;

/**
 * PermissionAdminMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class PermissionAdmin extends ServiceAbstractMBean<org.osgi.service.permissionadmin.PermissionAdmin>
        implements PermissionAdminMBean {

    public PermissionAdmin() throws NotCompliantMBeanException {
        super(PermissionAdminMBean.class);
    }

    public String[] listLocations() throws IOException {
        try {
            return service.getLocations();
        } catch (Exception e) {
            logVisitor.warning("listLocations error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public String[] getPermissions(String location) throws IOException {
        try {
            return getPermissions(service.getPermissions(location));
        } catch (Exception e) {
            logVisitor.warning("getPermissions error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void setDefaultPermissions(String[] encodedPermissions) throws IOException {
        try {
            service.setDefaultPermissions(getPermissions(encodedPermissions));
        } catch (Exception e) {
            logVisitor.warning("setDefaultPermissions error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public String[] listDefaultPermissions() throws IOException {
        try {
            return getPermissions(service.getDefaultPermissions());
        } catch (Exception e) {
            logVisitor.warning("listDefaultPermissions error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    public void setPermissions(String location, String[] encodedPermissions) throws IOException {
        try {
            service.setPermissions(location, getPermissions(encodedPermissions));
        } catch (Exception e) {
            logVisitor.warning("setPermissions error", e);
            throw new IOException(e.getMessage(), e);
        }
    }

    private PermissionInfo[] getPermissions(String[] encodedPermissions) {
        if (encodedPermissions == null) {
            return null;
        }
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
