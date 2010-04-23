/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.junit.Assert;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Collection;
import java.util.Map;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Equinox Shell test
 *
 * @author dpishchukhin
 */
public class EquinoxShellTest extends AbstractShellTest {
    @Configuration
    @Override
    public Option[] getShellSpecificConfiguration() {
        return options(
                // test framework tyte
                equinox(),
                mavenBundle().groupId("org.osgilab.bundles.shell").artifactId("equinox").version("1.0.0")
        );
    }

    @Override
    public void testCommandsRegistration() throws Exception {
        Map<String, String> defaultCommansList = getDefaultCommansList();

        String groupName = "Custom Commands Group";
        ServiceRegistration registration = registerShellService("group1", groupName, defaultCommansList);
        ServiceReference[] commandReferences = bc.getServiceReferences("org.eclipse.osgi.framework.console.CommandProvider",
                null);
        Assert.assertNotNull(commandReferences);

        int foundCommands = 0;
        Collection<String> commandHelps = defaultCommansList.values();
        for (ServiceReference commandReference : commandReferences) {
            CommandProvider commandProvider = (CommandProvider) bc.getService(commandReference);
            if (commandProvider.getHelp().indexOf(groupName) != -1) {
                for (String commandHelp : commandHelps) {
                    if (commandProvider.getHelp().indexOf(commandHelp) != -1) {
                        foundCommands++;
                    }
                }
            }
        }
        Assert.assertEquals(defaultCommansList.size(), foundCommands);

        registration.unregister();

        foundCommands = 0;
        commandReferences = bc.getServiceReferences("org.eclipse.osgi.framework.console.CommandProvider", null);
        if (commandReferences != null) {
            for (ServiceReference commandReference : commandReferences) {
                CommandProvider commandProvider = (CommandProvider) bc.getService(commandReference);
                if (commandProvider.getHelp().indexOf(groupName) != -1) {
                    for (String commandHelp : commandHelps) {
                        if (commandProvider.getHelp().indexOf(commandHelp) != -1) {
                            foundCommands++;
                        }
                    }
                }
            }
        }
        Assert.assertEquals(0, foundCommands);
    }
}
