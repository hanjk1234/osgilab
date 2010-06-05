/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgi.testing.it.commons.testbundle;

import org.osgi.testing.it.commons.testbundle.service.Echo;

/**
 * Echo service implementation
 *
 * @author dmytro.pishchukhin
 */
public class EchoImpl implements Echo {
    public String echo(String str) {
        return str; 
    }
}
