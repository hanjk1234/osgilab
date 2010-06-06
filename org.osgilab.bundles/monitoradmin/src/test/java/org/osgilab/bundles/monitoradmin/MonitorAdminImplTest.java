/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.monitor.*;
import org.osgilab.bundles.monitoradmin.mocks.MockLogVisitor;
import org.osgilab.bundles.monitoradmin.mocks.MockMonitorable;
import org.osgilab.bundles.monitoradmin.mocks.MockOsgiVisitor;
import org.osgilab.bundles.monitoradmin.mocks.MonitorableMockServiceReference;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author dmytro.pishchukhin
 */
public class MonitorAdminImplTest {
    @Test
    public void testGetMonitorableNames_NoMonitorableAvailable() throws Exception {
        MonitorAdmin monitorAdmin = new MonitorAdminImpl(new MockOsgiVisitor(), new MockLogVisitor());
        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(0, monitorableNames.length);
    }

    @Test
    public void testGetMonitorableNames_MonitorableAvailable() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();
        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();
        map.put(new MonitorableMockServiceReference("com.acme.pid2"), new MockMonitorable());
        map.put(new MonitorableMockServiceReference("com.acme.pid3"), new MockMonitorable());
        map.put(new MonitorableMockServiceReference("com.acme.pid1"), new MockMonitorable());
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        String[] monitorableNames = monitorAdmin.getMonitorableNames();
        Assert.assertNotNull(monitorableNames);
        Assert.assertEquals(3, monitorableNames.length);
        Assert.assertEquals("com.acme.pid1", monitorableNames[0]);
        Assert.assertEquals("com.acme.pid2", monitorableNames[1]);
        Assert.assertEquals("com.acme.pid3", monitorableNames[2]);
    }

    @Test
    public void testGetStatusVariable() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {new StatusVariable("sv.id", StatusVariable.CM_CC, 0)};
        monitorable.setStatusVariables(statusVariables);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        try {
            monitorAdmin.getStatusVariable(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariable("/&%(/=");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariable("com.aaa/sv.id");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariable("com.acme.pid/sv.id_u");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        StatusVariable sv = monitorAdmin.getStatusVariable("com.acme.pid/sv.id");
        Assert.assertNotNull(sv);
        Assert.assertEquals("sv.id", sv.getID());
        Assert.assertEquals(StatusVariable.CM_CC, sv.getCollectionMethod());
        Assert.assertEquals(StatusVariable.TYPE_INTEGER, sv.getType());
        Assert.assertEquals(0, sv.getInteger());

    }

    @Test
    public void testGetDescription() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {new StatusVariable("sv.id", StatusVariable.CM_CC, 0)};
        monitorable.setStatusVariables(statusVariables);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        try {
            monitorAdmin.getDescription(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getDescription("/&%(/=");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getDescription("com.aaa/sv.id");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getDescription("com.acme.pid/sv.id_u");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        String description = monitorAdmin.getDescription("com.acme.pid/sv.id");
        Assert.assertNotNull(description);
        Assert.assertEquals("sv.id", description);
    }

    @Test
    public void testGetStatusVariables() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        try {
            monitorAdmin.getStatusVariables(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariables("/&%(/=");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariables("com.aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        StatusVariable[] variables = monitorAdmin.getStatusVariables("com.acme.pid");
        Assert.assertNotNull(variables);
        Assert.assertEquals(2, variables.length);
        Assert.assertTrue("sv.id1".equals(variables[0].getID()));
        Assert.assertTrue("sv.id2".equals(variables[1].getID()));
    }

    @Test
    public void testGetStatusVariableNames() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        try {
            monitorAdmin.getStatusVariableNames(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariableNames("/&%(/=");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.getStatusVariableNames("com.aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        String[] names = monitorAdmin.getStatusVariableNames("com.acme.pid");
        Assert.assertNotNull(names);
        Assert.assertEquals(2, names.length);
        Assert.assertTrue("sv.id1".equals(names[0]));
        Assert.assertTrue("sv.id2".equals(names[1]));
    }

    @Test
    public void testGetRunningJobs() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);
        monitorable.setNotificationSupport("sv.id1", true);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        MonitoringJob[] jobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(0, jobs.length);

        MonitoringJob job = monitorAdmin.startJob("initiator", new String[]{"com.acme.pid/sv.id1"}, 1);

        jobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(1, jobs.length);
        Assert.assertTrue(jobs[0].isRunning());

        job.stop();

        jobs = monitorAdmin.getRunningJobs();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(0, jobs.length);
    }

    @Test
    public void testResetStatusVariable() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);
        monitorable.setNotificationSupport("sv.id1", true);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        StatusVariable sv = monitorAdmin.getStatusVariable("com.acme.pid/sv.id1");
        Assert.assertNotNull(sv);
        Assert.assertEquals(0, sv.getInteger());

        monitorable.setNewStatusVariableValue("sv.id1", "15");

        sv = monitorAdmin.getStatusVariable("com.acme.pid/sv.id1");
        Assert.assertNotNull(sv);
        Assert.assertEquals(15, sv.getInteger());

        try {
            monitorAdmin.resetStatusVariable(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.resetStatusVariable("/&%(/=");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.resetStatusVariable("com.aaa/sv.id");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.resetStatusVariable("com.acme.pid/sv.id_u");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        boolean result = monitorAdmin.resetStatusVariable("com.acme.pid/sv.id1");
        Assert.assertTrue(result);

        sv = monitorAdmin.getStatusVariable("com.acme.pid/sv.id1");
        Assert.assertNotNull(sv);
        Assert.assertEquals(0, sv.getInteger());
    }

    @Test
    public void testSwitchEvents() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
        };
        monitorable.setStatusVariables(statusVariables);
        monitorable.setNotificationSupport("sv.id1", true);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        try {
            monitorAdmin.switchEvents(null, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            monitorAdmin.switchEvents("/&%(/=", true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            monitorAdmin.switchEvents("com.aaa/sv.id1", true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            monitorAdmin.switchEvents("com.acme.pid/sv.id_u", true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            monitorAdmin.switchEvents("*", true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            monitorAdmin.switchEvents("**", true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            monitorAdmin.switchEvents("**/*", true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        monitorAdmin.switchEvents("com.acme.pid/sv.id1", false);

        String[] paths = ((MonitorAdminImpl) monitorAdmin).getDisabledNotificationPaths();
        Assert.assertNotNull(paths);
        Assert.assertEquals(1, paths.length);
        Assert.assertEquals("com.acme.pid/sv.id1", paths[0]);

        monitorAdmin.switchEvents("com.acme.pid/sv.id1", true);
        paths = ((MonitorAdminImpl) monitorAdmin).getDisabledNotificationPaths();
        Assert.assertEquals(0, paths.length);

        monitorAdmin.switchEvents("*/sv.id1", false);
        paths = ((MonitorAdminImpl) monitorAdmin).getDisabledNotificationPaths();
        Assert.assertEquals(1, paths.length);
        Assert.assertEquals("com.acme.pid/sv.id1", paths[0]);

        monitorAdmin.switchEvents("*/*", true);
        paths = ((MonitorAdminImpl) monitorAdmin).getDisabledNotificationPaths();
        Assert.assertEquals(0, paths.length);
    }

    @Test
    public void testSwitchEventsWithNotification() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);
        monitorable.setNotificationSupport("sv.id1", true);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        monitorable.setListener((MonitorListener) monitorAdmin);
        monitorable.setMonitorableId("com.acme.pid");

        monitorable.setNewStatusVariableValue("sv.id1", "15");

        Event[] events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(1, events.length);
        Assert.assertEquals(ConstantsMonitorAdmin.TOPIC, events[0].getTopic());
        Assert.assertEquals("com.acme.pid", events[0].getProperty(ConstantsMonitorAdmin.MON_MONITORABLE_PID));
        Assert.assertEquals("sv.id1", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME));
        Assert.assertEquals("15", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE));
        Assert.assertNull(events[0].getProperty(ConstantsMonitorAdmin.MON_LISTENER_ID));

        monitorAdmin.switchEvents("com.acme.pid/sv.id1", false);
        osgiVisitor.cleanPostedEvents();

        monitorable.setNewStatusVariableValue("sv.id1", "25");
        events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(0, events.length);

        monitorAdmin.switchEvents("com.acme.pid/sv.id1", true);
        monitorable.setNewStatusVariableValue("sv.id1", "25");
        events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(1, events.length);
        Assert.assertEquals(ConstantsMonitorAdmin.TOPIC, events[0].getTopic());
        Assert.assertEquals("com.acme.pid", events[0].getProperty(ConstantsMonitorAdmin.MON_MONITORABLE_PID));
        Assert.assertEquals("sv.id1", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME));
        Assert.assertEquals("25", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE));
        Assert.assertNull(events[0].getProperty(ConstantsMonitorAdmin.MON_LISTENER_ID));
    }

    @Test
    public void testStartJob() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);
        monitorable.setNotificationSupport("sv.id1", true);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        monitorable.setListener((MonitorListener) monitorAdmin);
        monitorable.setMonitorableId("com.acme.pid");

        MonitoringJob job = monitorAdmin.startJob("init1", new String[]{"com.acme.pid/sv.id1"}, 1);

        MonitoringJob[] jobs = monitorAdmin.getRunningJobs();
        Assert.assertEquals(1, jobs.length);

        monitorable.setNewStatusVariableValue("sv.id1", "15");

        Event[] events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(2, events.length);
        Assert.assertEquals(ConstantsMonitorAdmin.TOPIC, events[0].getTopic());
        Assert.assertEquals("com.acme.pid", events[0].getProperty(ConstantsMonitorAdmin.MON_MONITORABLE_PID));
        Assert.assertEquals("sv.id1", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME));
        Assert.assertEquals("15", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE));
        Assert.assertNull(events[0].getProperty(ConstantsMonitorAdmin.MON_LISTENER_ID));

        Assert.assertEquals(ConstantsMonitorAdmin.TOPIC, events[1].getTopic());
        Assert.assertEquals("com.acme.pid", events[1].getProperty(ConstantsMonitorAdmin.MON_MONITORABLE_PID));
        Assert.assertEquals("sv.id1", events[1].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME));
        Assert.assertEquals("15", events[1].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE));
        Assert.assertEquals("init1", events[1].getProperty(ConstantsMonitorAdmin.MON_LISTENER_ID));

        job.stop();
        jobs = monitorAdmin.getRunningJobs();
        Assert.assertEquals(0, jobs.length);

        osgiVisitor.cleanPostedEvents();

        monitorable.setNewStatusVariableValue("sv.id1", "25");

        events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(1, events.length);
        Assert.assertEquals(ConstantsMonitorAdmin.TOPIC, events[0].getTopic());
        Assert.assertEquals("com.acme.pid", events[0].getProperty(ConstantsMonitorAdmin.MON_MONITORABLE_PID));
        Assert.assertEquals("sv.id1", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME));
        Assert.assertEquals("25", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE));
        Assert.assertNull(events[0].getProperty(ConstantsMonitorAdmin.MON_LISTENER_ID));
    }

    @Test
    public void testStartScheduledJob() throws Exception {
        MockOsgiVisitor osgiVisitor = new MockOsgiVisitor();

        HashMap<ServiceReference, Monitorable> map = new HashMap<ServiceReference, Monitorable>();

        MockMonitorable monitorable = new MockMonitorable();

        StatusVariable[] statusVariables = {
                new StatusVariable("sv.id1", StatusVariable.CM_CC, 0),
                new StatusVariable("sv.id2", StatusVariable.CM_CC, "test")
        };
        monitorable.setStatusVariables(statusVariables);

        map.put(new MonitorableMockServiceReference("com.acme.pid"), monitorable);
        osgiVisitor.setReferences(map);

        MonitorAdmin monitorAdmin = new MonitorAdminImpl(osgiVisitor, new MockLogVisitor());

        monitorable.setListener((MonitorListener) monitorAdmin);
        monitorable.setMonitorableId("com.acme.pid");

        monitorable.setNewStatusVariableValue("sv.id1", "15");

        MonitoringJob job = monitorAdmin.startScheduledJob("init1", new String[]{"com.acme.pid/sv.id1"}, 5, 0);

        MonitoringJob[] jobs = monitorAdmin.getRunningJobs();
        Assert.assertEquals(1, jobs.length);

        TimeUnit.SECONDS.sleep(7);

        Event[] events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(2, events.length);
        Assert.assertEquals(ConstantsMonitorAdmin.TOPIC, events[0].getTopic());
        Assert.assertEquals("com.acme.pid", events[0].getProperty(ConstantsMonitorAdmin.MON_MONITORABLE_PID));
        Assert.assertEquals("sv.id1", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_NAME));
        Assert.assertEquals("15", events[0].getProperty(ConstantsMonitorAdmin.MON_STATUSVARIABLE_VALUE));
        Assert.assertEquals("init1", events[0].getProperty(ConstantsMonitorAdmin.MON_LISTENER_ID));

        job.stop();
        jobs = monitorAdmin.getRunningJobs();
        Assert.assertEquals(0, jobs.length);

        osgiVisitor.cleanPostedEvents();

        monitorable.setNewStatusVariableValue("sv.id1", "25");

        events = osgiVisitor.getPostedEvents();
        Assert.assertEquals(0, events.length);
    }
}
