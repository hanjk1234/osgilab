/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

import org.junit.Assert;
import org.junit.Test;
import org.osgilab.bundles.monitoradmin.util.StatusVariablePathFilter;

/**
 * @author dmytro.pishchukhin
 */
public class StatusVariablePathFilterTest {
    @Test
    public void testParse() {
        StatusVariablePathFilter filter = new StatusVariablePathFilter("aaa.*/aaa.aaa.*");
        Assert.assertEquals("aaa.", filter.getMonitorableId());
        Assert.assertEquals("aaa.aaa.", filter.getStatusVariableId());

        filter = new StatusVariablePathFilter("*/*");
        Assert.assertEquals("", filter.getMonitorableId());
        Assert.assertEquals("", filter.getStatusVariableId());

        try {
            new StatusVariablePathFilter("aaa/aaa/aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            new StatusVariablePathFilter("/aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            new StatusVariablePathFilter("aaa*.aaa./aaa");
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
    }
}