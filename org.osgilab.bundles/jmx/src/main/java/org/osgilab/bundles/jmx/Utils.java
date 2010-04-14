/*
 * Copyright (c) 2010 Dmytro Pishchukhin (http://osgilab.org)
 * This program is made available under the terms of the MIT License.
 */

package org.osgilab.bundles.jmx;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.jmx.JmxConstants;
import org.osgi.service.packageadmin.ExportedPackage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author dmytro.pishchukhin
 */
public class Utils {
    public static String getValueType(Object value) {
        Class aClass = value.getClass();
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
        String type = getValueType(value);
        boolean isStringType = type.endsWith(JmxConstants.STRING);
        if (type.startsWith(JmxConstants.ARRAY_OF)) {
            int length = Array.getLength(value);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                Object itemValue = Array.get(value, i);
                builder.append(serializeToStringSimpleValue(itemValue, isStringType));
                if (i < length - 1) {
                    builder.append(",");
                }
            }
            return builder.toString();
        } else if (type.startsWith(JmxConstants.VECTOR_OF)) {
            Vector vector = (Vector) value;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < vector.size(); i++) {
                Object itemValue = vector.get(i);
                builder.append(serializeToStringSimpleValue(itemValue, isStringType));
                if (i < vector.size() - 1) {
                    builder.append(",");
                }
            }
            return builder.toString();
        }
        return serializeToStringSimpleValue(value, isStringType);
    }

    private static String serializeToStringSimpleValue(Object value, boolean stringType) {
        if (stringType) {
            StringBuilder builder = new StringBuilder();
            builder.append('"');
            builder.append(String.valueOf(value).trim().replace("\\", "\\\\").replaceAll("'", "\\'").replaceAll("\"", "\\\""));
            builder.append('"');
            return builder.toString();
        } else {
            return String.valueOf(value);
        }
    }

    public static long[] getIds(Bundle[] bundles) {
        if (bundles == null) {
            return new long[0];
        }
        long[] result = new long[bundles.length];
        for (int i = 0; i < bundles.length; i++) {
            result[i] = bundles[i].getBundleId();
        }
        return result;
    }

    public static long[] getIds(ServiceReference[] serviceReferences) {
        if (serviceReferences == null) {
            return new long[0];
        }
        long[] result = new long[serviceReferences.length];
        for (int i = 0; i < serviceReferences.length; i++) {
            result[i] = (Long)serviceReferences[i].getProperty(Constants.SERVICE_ID);
        }
        return result;
    }

    public static Long[] toLongArray(long[] longArray) {
        if (longArray == null) {
            return new Long[0];
        }
        Long[] result = new Long[longArray.length];
        for (int i = 0; i < longArray.length; i++) {
            result[i] = longArray[i];
        }
        return result;
    }

    public static ExportedPackage findPackage(ExportedPackage[] packages, String packageName, Version version) {
        if (packages != null) {
            for (ExportedPackage exportedPackage : packages) {
                if (exportedPackage.getName().equals(packageName) &&
                        exportedPackage.getVersion().equals(version)) {
                    return exportedPackage;
                }
            }
        }
        return null;
    }

    public static ExportedPackage[] findPackages(ExportedPackage[] packages, String packageName, Version version) {
        List<ExportedPackage> result = new ArrayList<ExportedPackage>();
        if (packages != null) {
            for (ExportedPackage exportedPackage : packages) {
                if (exportedPackage.getName().equals(packageName) &&
                        exportedPackage.getVersion().equals(version)) {
                    result.add(exportedPackage);
                }
            }
        }
        return result.toArray(new ExportedPackage[result.size()]);
    }
}
