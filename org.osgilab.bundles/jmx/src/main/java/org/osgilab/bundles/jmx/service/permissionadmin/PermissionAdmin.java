/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.permissionadmin;

import org.osgi.jmx.service.permissionadmin.PermissionAdminMBean;
import org.osgilab.bundles.jmx.beans.AbstractMBean;
import org.osgilab.bundles.jmx.beans.LogVisitor;
import org.osgilab.bundles.jmx.beans.OsgiVisitor;

import javax.management.NotCompliantMBeanException;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class PermissionAdmin extends AbstractMBean implements PermissionAdminMBean {
    protected PermissionAdmin(OsgiVisitor visitor, LogVisitor logVisitor) throws NotCompliantMBeanException {
        super(PermissionAdminMBean.class, visitor, logVisitor);
    }

    public String[] listLocations() throws IOException {
        return new String[0];  // todo
    }

    public String[] getPermissions(String s) throws IOException {
        return new String[0];  // todo
    }

    public void setDefaultPermissions(String[] strings) throws IOException {
        // todo
    }

    public String[] listDefaultPermissions() throws IOException {
        return new String[0];  // todo
    }

    public void setPermissions(String s, String[] strings) throws IOException {
        // todo
    }
}
