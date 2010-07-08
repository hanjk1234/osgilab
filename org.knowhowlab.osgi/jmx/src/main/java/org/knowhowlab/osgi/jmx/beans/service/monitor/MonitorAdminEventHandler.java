package org.knowhowlab.osgi.jmx.beans.service.monitor;

import org.knowhowlab.osgi.jmx.beans.LogVisitor;
import org.knowhowlab.osgi.jmx.beans.OsgiVisitor;
import org.knowhowlab.osgi.jmx.service.monitor.MonitorAdminMBean;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.openmbean.CompositeDataSupport;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * MonitorAdmin events handler
 *
 * @author dmytro.pishchukhin
 */
public class MonitorAdminEventHandler implements EventHandler {
    /**
     * System property that enables all MonitorAdmin events notification
     */
    public static final String MONITOR_ADMIN_ALL_EVENTS_NOTIFICATION_PROPS = "org.knowhowlab.osgi.jmx.beans.service.monitor.all.events";
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

    private int sequenceNumber = 0;
    private OsgiVisitor visitor;
    private LogVisitor logVisitor;
    private NotificationBroadcasterSupport nbs;

    public MonitorAdminEventHandler(OsgiVisitor visitor, LogVisitor logVisitor, NotificationBroadcasterSupport nbs) {
        this.visitor = visitor;
        this.logVisitor = logVisitor;
        this.nbs = nbs;
    }

    public Dictionary<String, String> getHandlerProperties() {
        boolean allEvents = Boolean.valueOf(visitor.getProperty(MONITOR_ADMIN_ALL_EVENTS_NOTIFICATION_PROPS));
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put(EventConstants.EVENT_TOPIC, TOPIC);
        if (allEvents) {
            props.put(EventConstants.EVENT_FILTER, String.format("(%s=*)", MON_LISTENER_ID));
        } else {
            props.put(EventConstants.EVENT_FILTER, String.format("(!(%s=*))", MON_LISTENER_ID));
        }
        return props;
    }

    public void handleEvent(Event event) {
        Notification notification = new Notification(MonitorAdminMBean.EVENT, this, ++sequenceNumber,
                System.currentTimeMillis());

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(MonitorAdminMBean.MONITORABLE_PID, event.getProperty(MON_MONITORABLE_PID));
            values.put(MonitorAdminMBean.STATUS_VARIABLE_NAME, event.getProperty(MON_STATUSVARIABLE_NAME));
            values.put(MonitorAdminMBean.STATUS_VARIABLE_NAME, event.getProperty(MON_STATUSVARIABLE_VALUE));
            Object initiator = event.getProperty(MON_LISTENER_ID);
            values.put(MonitorAdminMBean.EVENT_INITIATOR, (initiator != null) ? initiator : "");
            notification.setUserData(new CompositeDataSupport(MonitorAdminMBean.EVENT_TYPE, values));

            nbs.sendNotification(notification);
        } catch (Exception e) {
            logVisitor.warning("Unable to send BundleEvent notification", e);
        }
    }
}
