/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.knowhowlab.osgi.testing.test.conditions;

import org.knowhowlab.osgi.testing.test.conditions.bundle.BundleEventCondition;
import org.knowhowlab.osgi.testing.test.conditions.bundle.BundleStateCondition;
import org.knowhowlab.osgi.testing.test.conditions.event.EventCondition;
import org.knowhowlab.osgi.testing.test.conditions.service.ServiceEventCondition;
import org.knowhowlab.osgi.testing.test.conditions.service.ServiceStateCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dmytro.pishchukhin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ComplexConditions {
    BundleEventCondition[] bundleEventConditions() default {};

    BundleStateCondition[] bundleStateConditions() default {};

    ServiceEventCondition[] serviceEventConditions() default {};

    ServiceStateCondition[] serviceStateConditions() default {};

    EventCondition[] eventConditions() default {};
}
