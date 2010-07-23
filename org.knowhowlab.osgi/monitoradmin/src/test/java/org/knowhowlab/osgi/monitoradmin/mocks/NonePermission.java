package org.knowhowlab.osgi.monitoradmin.mocks;

import java.security.Permission;

/**
 * @author dpishchukhin
 */
public class NonePermission extends Permission {
    public static final NonePermission INSTANCE = new NonePermission();

    public NonePermission() {
        super("<no permissions>");
    }

    @Override
    public boolean implies(Permission permission) {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NonePermission;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String getActions() {
        return "<none actions>";
    }
}
