/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Equinox Shell test
 *
 * @author dpishchukhin
 */
public class EquinoxShellTest extends AbstractShellTest {
    @Override
    public Option[] getShellSpecificConfiguration() {
        return options(
                // test framework tyte
                equinox(),
                mavenBundle().groupId("org.osgilab.bundles.shell").artifactId("equinox").version("1.0.0")
        );
    }

    @Override
    public void testCommandsRegistration() {
        //todo
    }
}
