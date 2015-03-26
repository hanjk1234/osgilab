The idea of the project to provide OSGi tools and implementation of OSGi APIs that are not available (e.g. [MonitorAdmin](MonitorAdmin.md), [JMX](JMX.md)) in the other open-source projects and some experiments with OSGi core API.

## News ##
  * **2012-10-25**: OSGi [Testing Commons](TestingCommons.md) were moved to [GitHub](https://github.com/dpishchukhin/org.knowhowlab.osgi.testing)
  * **2012-10-01**: OSGi [JMX](JMX.md) was donated to [OPS4J](https://github.com/ops4j/org.ops4j.pax.jmx)
  * **2012-10-01**: OSGi [MonitorAdmin](MonitorAdmin.md) was donated to [OPS4J](https://github.com/ops4j/org.ops4j.pax.monitoradmin)

  * [Old news](News.md)

## OSGi Bundles ##

|Name|Version|Specification|Description|Sources|JavaDoc|Maven Artifact|
|:---|:------|:------------|:----------|:------|:------|:-------------|
|[MonitorAdmin](MonitorAdmin.md)|1.0.2 (20100728)|[PDF](http://www.osgi.org/Download/File?url=/download/r4v42/r4.cmpn.pdf)/[API](http://www.osgi.org/javadoc/r4v42/org/osgi/service/monitor/package-summary.html)|MonitorAdmin implementation bundle|[JAR](http://osgilab.googlecode.com/files/monitoradmin-1.0.2-sources.jar)/[SVN](http://osgilab.googlecode.com/svn/tags/monitoradmin-1.0.2)|none|[org.knowhowlab.osgi:monitoradmin:1.0.2](http://repo2.maven.org/maven2/org/knowhowlab/osgi/monitoradmin/1.0.2/)|
|[JMX Model](JMX.md)|1.0.2 (20100709)|[PDF](http://www.osgi.org/Download/File?url=/download/r4v42/r4.enterprise.pdf)/[API](http://www.osgi.org/javadoc/r4v42/org/osgi/jmx/package-summary.html)|OSGi Enterprise JMX Management Model Implementation bundle|[JAR](http://osgilab.googlecode.com/files/jmx-1.0.2-sources.jar)/[SVN](http://code.google.com/p/osgilab/source/browse/tags/jmx-1.0.2)|[Here](http://osgilab.googlecode.com/svn/tags/jmx-1.0.2/javadoc/index.html)|[org.knowhowlab.osgi:jmx:1.0.2](http://repo2.maven.org/maven2/org/knowhowlab/osgi/jmx/1.0.2/)|
|[Equinox Shell Adapter](UniversalShell.md)|1.1.0 (20110518)|none|OSGi Universal shell Equinox adapter bundle|[JAR](http://osgilab.googlecode.com/files/equinox-1.1.0-sources.jar)/[SVN](http://osgilab.googlecode.com/svn/tags/equinox-1.1.0/)|none|[org.knowhowlab.osgi.shell:equinox:1.1.0](http://repo2.maven.org/maven2/org/knowhowlab/osgi/shell/equinox/1.1.0/)|
|[Felix Shell Adapter](UniversalShell.md)|1.1.0 (20110518)|none|OSGi Universal shell Felix adapter bundle|[JAR](http://osgilab.googlecode.com/files/felix-1.1.0-sources.jar)/[SVN](http://osgilab.googlecode.com/svn/tags/felix-1.1.0/)|none|[org.knowhowlab.osgi.shell:felix:1.1.0](http://repo2.maven.org/maven2/org/knowhowlab/osgi/shell/felix/1.1.0/)|
|[Felix GoGo Shell Adapter](UniversalShell.md)|1.1.0 (20110518)|none|OSGi Universal shell Felix GoGo adapter bundle|[JAR](http://osgilab.googlecode.com/files/felix-gogo-1.1.0-sources.jar)/[SVN](http://osgilab.googlecode.com/svn/tags/shell-felix-gogo-1.1.0/)|none|[org.knowhowlab.osgi.shell:felix-gogo:1.1.0](http://repo2.maven.org/maven2/org/knowhowlab/osgi/shell/felix-gogo/1.1.0/)|
|[Knopflerfish Shell Adapter](UniversalShell.md)|1.1.0 (20110518)|none|OSGi Universal shell Knopflerfish adapter bundle|[JAR](http://osgilab.googlecode.com/files/knopflerfish-1.1.0-sources.jar)/[SVN](http://osgilab.googlecode.com/svn/tags/shell-knopflerfish-1.1.0/)|none|[org.knowhowlab.osgi.shell:knopflerfish:1.1.0](http://repo2.maven.org/maven2/org/knowhowlab/osgi/shell/knopflerfish/1.1.0/)|

## OSGi Testing (useful tools and utilities to test OSGi specific code) ##

|Name|Version|Description|Sources|JavaDoc|Maven Artifact|
|:---|:------|:----------|:------|:------|:-------------|
|[Commons](TestingCommons.md)|1.0.0 (20100606)|OSGi specific assertions and utility classes that help to write OSGi integration/system tests|[JAR](http://osgilab.googlecode.com/files/testing.commons-1.0.0-sources.jar)/[SVN](http://osgilab.googlecode.com/svn/tags/testing.commons-1.0.0)|[JAR](http://osgilab.googlecode.com/files/testing.commons-1.0.0-javadoc.jar)/[Browse](http://osgilab.googlecode.com/svn/tags/testing.commons-1.0.0/javadoc/index.html)|[org.osgilab.testing:commons:1.0.0](http://repo2.maven.org/maven2/org/osgilab/testing/commons/1.0.0/)|

## Tips & Tricks ##
|Name|Description|Blog post|Sources|
|:---|:----------|:--------|:------|
|OSGi Profiling|How to profile OSGi bundles with Java profile tools|[Blog](http://blog.knowhowlab.org/2010/03/osgi-tips-osgi-profiling-yourkit.html)|[Sources](http://code.google.com/p/osgilab/source/browse/trunk/org.osgilab.tips/profile/)|
|OSGi Logging|How to forward JUL to OSGi and vice versa|[Post 1](http://blog.knowhowlab.org/2010/03/osgi-tips-osgi-logging-forward-osgi.html)/[Post 2](http://blog.knowhowlab.org/2010/03/osgi-tips-logging-jul-to-osgi-forward.html)|[Sources 1](http://code.google.com/p/osgilab/source/browse/trunk/org.osgilab.tips/logs/osgi2jul/)/[Sources 2](http://code.google.com/p/osgilab/source/browse/trunk/org.osgilab.tips/logs/jul2osgi/)|
|OSGi Activation|How to activate code in OSGi bundle|[Post](http://www.dzone.com/links/r/osgi_tutorial_4_ways_to_activate_code_in_osgi_bun.html)|[Sources](http://code.google.com/p/osgilab/source/browse/trunk/org.osgilab.tips/activation)/[Sources ZIP](http://osgilab.googlecode.com/files/osgi-activation-samples.zip)|

## Experiments ##
|Description|Blog post|Sources|Binaries|
|:----------|:--------|:------|:-------|
|How to replace standard Equinox shell with Apache Felix GoGo.|[Here](http://blog.knowhowlab.org/2010/06/how-to-replace-standard-equinox-shell.html)|[Here](http://code.google.com/p/osgilab/downloads/detail?name=equinox-gogo-adapter-1.0.0-sources.jar)|[Here](http://code.google.com/p/osgilab/downloads/detail?name=equinox-gogo-adapter-1.0.0.jar)|


**<sup>*</sup>"OSGi" trademark belongs to OSGi Alliance.**