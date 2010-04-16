/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx.service.provisioning;

import org.osgi.jmx.service.provisioning.ProvisioningServiceMBean;
import org.osgilab.bundles.jmx.Utils;
import org.osgilab.bundles.jmx.beans.ServiceAbstractMBean;

import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.TabularData;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipInputStream;

/**
 * @author dmytro.pishchukhin
 */
public class ProvisioningService extends ServiceAbstractMBean<org.osgi.service.provisioning.ProvisioningService>
        implements ProvisioningServiceMBean {

    public ProvisioningService() throws NotCompliantMBeanException {
        super(ProvisioningServiceMBean.class);
    }

    public void addInformationFromZip(String zipURL) throws IOException {
        service.addInformation(new ZipInputStream(new URL(zipURL).openStream()));
    }

    public void addInformation(TabularData tabularData) throws IOException {
        service.addInformation(Utils.convertToDictionary(tabularData, true));
    }

    public TabularData listInformation() throws IOException {
        return Utils.getProperties(service.getInformation());
    }

    public void setInformation(TabularData tabularData) throws IOException {
        service.setInformation(Utils.convertToDictionary(tabularData, true));
    }
}
