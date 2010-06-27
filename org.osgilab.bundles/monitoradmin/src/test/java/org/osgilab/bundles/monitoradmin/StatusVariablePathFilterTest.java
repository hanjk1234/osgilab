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