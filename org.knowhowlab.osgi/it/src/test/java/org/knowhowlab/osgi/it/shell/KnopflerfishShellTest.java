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

package org.knowhowlab.osgi.it.shell;

import org.junit.Assert;
import org.knopflerfish.service.console.CommandGroup;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Collection;
import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Knopflerfish Shell test
 *
 * @author dpishchukhin
 */
public class KnopflerfishShellTest extends AbstractShellTest {
    @Configuration
    @Override
    public Option[] getShellSpecificConfiguration() {
        return options(
                // test framework tyte
                knopflerfish().version("2.3.3"), knopflerfish().version("3.0.0"),
                provision(
                        bundle("http://www.knopflerfish.org/releases/2.3.3/osgi/jars/console/console_all-2.0.1.jar"),
                        mavenBundle().groupId("org.knowhowlab.osgi.shell").artifactId("knopflerfish").version("1.0.2-SNAPSHOT")
                )
        );
    }

    @Override
    public void testCommandsRegistration() throws Exception {
        Map<String, String> defaultCommansList = getDefaultCommansList();

        String groupId = "group1";
        String groupName = "Custom Commands Group";

        ServiceRegistration registration = registerShellService(groupId, groupName, defaultCommansList);
        ServiceReference[] commandReferences = bc.getServiceReferences("org.knopflerfish.service.console.CommandGroup",
                "(" + CommandGroup.GROUP_NAME + "=" + groupId + ")");
        Assert.assertNotNull(commandReferences);
        Assert.assertEquals(1, commandReferences.length);

        CommandGroup commandGroup = (CommandGroup) bc.getService(commandReferences[0]);
        Assert.assertEquals(groupName, commandGroup.getShortHelp());
        String longHelp = commandGroup.getLongHelp();
        Collection<String> commandHelps = defaultCommansList.values();

        int foundCommands = 0;
        for (String commandHelp : commandHelps) {
            if (longHelp.indexOf(commandHelp) != -1) {
                foundCommands++;
            }
        }
        Assert.assertEquals(2, foundCommands);

        registration.unregister();

        commandReferences = bc.getServiceReferences("org.knopflerfish.service.console.CommandGroup",
                "(" + CommandGroup.GROUP_NAME + "=" + groupId + ")");
        Assert.assertNull(commandReferences);
    }
}
