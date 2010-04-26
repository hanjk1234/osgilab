/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.monitoradmin;

/**
 * Constants
 *
 * @author dmytro.pishchukhin
 */
public interface ConstantsMonitorAdmin {
    /**
     * MonitorAdmin events topic
     */
    public final static String TOPIC = "org/osgi/service/monitor";
    /**
     * Monitorable ID
     */
    public final static String MON_MONITORABLE_PID = "mon.monitorable.pid";
    /**
     * StatusVariable name
     */
    public final static String MON_STATUSVARIABLE_NAME = "mon.statusvariable.name";
    /**
     * StatusVariable value
     */
    public final static String MON_STATUSVARIABLE_VALUE = "mon.statusvariable.value";
    /**
     * Initiator
     */
    public final static String MON_LISTENER_ID = "mon.listener.id";
}
