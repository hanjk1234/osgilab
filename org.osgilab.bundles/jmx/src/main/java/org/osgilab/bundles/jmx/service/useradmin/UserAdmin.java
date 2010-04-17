/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.useradmin;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.jmx.service.useradmin.UserAdminMBean;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgilab.bundles.jmx.Utils;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularData;
import java.io.IOException;
import java.util.*;

/**
 * @author dmytro.pishchukhin
 */
public class UserAdmin extends ServiceAbstractMBean<org.osgi.service.useradmin.UserAdmin>
        implements UserAdminMBean {

    public UserAdmin() throws NotCompliantMBeanException {
        super(UserAdminMBean.class);
    }

    public void addCredential(String key, byte[] value, String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        Dictionary credentials = ((User) role).getCredentials();
        if (credentials != null) {
            credentials.put(key, value);
        }
    }

    public void addCredentialString(String key, String value, String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        Dictionary credentials = ((User) role).getCredentials();
        if (credentials != null) {
            credentials.put(key, value);
        }
    }

    public boolean addMember(String groupname, String rolename) throws IOException {
        Role role = service.getRole(groupname);
        if (role != null && role.getType() == Role.GROUP) {
            Role member = service.getRole(rolename);
            if (member != null) {
                return ((Group) role).addMember(member);
            }
        }
        return false;
    }

    public void addPropertyString(String key, String value, String rolename) throws IOException {
        Role role = service.getRole(rolename);
        if (role != null) {
            Dictionary properties = role.getProperties();
            if (properties != null) {
                properties.put(key, value);
            }
        }
    }

    public void addProperty(String key, byte[] value, String rolename) throws IOException {
        Role role = service.getRole(rolename);
        if (role != null) {
            Dictionary properties = role.getProperties();
            if (properties != null) {
                properties.put(key, value);
            }
        }
    }

    public boolean addRequiredMember(String groupname, String rolename) throws IOException {
        Role role = service.getRole(groupname);
        if (role != null && role.getType() == Role.GROUP) {
            Role requiredMember = service.getRole(rolename);
            if (requiredMember != null) {
                return ((Group) role).addRequiredMember(requiredMember);
            }
        }
        return false;
    }

    public void createUser(String name) throws IOException {
        service.createRole(name, Role.USER);
    }

    public void createGroup(String name) throws IOException {
        service.createRole(name, Role.GROUP);
    }

    public void createRole(String name) throws IOException {
        service.createRole(name, Role.ROLE);
    }

    public CompositeData getAuthorization(String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        Authorization authorization = service.getAuthorization((User) role);
        Map<String, Object> values = new HashMap<String, Object>();
        try {
            String name = authorization.getName();
            values.put(NAME, name);
            Role authRole = service.getRole(name);
            values.put(TYPE, authRole.getType());
            return new CompositeDataSupport(AUTORIZATION_TYPE, values);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public TabularData getCredentials(String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        return Utils.getProperties(((User) role).getCredentials());
    }

    public CompositeData getGroup(String groupname) throws IOException {
        Role role = service.getRole(groupname);
        if (role == null || role.getType() != Role.GROUP) {
            throw new IllegalArgumentException(groupname + " is not Group name");
        }
        Group group = (Group) role;
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(NAME, group.getName());
            values.put(TYPE, group.getProperties());
            values.put(PROPERTIES, Utils.getProperties(group.getProperties()));
            values.put(CREDENTIALS, Utils.getProperties(group.getCredentials()));
            values.put(MEMBERS, getRoleNames(group.getMembers()));
            values.put(REQUIRED_MEMBERS, getRoleNames(group.getRequiredMembers()));
            return new CompositeDataSupport(GROUP_TYPE, values);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public String[] listGroups() throws IOException {
        try {
            return getRoleNames(null, Role.GROUP);
        } catch (InvalidSyntaxException e) {
            throw new IOException(e);
        }
    }

    public String[] getGroups(String filter) throws IOException {
        try {
            return getRoleNames(filter, Role.GROUP);
        } catch (InvalidSyntaxException e) {
            throw new IOException(e);
        }
    }

    public String[] getImpliedRoles(String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        Authorization authorization = service.getAuthorization((User) role);
        if (authorization != null) {
            return authorization.getRoles();
        } else {
            return new String[0];
        }
    }

    public String[] getMembers(String groupname) throws IOException {
        Role role = service.getRole(groupname);
        if (role == null || role.getType() != Role.GROUP) {
            throw new IllegalArgumentException(groupname + " is not Group name");
        }
        return getRoleNames(((Group) role).getMembers());
    }

    public TabularData getProperties(String rolename) throws IOException {
        Role role = service.getRole(rolename);
        return Utils.getProperties(role != null ? role.getProperties() : null);
    }

    public String[] getRequiredMembers(String groupname) throws IOException {
        Role role = service.getRole(groupname);
        if (role == null || role.getType() != Role.GROUP) {
            throw new IllegalArgumentException(groupname + " is not Group name");
        }
        return getRoleNames(((Group) role).getRequiredMembers());
    }

    public CompositeData getRole(String name) throws IOException {
        Role role = service.getRole(name);
        if (role != null) {
            try {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put(NAME, role.getName());
                values.put(TYPE, role.getProperties());
                values.put(PROPERTIES, Utils.getProperties(role.getProperties()));
                return new CompositeDataSupport(ROLE_TYPE, values);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return null;
    }

    public String[] listRoles() throws IOException {
        try {
            return getRoleNames(null, Role.ROLE);
        } catch (InvalidSyntaxException e) {
            throw new IOException(e);
        }
    }

    public String[] getRoles(String filter) throws IOException {
        try {
            return getRoleNames(filter, Role.ROLE);
        } catch (InvalidSyntaxException e) {
            throw new IOException(e);
        }
    }

    public CompositeData getUser(String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        User user = (User) role;
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(NAME, user.getName());
            values.put(TYPE, user.getProperties());
            values.put(PROPERTIES, Utils.getProperties(user.getProperties()));
            values.put(CREDENTIALS, Utils.getProperties(user.getCredentials()));
            return new CompositeDataSupport(USER_TYPE, values);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public String getUserWithProperty(String key, String value) throws IOException {
        User user = service.getUser(key, value);
        if (user != null) {
            return user.getName();
        } else {
            return null;
        }
    }

    public String[] listUsers() throws IOException {
        try {
            return getRoleNames(null, Role.USER);
        } catch (InvalidSyntaxException e) {
            throw new IOException(e);
        }
    }

    public String[] getUsers(String filter) throws IOException {
        try {
            return getRoleNames(filter, Role.USER);
        } catch (InvalidSyntaxException e) {
            throw new IOException(e);
        }
    }

    public void removeCredential(String key, String username) throws IOException {
        Role role = service.getRole(username);
        if (role == null || role.getType() != Role.USER) {
            throw new IllegalArgumentException(username + " is not User name");
        }
        Dictionary credentials = ((User) role).getCredentials();
        if (credentials != null) {
            credentials.remove(key);
        }
    }

    public boolean removeMember(String groupname, String rolename) throws IOException {
        Role role = service.getRole(groupname);
        if (role == null || role.getType() != Role.GROUP) {
            throw new IllegalArgumentException(groupname + " is not Group name");
        }
        return ((Group) role).removeMember(service.getRole(rolename));
    }

    public void removeProperty(String key, String rolename) throws IOException {
        Role role = service.getRole(rolename);
        if (role != null) {
            Dictionary properties = role.getProperties();
            if (properties != null) {
                properties.remove(key);
            }
        }
    }

    public boolean removeRole(String name) throws IOException {
        return removeRole(name, Role.ROLE);
    }

    public boolean removeGroup(String name) throws IOException {
        return removeRole(name, Role.GROUP);
    }

    public boolean removeUser(String name) throws IOException {
        return removeRole(name, Role.USER);
    }

    private String[] getRoleNames(String filter, int type) throws InvalidSyntaxException {
        Role[] roles = service.getRoles(filter);
        List<String> names = new ArrayList<String>();
        if (roles != null) {
            for (Role role : roles) {
                if (role.getType() == type) {
                    names.add(role.getName());
                }
            }
        }
        return names.toArray(new String[names.size()]);
    }

    private String[] getRoleNames(Role[] roles) {
        List<String> names = new ArrayList<String>();
        if (roles != null) {
            for (Role role : roles) {
                names.add(role.getName());
            }
        }
        return names.toArray(new String[names.size()]);
    }

    private boolean removeRole(String name, int type) {
        Role role = service.getRole(name);
        if (role != null && role.getType() == type) {
            return service.removeRole(name);
        } else {
            return false;
        }
    }
}
