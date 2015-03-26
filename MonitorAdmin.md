## Introduction ##

OSGi MonitorAdmin Implementation.

## Details ##

Specification reference: [OSGi Service Platform Service Compendium 4.2 - 119 Monitor Admin Service Specification](http://www.osgi.org/Download/File?url=/download/r4v42/r4.cmpn.pdf).

API JavaDoc reference: [Here](http://www.osgi.org/javadoc/r4v42/org/osgi/service/monitor/package-summary.html).


Current version: **1.0.2** (20100728)

Bundle download: [monitoradmin-1.0.2.jar](http://osgilab.googlecode.com/files/monitoradmin-1.0.2.jar)

Sources download: [monitoradmin-1.0.2-sources.jar](http://osgilab.googlecode.com/files/monitoradmin-1.0.2-sources.jar)

Sources browse: [Here](http://code.google.com/p/osgilab/source/browse/tags/monitoradmin-1.0.2)

Maven Artifact:
```
<dependency>
    <groupId>org.knowhowlab.osgi</groupId>
    <artifactId>monitoradmin</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Changes ##
**1.0.2**
  * change POM packaging to JAR
  * embed org.osgi.service.monitor package
  * refactor package to org.knowhowlab.osgi.monitoradmin
  * change groupId to org.knowhowlab.osgi
  * change license to Apache License 2.0
  * add support of security permissions
  * add local permissions file

**1.0.1**
  * recompile with J2SE5
  * use OSGi specification 4.1.0 dependencies

**1.0.0**
Implemented functionality:
  * String getDescription(String path)
  * String[.md](.md) getMonitorableNames()
  * MonitoringJob[.md](.md) getRunningJobs()
  * StatusVariable getStatusVariable(String path)
  * String[.md](.md) getStatusVariableNames(String monitorableId)
  * StatusVariable[.md](.md) getStatusVariables(String monitorableId)
  * boolean resetStatusVariable(String path)
  * MonitoringJob startJob(String initiator, String[.md](.md) statusVariables, int count)
  * MonitoringJob startScheduledJob(String initiator, String[.md](.md) statusVariables, int schedule, int count)
  * void switchEvents(String path, boolean on)
  * StatusVariable notification events

Not implemented functionality:
  * Remote Jobs
  * Device Management Tree support

## Dependencies ##
  * OSGi EventAdmin Service
  * OSGi LogService

## Logging ##
  * OSGi LogService if available, otherwise - java.util.logging