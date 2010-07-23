package org.knowhowlab.osgi.monitoradmin.mocks;

import org.osgi.framework.BundleContext;
import org.springframework.osgi.mock.MockBundle;

import java.security.Permission;
import java.util.Dictionary;

/**
 * @author dpishchukhin
 */
public class SecutiryMockBundle extends MockBundle {
    private Permission[] permissions;

    public SecutiryMockBundle() {
    }

    public SecutiryMockBundle(Permission... permissions) {
        this.permissions = permissions;
    }

    public SecutiryMockBundle(Dictionary headers) {
        super(headers);
    }

    public SecutiryMockBundle(BundleContext context) {
        super(context);
    }

    public SecutiryMockBundle(String symName) {
        super(symName);
    }

    public SecutiryMockBundle(String symName, Dictionary headers, BundleContext context) {
        super(symName, headers, context);
    }

    @Override
    public boolean hasPermission(Object permission) {
        if (permissions != null) {
            for (Permission perm : permissions) {
                if (perm.implies((Permission) permission)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
