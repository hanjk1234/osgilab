package org.osgilab.bundles.jmx.beans;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * @author dpishchukhin
 */
public abstract class AbstractMBean extends StandardMBean {
    protected OsgiVisitor visitor;
    protected LogVisitor logVisitor;

    protected AbstractMBean(Class<?> mbeanInterface, OsgiVisitor visitor, LogVisitor logVisitor) throws NotCompliantMBeanException {
        super(mbeanInterface);
        this.visitor = visitor;
        this.logVisitor = logVisitor;
    }
}
