package org.osgilab.bundles.jmx.beans;

import org.osgilab.bundles.jmx.OsgiVisitor;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * @author dpishchukhin
 */
public abstract class AbstractMBean extends StandardMBean {
    protected OsgiVisitor visitor;

    protected AbstractMBean(Class<?> mbeanInterface, OsgiVisitor visitor) throws NotCompliantMBeanException {
        super(mbeanInterface);
        this.visitor = visitor;
    }
}
