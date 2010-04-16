/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.provisioning;

import org.osgi.jmx.service.provisioning.ProvisioningServiceMBean;
import org.osgilab.bundles.jmx.beans.AbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.TabularData;
import java.io.IOException;

/**
 * @author dmytro.pishchukhin
 */
public class ProvisioningService extends AbstractMBean implements ProvisioningServiceMBean {
    protected ProvisioningService() throws NotCompliantMBeanException {
        super(ProvisioningServiceMBean.class);
    }

    public void addInformationFromZip(String s) throws IOException {
        // todo
    }

    public void addInformation(TabularData tabularData) throws IOException {
        // todo
    }

    public TabularData listInformation() throws IOException {
        return null;  // todo
    }

    public void setInformation(TabularData tabularData) throws IOException {
        // todo
    }
}
