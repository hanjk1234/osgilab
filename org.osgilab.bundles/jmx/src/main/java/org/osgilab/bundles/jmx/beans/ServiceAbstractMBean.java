package org.osgilab.bundles.jmx.beans;

import javax.management.NotCompliantMBeanException;

/**
 * @author dpishchukhin
 */
public abstract class ServiceAbstractMBean<T> extends AbstractMBean {
    protected T service;

    protected ServiceAbstractMBean(Class<?> mbeanInterface) throws NotCompliantMBeanException {
        super(mbeanInterface);
    }

    public void setService(T service) {
        this.service = service;
    }

    @Override
    public void uninit() {
        service = null;
        super.uninit();
    }
}
