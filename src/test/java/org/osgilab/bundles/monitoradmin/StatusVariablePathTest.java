/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.junit.Assert;
import org.junit.Test;
import org.osgilab.bundles.monitoradmin.util.StatusVariablePath;

/**
 * @author dmytro.pishchukhin
 */
public class StatusVariablePathTest {
    @Test
    public void testParse() {
        StatusVariablePath path = new StatusVariablePath("aaa/aaa");
        Assert.assertEquals("aaa", path.getMonitorableId());
        Assert.assertEquals("aaa", path.getStatusVariableId());

        try {
            new StatusVariablePath("aaa/aaa/aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            new StatusVariablePath("/aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            new StatusVariablePath("aaa.aaa./aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
