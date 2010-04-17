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

import javax.management.openmbean.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Utils class
 *
 * @author dmytro.pishchukhin
 */
public class Utils {
    /**
     * Get type by value
     *
     * @param value object value
     * @return one of value {@link JmxConstants#TYPE_ITEM}
     * @throws IllegalArgumentException if type is out of defined scope
     */
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

    /**
     * Serialize object value to string
     *
     * @param value object value
     * @return string for object value (see 124.5.6 for more details)
     */
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

    /**
     * Convert Bundles array to ids array
     *
     * @param bundles bundles array
     * @return non-<code>null</code> bundle ids array
     */
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

    /**
     * Convert ServiceReferences array to ids array
     *
     * @param serviceReferences bundles array
     * @return non-<code>null</code> ServiceReference ids array
     */
    public static long[] getIds(ServiceReference[] serviceReferences) {
        if (serviceReferences == null) {
            return new long[0];
        }
        long[] result = new long[serviceReferences.length];
        for (int i = 0; i < serviceReferences.length; i++) {
            result[i] = (Long) serviceReferences[i].getProperty(Constants.SERVICE_ID);
        }
        return result;
    }

    /**
     * Convert primitive long array to Long array
     *
     * @param longArray primitive long array
     * @return non-<code>null</code> Long array
     */
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

    /**
     * Find the first <code>ExportedPackage</code> by name and version
     * @param packages <code>ExportedPackage</code>s array
     * @param packageName package name
     * @param version package version
     * @return package with name and version, otherwise - <code>null</code>
     */
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

    /**
     * Find <code>ExportedPackage</code>s by name and version
     * @param packages <code>ExportedPackage</code>s array
     * @param packageName package name
     * @param version package version
     * @return non-<code>null</code> array with <code>ExportedPackage</code>s with name and version
     */
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

    /**
     * Deserialized to String value
     *
     * @param value string value
     * @param type one of value {@link JmxConstants#TYPE_ITEM}
     * @return deserialized object
     * @throws IllegalArgumentException if type is out of defined scope or unable to deserialize string
     */
    public static Object deserializeFromString(String value, String type) {
        return null; // todo
    }

    /**
     * Convert TabularData to properties
     *
     * @param properties tabular data
     * @param ignoreErrors ignore errors flag
     * @return <code>Dictionary</code> with properties
     * @throws IllegalArgumentException if <code>ignoreErrors</code> is <code>false</code> and unable to convert value from String by type
     */
    public static Dictionary convertToDictionary(TabularData properties, boolean ignoreErrors) {
        Hashtable<String, Object> props = new Hashtable<String, Object>();
        if (properties != null) {
            Collection<CompositeData> values = (Collection<CompositeData>) properties.values();
            for (CompositeData value : values) {
                try {
                    String key = (String) value.get(JmxConstants.KEY);
                    String type = (String) value.get(JmxConstants.TYPE);
                    String val = (String) value.get(JmxConstants.VALUE);
                    props.put(key, deserializeFromString(val, type));
                } catch (IllegalArgumentException e) {
                    if (!ignoreErrors) {
                        throw e;
                    }
                }
            }

        }
        return props;
    }

    /**
     * Convert Properties to TabularData
     *
     * @param properties properties
     * @return <code>TabularData</code> with properties
     * @throws IllegalArgumentException if property type is out of defined scope
     */
    public static TabularData getProperties(Dictionary properties) {
        TabularDataSupport dataSupport = new TabularDataSupport(JmxConstants.PROPERTIES_TYPE);
        try {
            if (properties != null) {
                Enumeration keys = properties.keys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Map<String, Object> values = new HashMap<String, Object>();
                    values.put(JmxConstants.KEY, key);
                    Object value = properties.get(key);
                    values.put(JmxConstants.VALUE, serializeToString(value));
                    values.put(JmxConstants.TYPE, getValueType(value));
                    dataSupport.put(new CompositeDataSupport(JmxConstants.PROPERTY_TYPE, values));
                }
            }
        } catch (OpenDataException e) {
            e.printStackTrace();
        }
        return dataSupport;
    }
}
