## Introduction ##

OSGi JMX Management Model Implementation.

## Details ##

Specification reference: [OSGi Service Platform Enterprise Specification 4.2 - 124 JMX Management Model Specification](http://www.osgi.org/Download/File?url=/download/r4v42/r4.enterprise.pdf).

API JavaDoc reference: [Here](http://www.osgi.org/javadoc/r4v42/org/osgi/jmx/package-summary.html).


Current version: **1.0.2** (20100709)

Bundle download: [jmx-1.0.2.jar](http://osgilab.googlecode.com/files/jmx-1.0.2.jar)

Sources download: [jmx-1.0.2-sources.jar](http://osgilab.googlecode.com/files/jmx-1.0.2-sources.jar)

Javadoc download: [jmx-1.0.2-javadoc.jar](http://osgilab.googlecode.com/files/jmx-1.0.2-javadoc.jar)

Sources browse: [Here](http://code.google.com/p/osgilab/source/browse/tags/jmx-1.0.2)

Maven Artifact:
```
<dependency>
    <groupId>org.knowhowlab.osgi</groupId>
    <artifactId>jmx</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Changes ##
**1.0.2**
  * refactor package to org.knowhowlab.osgi.jmx
  * change groupId to org.knowhowlab.osgi
  * change license to Apache License 2.0
  * add support of MonitorAdmin event to notify through JMX
  * handle errors on bundle start and unregister registered MBeans
  * embed org.osgi.jmx package

**1.0.1**
  * fix NoClassDefFoundException for Compendium service
  * fix NPEs

**1.0.0**
Implemented functionality:
  * org.osgi.jmx.framework.BundleStateMBean
  * org.osgi.jmx.framework.FrameworkMBean
  * org.osgi.jmx.framework.PackageStateMBean
  * org.osgi.jmx.framework.ServiceStateMBean
  * org.osgi.jmx.service.cm.ConfigurationAdminMBean
  * org.osgi.jmx.service.permissionadmin.PermissionAdminMBean
  * org.osgi.jmx.service.provisioning.ProvisioningServiceMBean
  * org.osgi.jmx.service.useradmin.UserAdminMBean
Optional implemented functionality:
  * [org.osgilab.bundles.jmx.service.monitor.MonitorAdminMBean](http://osgilab.googlecode.com/svn/tags/jmx-1.0.2/javadoc/org/knowhowlab/osgi/jmx/service/monitor/MonitorAdminMBean.html) - MonitorAdmin JMX bean

Logging: OSGi LogService if available, otherwise - java.util.logging

System properties:
  * org.knowhowlab.osgi.jmx.beans.service.monitor.all.events = true - notify all MonitorAdmin events including jobs events

## Roadmap ##
  * redesign to add support of new/existing Compendium Service MBeans as separate bundles