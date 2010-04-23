/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.it.shell;

import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Felix Shell test
 *
 * @author dpishchukhin
 */
public class FelixShellTest extends AbstractShellTest {
    @Override
    public Option[] getShellSpecificConfiguration() {
        return options(
                // test framework tyte
                felix(),
                provision(
                        mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.shell").version("1.2.0"),
                        mavenBundle().groupId("org.osgilab.bundles.shell").artifactId("felix").version("1.0.0")
                )
        );
    }

    @Override
    public void testCommandsRegistration() {
        //todo
    }
}
