/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

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
                        mavenBundle().groupId("org.osgilab.bundles.shell").artifactId("felix").version("1.0.0").startLevel(1)
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
