package org.osgilab.bundles.jmx.beans;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * Abstract MBean
 * 
 * @author dpishchukhin
 */
public abstract class AbstractMBean extends StandardMBean {
    protected OsgiVisitor visitor;
    protected LogVisitor logVisitor;

    protected AbstractMBean(Class<?> mbeanInterface) throws NotCompliantMBeanException {
        super(mbeanInterface);
    }

    public void setVisitor(OsgiVisitor visitor) {
        this.visitor = visitor;
    }

    public void setLogVisitor(LogVisitor logVisitor) {
        this.logVisitor = logVisitor;
    }

    public void uninit() {
        visitor = null;
        logVisitor = null;
    }
}
