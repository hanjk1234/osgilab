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

import java.security.AllPermission;
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
        if (permisions == null || permisions.length == 0) {
            return new SecutiryMockBundle(new AllPermission());
        } else {
            return new SecutiryMockBundle(permisions);
        }
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

        map.put(new MonitorableMockServiceReference(createMockBundle(NonePermission.INSTANCE),
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

        map.put(new MonitorableMockServiceReference(createMockBundle(NonePermission.INSTANCE),
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

        map.put(new MonitorableMockServiceReference(createMockBundle(NonePermission.INSTANCE),
                "com.acme.pid3"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));

        map.put(new MonitorableMockServiceReference(createMockBundle(new MonitorPermission("*/*", MonitorPermission.PUBLISH)),
                "com.acme.pid1"), new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0)));
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(new MonitorPermission("com.acme.pid2/*", MonitorPermission.READ)));

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);
    }


    @Test
    public void testGetStatusVariable_WithAllPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle());

        StatusVariable sv = monitorAdmin.getStatusVariable("com.acme.pid/sv.id");
        Assert.assertNotNull(sv);
        Assert.assertEquals("sv.id", sv.getID());
        Assert.assertEquals(StatusVariable.CM_CC, sv.getCollectionMethod());
        Assert.assertEquals(StatusVariable.TYPE_INTEGER, sv.getType());
        Assert.assertEquals(0, sv.getInteger());
    }

    @Test
    public void testGetStatusVariable_WithMonitorPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.PUBLISH)),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common,
                createMockBundle(new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.READ)));

        StatusVariable sv = monitorAdmin.getStatusVariable("com.acme.pid/sv.id");
        Assert.assertNotNull(sv);
        Assert.assertEquals("sv.id", sv.getID());
        Assert.assertEquals(StatusVariable.CM_CC, sv.getCollectionMethod());
        Assert.assertEquals(StatusVariable.TYPE_INTEGER, sv.getType());
        Assert.assertEquals(0, sv.getInteger());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusVariable_WithMonitorPermissions_noPublishPermission() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(NonePermission.INSTANCE),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common,
                createMockBundle(new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.READ)));

        monitorAdmin.getStatusVariable("com.acme.pid/sv.id");
    }

    @Test(expected = SecurityException.class)
    public void testGetStatusVariable_WithMonitorPermissions_noReadPermission() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.PUBLISH)),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(NonePermission.INSTANCE));

        monitorAdmin.getStatusVariable("com.acme.pid/sv.id");
    }

    @Test
    public void testGetDescription_WithAllPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle());

        String description = monitorAdmin.getDescription("com.acme.pid/sv.id");
        Assert.assertNotNull(description);
        Assert.assertEquals("sv.id", description);
    }

    @Test
    public void testGetDescription_WithMonitorPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.PUBLISH)),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.READ)
        ));

        String description = monitorAdmin.getDescription("com.acme.pid/sv.id");
        Assert.assertNotNull(description);
        Assert.assertEquals("sv.id", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDescription_NoPublishPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(NonePermission.INSTANCE),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.READ)
        ));

        monitorAdmin.getDescription("com.acme.pid/sv.id");
    }

    @Test(expected = SecurityException.class)
    public void testGetDescription_NoReadPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(new StatusVariable("sv.id", StatusVariable.CM_CC, 0));

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id", MonitorPermission.PUBLISH)),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(NonePermission.INSTANCE));

        monitorAdmin.getDescription("com.acme.pid/sv.id");
    }

    @Test
    public void testGetStatusVariableNames_WithAllPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle());

        String[] names = monitorAdmin.getStatusVariableNames("com.acme.pid");
        Assert.assertNotNull(names);
        Assert.assertEquals(2, names.length);
        Assert.assertTrue("sv.id1".equals(names[0]));
        Assert.assertTrue("sv.id2".equals(names[1]));
    }

    @Test
    public void testGetStatusVariableNames_WithMonitorPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.PUBLISH)
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.READ)
        ));

        String[] names = monitorAdmin.getStatusVariableNames("com.acme.pid");
        Assert.assertNotNull(names);
        Assert.assertEquals(2, names.length);
        Assert.assertTrue("sv.id1".equals(names[0]));
        Assert.assertTrue("sv.id2".equals(names[1]));
    }

    @Test
    public void testGetStatusVariableNames_NoPublishPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(NonePermission.INSTANCE),
                "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.READ)
        ));

        String[] names = monitorAdmin.getStatusVariableNames("com.acme.pid");
        Assert.assertNotNull(names);
        Assert.assertEquals(0, names.length);
    }

    @Test
    public void testGetStatusVariableNames_NoReadPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.PUBLISH)
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                NonePermission.INSTANCE));

        String[] names = monitorAdmin.getStatusVariableNames("com.acme.pid");
        Assert.assertNotNull(names);
        Assert.assertEquals(0, names.length);
    }

    @Test
    public void testGetStatusVariableNames_WithPartialPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test"),
                new StatusVariable("sv.id3", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id1", MonitorPermission.PUBLISH),
                new MonitorPermission("com.acme.pid/sv.id2", MonitorPermission.PUBLISH)
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id2", MonitorPermission.READ),
                new MonitorPermission("com.acme.pid/sv.id3", MonitorPermission.READ)
        ));

        String[] names = monitorAdmin.getStatusVariableNames("com.acme.pid");
        Assert.assertNotNull(names);
        Assert.assertEquals(1, names.length);
        Assert.assertTrue("sv.id2".equals(names[0]));
    }

    @Test
    public void testGetStatusVariables_WithAllPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle());

        StatusVariable[] variables = monitorAdmin.getStatusVariables("com.acme.pid");
        Assert.assertNotNull(variables);
        Assert.assertEquals(2, variables.length);
        Assert.assertTrue("sv.id1".equals(variables[0].getID()));
        Assert.assertTrue("sv.id2".equals(variables[1].getID()));
    }

    @Test
    public void testGetStatusVariables_WithMonitorPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.PUBLISH)
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.READ)
        ));

        StatusVariable[] variables = monitorAdmin.getStatusVariables("com.acme.pid");
        Assert.assertNotNull(variables);
        Assert.assertEquals(2, variables.length);
        Assert.assertTrue("sv.id1".equals(variables[0].getID()));
        Assert.assertTrue("sv.id2".equals(variables[1].getID()));
    }

    @Test
    public void testGetStatusVariables_NoPublishPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                NonePermission.INSTANCE
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.READ)
        ));

        StatusVariable[] variables = monitorAdmin.getStatusVariables("com.acme.pid");
        Assert.assertNotNull(variables);
        Assert.assertEquals(0, variables.length);
    }

    @Test
    public void testGetStatusVariables_NoReadPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/*", MonitorPermission.PUBLISH)
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                NonePermission.INSTANCE
        ));

        StatusVariable[] variables = monitorAdmin.getStatusVariables("com.acme.pid");
        Assert.assertNotNull(variables);
        Assert.assertEquals(0, variables.length);
    }

    @Test
    public void testGetStatusVariables_WithPartialPermissions() throws Exception {
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable(
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test"),
                new StatusVariable("sv.id3", StatusVariable.CM_CC, "test")
        );

        map.put(new MonitorableMockServiceReference(createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id1", MonitorPermission.PUBLISH),
                new MonitorPermission("com.acme.pid/sv.id2", MonitorPermission.PUBLISH)
        ), "com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(logVisitor, common, createMockBundle(
                new MonitorPermission("com.acme.pid/sv.id2", MonitorPermission.READ),
                new MonitorPermission("com.acme.pid/sv.id3", MonitorPermission.READ)
        ));

        StatusVariable[] variables = monitorAdmin.getStatusVariables("com.acme.pid");
        Assert.assertNotNull(variables);
        Assert.assertEquals(1, variables.length);
        Assert.assertTrue("sv.id2".equals(variables[0].getID()));
    }
}
