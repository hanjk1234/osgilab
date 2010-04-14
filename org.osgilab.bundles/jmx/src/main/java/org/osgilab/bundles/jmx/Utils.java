/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.jmx.JmxConstants;

import java.util.Vector;

/**
 * @author dmytro.pishchukhin
 */
public class Utils {
    public static String getValueType(Object value) {
        Class<? extends Object> aClass = value.getClass();
        StringBuilder result = new StringBuilder();

        if (value instanceof Vector) {
            result.append(JmxConstants.VECTOR_OF);
            Vector vector = (Vector) value;
            if (vector.size() > 0) {
                Object item = vector.get(0);
                String simpleName = item.getClass().getSimpleName();
                if (JmxConstants.SCALAR.contains(simpleName)) {
                    result.append(simpleName);
                } else {
                    throw new IllegalArgumentException("Unknown type of vector item: " + item.getClass().getName());
                }
            } else {
                result.append(JmxConstants.STRING); // if vector is empty - String is default Scalar type
            }
        } else if (aClass.isArray()) {
            result.append(JmxConstants.ARRAY_OF);
            Class<?> arrayType = aClass.getComponentType();
            String simpleName = arrayType.getSimpleName();
            if (JmxConstants.SCALAR.contains(simpleName) || JmxConstants.P_BOOLEAN.equals(simpleName)) {
                result.append(simpleName);
            } else {
                throw new IllegalArgumentException("Unknown array type: " + arrayType.getName());
            }
        } else {
            String simpleName = aClass.getSimpleName();
            if (JmxConstants.SCALAR.contains(simpleName)) {
                result.append(simpleName);
            } else {
                throw new IllegalArgumentException("Unknown type value: " + aClass.getName());
            }
        }
        return result.toString();
    }

    public static String serializeToString(Object value) {
        return null;  // todo
    }
}
