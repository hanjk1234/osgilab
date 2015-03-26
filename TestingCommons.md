## Introduction ##

OSGi specific assertions and utility classes that help to write OSGi integration/system tests.

## Usage ##

There is a comparison of the same test with and without OSGiLab testing assertions and utils.

With
```
@Test
public void test_With_OSGiAssertions() throws BundleException, InvalidSyntaxException {
    // asserts that test bundle is installed
    assertBundleAvailable("org.osgilab.testing.it.commons.test.bundle");
    // asserts that test bundle is resolved
    assertBundleState(Bundle.RESOLVED, "org.osgilab.testing.it.commons.test.bundle");
    // gets bundle instance
    Bundle bundle = findBundle(bc, "org.osgilab.testing.it.commons.test.bundle");
    // asserts that test service is unavailable
    assertServiceUnavailable("org.osgi.testing.it.commons.testbundle.service.Echo");
    // start bundle
    bundle.start();
    // asserts that test bundle is active
    assertBundleState(Bundle.ACTIVE, "org.osgilab.testing.it.commons.test.bundle");
    // asserts that test service is available within 2 seconds
    assertServiceAvailable("org.osgi.testing.it.commons.testbundle.service.Echo", 2, TimeUnit.SECONDS);
    // asserts that test service with custom properties is available
    assertServiceAvailable(and(create(Echo.class), eq("testkey", "testvalue")));
    // gets service by class and filter
    Echo echo = ServiceUtils.getService(bc, Echo.class, eq("testkey", "testvalue"));
    // asserts service method call
    Assert.assertEquals("test", echo.echo("test"));
    // stops bundle
    bundle.stop();
    // asserts that test bundle is resolved
    assertBundleState(Bundle.RESOLVED, "org.osgilab.testing.it.commons.test.bundle");
    // asserts that test service is unregistered
    assertServiceUnavailable(Echo.class);
}
```

Without:
```
@Test
public void test_Without_OSGiAssertions() throws BundleException, InterruptedException, InvalidSyntaxException {
    ServiceTracker packageAdminTracker = new ServiceTracker(bc, PackageAdmin.class.getName(), null);
    packageAdminTracker.open();
    PackageAdmin packageAdmin = (PackageAdmin) packageAdminTracker.getService();
    Assert.assertNotNull(packageAdmin);
    packageAdminTracker.close();
    Bundle[] bundles = packageAdmin.getBundles("org.osgilab.testing.it.commons.test.bundle", null);
    // asserts that test bundle is installed
    Assert.assertNotNull(bundles);
    Assert.assertTrue(bundles.length > 0);
    // gets bundle instance
    Bundle bundle = bundles[0];
    // asserts that test bundle is resolved
    Assert.assertEquals(Bundle.RESOLVED, bundle.getState());
    ServiceTracker serviceTracker1 = new ServiceTracker(bc, "org.osgi.testing.it.commons.testbundle.service.Echo", null);
    serviceTracker1.open();
    Assert.assertEquals(0, serviceTracker1.size());
    // start bundle
    bundle.start();
    // asserts that test bundle is active
    Assert.assertEquals(Bundle.ACTIVE, bundle.getState());
    // asserts that test service is available within 2 seconds
    Assert.assertNotNull(serviceTracker1.waitForService(2000));
    // asserts that test service with custom properties is available
    ServiceTracker serviceTracker2 = new ServiceTracker(bc, FrameworkUtil.createFilter(
            "(&(" + Constants.OBJECTCLASS + "=org.osgi.testing.it.commons.testbundle.service.Echo)" +
                    "(testkey=testvalue))"), null);
    serviceTracker2.open();
    Assert.assertTrue(serviceTracker2.size() > 0);
    // gets service by class and filter
    Echo echo = (Echo) serviceTracker2.getService();
    // asserts service method call
    Assert.assertEquals("test", echo.echo("test"));
    // stops bundle
    bundle.stop();
    // asserts that test bundle is resolved
    Assert.assertEquals(Bundle.RESOLVED, bundle.getState());
    // asserts that test service is unregistered
    Assert.assertEquals(0, serviceTracker1.size());
}
```

As you can see with OSGi assertions and utils you can concentrate on your functinality testing without any low-level OSGi API calls.

## Details ##

Current version: **1.0.0** (20100606)

Bundle download: [testing.commons-1.0.0.jar](http://osgilab.googlecode.com/files/testing.commons-1.0.0.jar)

Sources download: [testing.commons-1.0.0-sources.jar](http://osgilab.googlecode.com/files/testing.commons-1.0.0-sources.jar)

Javadoc download: [testing.commons-1.0.0-javadoc.jar](http://osgilab.googlecode.com/files/testing.commons-1.0.0-javadoc.jar)

Sources browse: [Here](http://osgilab.googlecode.com/svn/tags/testing.commons-1.0.0)

Javadoc browse: [Here](http://osgilab.googlecode.com/svn/tags/testing.commons-1.0.0/javadoc/index.html)

Maven Artifact:
```
<dependency>
    <groupId>org.osgilab.testing</groupId>
    <artifactId>commons</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Changes ##
**1.0.0**
Implemented functionality:
  * org.osgilab.testing.commons.assertions.BundleAssert - bundle assertions
  * org.osgilab.testing.commons.assertions.FilterAssert - filter assertions
  * org.osgilab.testing.commons.assertions.ServiceAssert - service assertions
  * org.osgilab.testing.commons.utils.BundleUtils - bundle utils that help to find bundle in OSGi Framework
  * org.osgilab.testing.commons.utils.FilterUtils - filter utils that help to create OSGi filters
  * org.osgilab.testing.commons.utils.ServiceUtils - server utils that help to find services and ServiceReferences in OSGi registry

## Pax Exam integration ##
To integrate OSGi assertions into your Pax Exam tests you have to do:
  * add dependency to your testing module
```
<dependency>
    <groupId>org.osgilab.testing</groupId>
    <artifactId>commons</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```
  * install OSGiLab Testing Commons bundle into your testing environment
```
@Configuration
public static Option[] osgiAssertsConfig() {
    return options(
            // list of bundles that should be installed
            provision(
                    // mandatory bundle
                    mavenBundle().groupId("org.osgilab.testing").artifactId("commons").version("1.0.0"),
                    // optional bundle
                    mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.0.1")
            )
    );
}
```
  * initialize assertions with OSGi BundleContext instance
```
/**
 * Injected BundleContext
 */
@Inject
protected BundleContext bc;

/**
 * Init OSGiAssert with BundleContext
 */
@Before
public void initOSGiAssert() {
    OSGiAssert.init(bc);
}
```

**Sample**: http://osgilab.googlecode.com/svn/trunk/org.osgilab.testing/it/it.commons/it.commons.pax.exam/

## Spring OSGi integration ##
To integrate OSGi assertions into your Spring OSGi tests you have to do:
  * add dependencies to your testing module
```
<dependency>
    <groupId>org.osgilab.testing</groupId>
    <artifactId>commons</artifactId>
    <version>1.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.osgi</groupId>
            <artifactId>org.osgi.core</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.osgi</groupId>
            <artifactId>org.osgi.compendium</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.junit</groupId>
    <artifactId>com.springsource.junit</artifactId>
    <version>3.8.2</version>
    <scope>test</scope>
</dependency>
```
  * install OSGiLab Testing Commons bundle into your testing environment
```
protected String[] getTestBundlesNames() {
    return new String[]{"org.junit, com.springsource.junit, 3.8.2",
            "org.osgilab.testing, commons, 1.0.0"};
}
```
  * initialize assertions with OSGi BundleContext instance
```
@Override
protected void onSetUp() throws Exception {
    OSGiAssert.init(bundleContext);
}
```

**Sample**: http://osgilab.googlecode.com/svn/trunk/org.osgilab.testing/it/it.commons/it.commons.spring.osgi/

## Roadmap ##
  * add support of OSGi listeners and events.