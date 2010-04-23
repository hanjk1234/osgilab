/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.*;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.cleanCaches;

/**
 * Abstract Shell test
 *
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public abstract class AbstractShellTest {
    // OSGi framework test configutation
    @Configuration
    public Option[] configuration() {
        return options(
                cleanCaches(),
                mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.0.1").startLevel(1)
        );
    }

    public abstract Option[] getShellSpecificConfiguration();

    // injected BundleContext
    @Inject
    protected BundleContext bc;

    protected ShellTestService shellTestService;

    @Before
    public void init() {
        // initialize test service
        shellTestService = new ShellTestService(bc);
    }

    /**
     * Register test service
     *
     * @param groupId   id
     * @param groupName name
     * @param commands  commands (name => help)
     * @return registration
     */
    protected ServiceRegistration registerShellService(String groupId, String groupName, Map<String, String> commands) {
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        if (groupId != null) {
            props.put("org.osgilab.tips.shell.group.id", groupId);
        }
        if (groupName != null) {
            props.put("org.osgilab.tips.shell.group.name", groupName);
        }
        if (commands != null && commands.size() > 0) {
            String[][] commandsArray = new String[commands.size()][2];
            Iterator<String> iterator = commands.keySet().iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                String name = iterator.next();
                commandsArray[i] = new String[]{name, commands.get(name)};
            }
            props.put("org.osgilab.tips.shell.commands", commandsArray);
        }

        return bc.registerService(ShellTestService.class.getName(), shellTestService, props);
    }

    /**
     * Get default map of commands
     *
     * @return commands map
     */
    protected Map<String, String> getDefaultCommansList() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("bndsinfo", "bndsinfo - Print information for all bundles");
        result.put("bndinfo", "bndinfo <bundleId> - Print information for bundle with <bundleId>");
        return result;
    }

    @Test
    public abstract void testCommandsRegistration() throws Exception;

}
