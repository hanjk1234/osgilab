package org.knowhowlab.osgi.manual.tests.monitorable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.monitor.MonitorListener;
import org.osgi.service.monitor.Monitorable;
import org.osgi.service.monitor.StatusVariable;
import org.osgi.util.tracker.ServiceTracker;

import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Test <code>Monitorable</code> implementation
 *
 * @author dpishchukhin
 */
public class Activator implements BundleActivator, Monitorable {
    /**
     * <code>StatusVariable</code> ID
     */
    private static final String STATUS_VARIABLE_NAME = "test.sv";
    /**
     * Monitorable PID
     */
    private static final String MONITORABLE_PID = "test.monitorable";

    /**
     * <code>MonitorListener</code> tracker
     */
    private ServiceTracker monitorListenerTracker;

    /**
     * <code>StatusVariable</code> value
     */
    private int svValue = 0;
    /**
     * <code>StatusVariable</code>
     */
    private StatusVariable sv = new StatusVariable(STATUS_VARIABLE_NAME, StatusVariable.CM_SI, svValue);
    private ServiceRegistration registration;

    public void start(BundleContext bundleContext) throws Exception {
        monitorListenerTracker = new ServiceTracker(bundleContext, MonitorListener.class.getName(), null);
        monitorListenerTracker.open();

        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(Constants.SERVICE_PID, MONITORABLE_PID);
        props.put("org.knowhowlab.osgi.shell.group.id", "test.monitor");
        props.put("org.knowhowlab.osgi.shell.group.name", "test.monitor");
        props.put("org.knowhowlab.osgi.shell.commands", new String[][] {
                new String[] {"svinc", "svinc - Increment test StatusVariable"}
        });
        registration = bundleContext.registerService(Monitorable.class.getName(), this, props);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        registration.unregister();
        registration = null;

        monitorListenerTracker.close();
        monitorListenerTracker = null;
    }

    public void svinc(PrintWriter out, String[] args) {
        incrementValue();
    }

    public String[] getStatusVariableNames() {
        return new String[] {STATUS_VARIABLE_NAME};
    }

    public StatusVariable getStatusVariable(String id) throws IllegalArgumentException {
        if (!STATUS_VARIABLE_NAME.equals(id)) {
            throw new IllegalArgumentException("Unknown id: " + id);
        }
        return new StatusVariable(sv.getID(), sv.getCollectionMethod(), svValue);
    }

    public boolean notifiesOnChange(String id) throws IllegalArgumentException {
        if (!STATUS_VARIABLE_NAME.equals(id)) {
            throw new IllegalArgumentException("Unknown id: " + id);
        }
        return true;
    }

    public boolean resetStatusVariable(String id) throws IllegalArgumentException {
        if (!STATUS_VARIABLE_NAME.equals(id)) {
            throw new IllegalArgumentException("Unknown id: " + id);
        }
        svValue = 0;
        notifyValueChange();
        return true;
    }

    private void incrementValue() {
        svValue++;
        notifyValueChange();
    }

    private void notifyValueChange() {
        MonitorListener listener = (MonitorListener) monitorListenerTracker.getService();
        if (listener != null) {
            listener.updated(MONITORABLE_PID, new StatusVariable(sv.getID(), sv.getCollectionMethod(), svValue));
        }
    }

    public String getDescription(String id) throws IllegalArgumentException {
        if (!STATUS_VARIABLE_NAME.equals(id)) {
            throw new IllegalArgumentException("Unknown id: " + id);
        }
        return STATUS_VARIABLE_NAME;
    }
}
