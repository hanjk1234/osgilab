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

package org.knowhowlab.osgi.monitoradmin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowhowlab.osgi.monitoradmin.mocks.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.monitor.MonitorAdmin;
import org.osgi.service.monitor.MonitorPermission;
import org.osgi.service.monitor.Monitorable;
import org.osgi.service.monitor.StatusVariable;

import java.security.Permission;
import java.util.HashMap;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdminImplSecurityTest {
    private MockOsgiVisitor osgiVisitor;
    private MockLogVisitor logVisitor;
    private MonitorAdminCommon common;

    @Before
    public void init() {
        osgiVisitor = new MockOsgiVisitor();
        logVisitor = new MockLogVisitor();
        common = new MonitorAdminCommon(osgiVisitor, logVisitor);
    }

    @After
    public void uninit() {
        if (common != null) {
            common.cancelAllJobs();
        }
    }

    private Bundle createMockBundle(Permission... permisions) {
        return new SecutiryMockBundle(permisions);
    }

    @Test
    public void testGetMonitorableNames_NoMonitorableAvailable() throws Exception {
        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle());
        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);
    }

    @Test
    public void testGetMonitorableNames_MonitorableAvailable_WithAllPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();
        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid2"),
                new MockMonitorable());
        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid3"),
                new MockMonitorable());
        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid1"),
                new MockMonitorable());
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle());

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(3, monitorableNames.length);
        Assert.assertEquals("com.acme.pid1", monitorableNames[0]);
        Assert.assertEquals("com.acme.pid2", monitorableNames[1]);
        Assert.assertEquals("com.acme.pid3", monitorableNames[2]);
    }

    @Test
    public void testGetMonitorableNames_MonitorableAvailable_WithMonitorPermission_for_all_sv() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();
        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid2"), new MockMonitorable());
        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid3"), new MockMonitorable());
        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid1"), new MockMonitorable());
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(new MonitorPermission("*/*", MonitorPermission.READ)));

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(3, monitorableNames.length);
        Assert.assertEquals("com.acme.pid1", monitorableNames[0]);
        Assert.assertEquals("com.acme.pid2", monitorableNames[1]);
        Assert.assertEquals("com.acme.pid3", monitorableNames[2]);
    }

    @Test
    public void testGetMonitorableNames_MonitorableAvailable_WithMonitorPermission_not_for_all_sv() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("com.acme.pid2/*", MonitorPermission.PUBLISH)),
                "com.acme.pid2"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));

        map.put(new MonitorableMockServiceReference(createMockBundle(),
                "com.acme.pid3"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));

        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid1"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(new MonitorPermission("*/*", MonitorPermission.READ)));

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(2, monitorableNames.length);
        Assert.assertEquals("com.acme.pid1", monitorableNames[0]);
        Assert.assertEquals("com.acme.pid2", monitorableNames[1]);
    }

    @Test
    public void testGetMonitorableNames_MonitorableAvailable_WithMonitorPermission_not_for_all_sv2() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("com.acme.pid2/*", MonitorPermission.PUBLISH)),
                "com.acme.pid2"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        map.put(new MonitorableMockServiceReference(createMockBundle(),
                "com.acme.pid3"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid1"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(new MonitorPermission("com.acme.pid2/*", MonitorPermission.READ)));

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(1, monitorableNames.length);
        Assert.assertEquals("com.acme.pid2", monitorableNames[0]);
    }

    @Test
    public void testGetMonitorableNames_MonitorableAvailable_WithMonitorPermission_not_for_all_sv3() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid2.very.long.monitorable.id"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        map.put(new MonitorableMockServiceReference(createMockBundle(),
                "com.acme.pid3"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid1"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(new MonitorPermission("com.acme.pid2/*", MonitorPermission.READ)));

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);
    }

}
