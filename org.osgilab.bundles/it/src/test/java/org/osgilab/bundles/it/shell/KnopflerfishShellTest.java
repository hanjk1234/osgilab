/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Knopflerfish Shell test
 *
 * @author dpishchukhin
 */
public class KnopflerfishShellTest extends AbstractShellTest {
    @Override
    public Option[] getShellSpecificConfiguration() {
        return options(
                // test framework tyte
                knopflerfish(),
                provision(
                        bundle("http://www.knopflerfish.org/snapshots/2.3.3.snapshot_trunk_2916/osgi/jars/console/console_all-2.0.1.jar"),
                        mavenBundle().groupId("org.osgilab.bundles.shell").artifactId("knopflerfish").version("1.0.0")
                )
        );
    }

    @Override
    public void testCommandsRegistration() {
        //todo
    }
}
