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

import org.apache.felix.shell.Command;
import org.junit.Assert;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Map;
import java.util.Set;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Felix Shell test
 *
 * @author dpishchukhin
 */
public class FelixShellTest extends AbstractShellTest {
    @Configuration
    @Override
    public Option[] getShellSpecificConfiguration() {
        return options(
                // test framework tyte
                felix(),
                provision(
                        mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.shell").version("1.2.0").startLevel(1),
                        mavenBundle().groupId("org.knowhowlab.osgi.shell").artifactId("felix").version("1.0.1-SNAPSHOT").startLevel(1)
                )
        );
    }

    @Override
    public void testCommandsRegistration() throws Exception {
        Map<String, String> defaultCommansList = getDefaultCommansList();

        ServiceRegistration registration = registerShellService("group1", "Custom Commands Group", defaultCommansList);
        ServiceReference[] commandReferences = bc.getServiceReferences("org.apache.felix.shell.Command", null);
        Assert.assertNotNull(commandReferences);

        int foundCommands = 0;
        Set<String> names = defaultCommansList.keySet();
        for (ServiceReference commandReference : commandReferences) {
            Command command = (Command) bc.getService(commandReference);
            if (names.contains(command.getName())) {
                foundCommands++;
            }
        }
        Assert.assertEquals(defaultCommansList.size(), foundCommands);

        registration.unregister();

        foundCommands = 0;
        commandReferences = bc.getServiceReferences("org.apache.felix.shell.Command", null);
        if (commandReferences != null) {
            for (ServiceReference commandReference : commandReferences) {
                Command command = (Command) bc.getService(commandReference);
                if (names.contains(command.getName())) {
                    foundCommands++;
                }
            }
        }
        Assert.assertEquals(0, foundCommands);
    }
}
