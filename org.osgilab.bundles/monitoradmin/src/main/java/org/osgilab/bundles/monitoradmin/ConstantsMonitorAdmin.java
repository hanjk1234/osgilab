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
     * <code>MonitorAdmin</code> events topic
     */
    public final static String TOPIC = "org/osgi/service/monitor";
    /**
     * <code>Monitorable</code> ID
     */
    public final static String MON_MONITORABLE_PID = "mon.monitorable.pid";
    /**
     * <code>StatusVariable</code> name
     */
    public final static String MON_STATUSVARIABLE_NAME = "mon.statusvariable.name";
    /**
     * <code>StatusVariable</code> value
     */
    public final static String MON_STATUSVARIABLE_VALUE = "mon.statusvariable.value";
    /**
     * Initiator
     */
    public final static String MON_LISTENER_ID = "mon.listener.id";
}
