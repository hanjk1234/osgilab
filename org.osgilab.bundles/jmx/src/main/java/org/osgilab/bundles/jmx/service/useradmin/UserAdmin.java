/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.useradmin;

import org.osgi.jmx.service.useradmin.UserAdminMBean;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class UserAdmin extends ServiceAbstractMBean<org.osgi.service.useradmin.UserAdmin>
        implements UserAdminMBean {
    
    public UserAdmin() throws NotCompliantMBeanException {
        super(UserAdminMBean.class);
    }

    public void addCredential(String s, byte[] bytes, String s1) throws IOException {
        // todo
    }

    public void addCredentialString(String s, String s1, String s2) throws IOException {
        // todo
    }

    public boolean addMember(String s, String s1) throws IOException {
        return false;  // todo
    }

    public void addPropertyString(String s, String s1, String s2) throws IOException {
        // todo
    }

    public void addProperty(String s, byte[] bytes, String s1) throws IOException {
        // todo
    }

    public boolean addRequiredMember(String s, String s1) throws IOException {
        return false;  // todo
    }

    public void createUser(String s) throws IOException {
        // todo
    }

    public void createGroup(String s) throws IOException {
        // todo
    }

    public void createRole(String s) throws IOException {
        // todo
    }

    public CompositeData getAuthorization(String s) throws IOException {
        return null;  // todo
    }

    public TabularData getCredentials(String s) throws IOException {
        return null;  // todo
    }

    public CompositeData getGroup(String s) throws IOException {
        return null;  // todo
    }

    public String[] listGroups() throws IOException {
        return new String[0];  // todo
    }

    public String[] getGroups(String s) throws IOException {
        return new String[0];  // todo
    }

    public String[] getImpliedRoles(String s) throws IOException {
        return new String[0];  // todo
    }

    public String[] getMembers(String s) throws IOException {
        return new String[0];  // todo
    }

    public TabularData getProperties(String s) throws IOException {
        return null;  // todo
    }

    public String[] getRequiredMembers(String s) throws IOException {
        return new String[0];  // todo
    }

    public CompositeData getRole(String s) throws IOException {
        return null;  // todo
    }

    public String[] listRoles() throws IOException {
        return new String[0];  // todo
    }

    public String[] getRoles(String s) throws IOException {
        return new String[0];  // todo
    }

    public CompositeData getUser(String s) throws IOException {
        return null;  // todo
    }

    public String getUserWithProperty(String s, String s1) throws IOException {
        return null;  // todo
    }

    public String[] listUsers() throws IOException {
        return new String[0];  // todo
    }

    public String[] getUsers(String s) throws IOException {
        return new String[0];  // todo
    }

    public void removeCredential(String s, String s1) throws IOException {
        // todo
    }

    public boolean removeMember(String s, String s1) throws IOException {
        return false;  // todo
    }

    public void removeProperty(String s, String s1) throws IOException {
        // todo
    }

    public boolean removeRole(String s) throws IOException {
        return false;  // todo
    }

    public boolean removeGroup(String s) throws IOException {
        return false;  // todo
    }

    public boolean removeUser(String s) throws IOException {
        return false;  // todo
    }
}
