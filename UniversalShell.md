## Introduction ##

OSGi Universal Shell Adapters.

## Usage ##

There are two simple steps that should be done by bundle developer to add a new shell command:

1. Add commands definitions to class. Every command is defined as public method with such signature:
```
public void <command_name>(java.io.PrintWriter out, java.lang.String[] args);
```
  * out - command output stream
  * args - command arguments list

2.a. Register Services in BundleContext with definition:
  * service property **"org.knowhowlab.osgi.shell.group.id"** (**mandatory**, java.lang.String) - unique commands group id. Used by some framework specific shell API that support command groups;
  * service property **"org.knowhowlab.osgi.shell.group.name"** (**mandatory**, java.lang.String) - commands group name. Used by some framework specific shell API that support command groups;
  * service property **"org.knowhowlab.osgi.shell.commands"** (**mandatory**, java.lang.String[.md](.md)[.md](.md)) - commands definition "command\_name" -> "command\_help";
  * service object class - could be used any value. It's very useful when commands are included into interface implementation.

2.b. Register Service with Declarative Service description:
  * service property **"org.knowhowlab.osgi.shell.group.id"** (**mandatory**, java.lang.String) - unique commands group id. Used by some framework specific shell API that support command groups;
  * service property **"org.knowhowlab.osgi.shell.group.name"** (**mandatory**, java.lang.String) - commands group name. Used by some framework specific shell API that support command groups;
  * service property **"org.knowhowlab.osgi.shell.commands"** (**mandatory**, java.lang.String[.md](.md)) - commands definition "command\_name#command\_help";
  * service object class - could be used any value. It's very useful when commands are included into interface implementation.


Here is a sample of new commands:

1. Commands implementation
```
public class ShellTestService {
................
    public void bndinfo(PrintWriter out, String... args) {
        if (args == null || args.length != 1) {
            out.println("Bundle id argument is missed");
            return;
        }
        try {
            int bundleId = Integer.parseInt(args[0]);
            Bundle bundle = bc.getBundle(bundleId);
            if (bundle == null) {
                out.println("Bundle id is invalid: " + bundleId);
                return;
            }
            printBundleInfo(bundle, out);
        } catch (NumberFormatException e) {
            out.println("Bundle id has wrong format: " + args[0]);
        }
    }

    public void bndsinfo(PrintWriter out, String... args) {
        Bundle[] bundles = bc.getBundles();
        for (Bundle bundle : bundles) {
            printBundleInfo(bundle, out);
        }
    }
......................
}
```

2.a. Command service registration.
```
ShellTestService shellTestService = ....;
Dictionary<String, Object> props = new Hashtable<String, Object>();
props.put("org.knowhowlab.osgi.shell.group.id", "test_group_id");
props.put("org.knowhowlab.osgi.shell.group.name", "Test commands");
String[][] commandsArray = new String[2][2];
commandsArray[0] = new String[]{"bndinfo", "bndinfo <bundleId> - Print information for bundle with <bundleId>"};
commandsArray[1] = new String[]{"bndsinfo", "bndsinfo - Print information for all bundles"};
props.put("org.knowhowlab.osgi.shell.commands", commandsArray);
bc.registerService(ShellTestService.class.getName(), shellTestService, props);
```

2.b. Command service registration with DS.
```
<?xml version="1.0" encoding="UTF-8"?>
<component name="shell_test.component">
    <implementation class="...ShellTestService"/>

    <service>
        <provide interface="...ShellTestService"/>
    </service>

    <property name="org.knowhowlab.osgi.shell.group.id" type="String" value="test_group_id"/>
    <property name="org.knowhowlab.osgi.shell.group.name" type="String" value="Test commands"/>
    <property name="org.knowhowlab.osgi.shell.commands" type="String">
        bndinfo#bndinfo - Print information for bundle with bundleId
        bndsinfo#bndsinfo - Print information for all bundles
    </property>
...
</component>
```

## Details ##

Current version: **1.1.0** (20110518)

Equinox Adapter download: [equinox-1.1.0.jar](http://osgilab.googlecode.com/files/equinox-1.1.0.jar)

Equinox Adapter Sources download: [equinox-1.1.0-sources.jar](http://osgilab.googlecode.com/files/equinox-1.1.0-sources.jar)

Equinox Adapter Sources browse: [Here](http://code.google.com/p/osgilab/source/browse/tags/equinox-1.1.0)

Equinox Adapter Maven Artifact:
```
<dependency>
    <groupId>org.knowhowlab.osgi.shell</groupId>
    <artifactId>equinox</artifactId>
    <version>1.1.0</version>
</dependency>
```

Felix Adapter download: [felix-1.1.0.jar](http://osgilab.googlecode.com/files/felix-1.1.0.jar)

Felix Adapter Sources download: [felix-1.1.0-sources.jar](http://osgilab.googlecode.com/files/felix-1.1.0-sources.jar)

Felix Adapter Sources browse: [Here](http://code.google.com/p/osgilab/source/browse/tags/felix-1.1.0)

Felix Adapter Maven Artifact:
```
<dependency>
    <groupId>org.knowhowlab.osgi.shell</groupId>
    <artifactId>felix</artifactId>
    <version>1.1.0</version>
</dependency>
```

Felix GoGo Adapter download: [felix-gogo-1.1.0.jar](http://osgilab.googlecode.com/files/felix-gogo-1.1.0.jar)

Felix GoGo Adapter Sources download: [felix-gogo-1.1.0-sources.jar](http://osgilab.googlecode.com/files/felix-gogo-1.1.0-sources.jar)

Felix Adapter Sources browse: [Here](http://code.google.com/p/osgilab/source/browse/tags/felix-gogo-1.1.0)

Felix Adapter Maven Artifact:
```
<dependency>
    <groupId>org.knowhowlab.osgi.shell</groupId>
    <artifactId>felix-gogo</artifactId>
    <version>1.1.0</version>
</dependency>
```

Knopflerfish Adapter download: [knopflerfish-1.1.0.jar](http://osgilab.googlecode.com/files/knopflerfish-1.1.0.jar)

Knopflerfish Adapter Sources download: [knopflerfish-1.1.0-sources.jar](http://osgilab.googlecode.com/files/knopflerfish-1.1.0-sources.jar)

Knopflerfish Adapter Sources browse: [Here](http://code.google.com/p/osgilab/source/browse/tags/knopflerfish-1.1.0)

Knopflerfish Adapter Maven Artifact:
```
<dependency>
    <groupId>org.knowhowlab.osgi.shell</groupId>
    <artifactId>knopflerfish</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Changes ##
**1.1.0**
Implemented functionality:
  * Added support of defining commands services with Declarative Services

**1.0.1**
Implemented functionality:
  * Changed package to org.knowhowlab.osgi.shell
  * Changed maven groupId to org.knowhowlab.osgi.shell
  * Added Apache Felix GoGo Universal Shell Adapter

**1.0.0**
Implemented functionality:
  * Eclipse Equinox Universal Shell Adapter
  * Apache Felix Universal Shell Adapter
  * Knopflerfish Universal Shell Adapter

> ## Roadmap ##
  * add support of RFC147 as soon as it is released for public review