/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

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
                knopflerfish(),
                provision(
                        bundle("http://www.knopflerfish.org/releases/2.3.3/osgi/jars/console/console_all-2.0.1.jar"),
                        mavenBundle().groupId("org.osgilab.bundles.shell").artifactId("knopflerfish").version("1.0.0")
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
