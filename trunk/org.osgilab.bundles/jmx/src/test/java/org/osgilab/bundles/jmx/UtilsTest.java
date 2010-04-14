package org.osgilab.bundles.jmx;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.jmx.JmxConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author dpishchukhin
 */
public class UtilsTest {
    @Test
    public void testGetValueType() throws Exception {
        Assert.assertEquals(JmxConstants.BIGDECIMAL, Utils.getValueType(new BigDecimal(1)));
        Assert.assertEquals(JmxConstants.BIGINTEGER, Utils.getValueType(new BigInteger("1")));
        Assert.assertEquals(JmxConstants.BOOLEAN, Utils.getValueType(Boolean.TRUE));
        Assert.assertEquals(JmxConstants.BYTE, Utils.getValueType((byte) 1));
        Assert.assertEquals(JmxConstants.CHARACTER, Utils.getValueType((char) 1));
        Assert.assertEquals(JmxConstants.DOUBLE, Utils.getValueType((double) 1));
        Assert.assertEquals(JmxConstants.FLOAT, Utils.getValueType((float) 1));
        Assert.assertEquals(JmxConstants.INTEGER, Utils.getValueType(1));
        Assert.assertEquals(JmxConstants.LONG, Utils.getValueType((long) 1));
        Assert.assertEquals(JmxConstants.SHORT, Utils.getValueType((short) 1));
        Assert.assertEquals(JmxConstants.STRING, Utils.getValueType("1"));

        try {
            Utils.getValueType(new Object());
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.BIGDECIMAL, Utils.getValueType(new BigDecimal[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.BIGINTEGER, Utils.getValueType(new BigInteger[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.BOOLEAN, Utils.getValueType(new Boolean[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.BYTE, Utils.getValueType(new Byte[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.CHARACTER, Utils.getValueType(new Character[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.DOUBLE, Utils.getValueType(new Double[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.FLOAT, Utils.getValueType(new Float[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.INTEGER, Utils.getValueType(new Integer[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.LONG, Utils.getValueType(new Long[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.SHORT, Utils.getValueType(new Short[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.STRING, Utils.getValueType(new String[0]));
        try {
            Utils.getValueType(new Object[0]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }

        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_BOOLEAN, Utils.getValueType(new boolean[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_BYTE, Utils.getValueType(new byte[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_CHAR, Utils.getValueType(new char[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_DOUBLE, Utils.getValueType(new double[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_FLOAT, Utils.getValueType(new float[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_INT, Utils.getValueType(new int[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_LONG, Utils.getValueType(new long[0]));
        Assert.assertEquals(JmxConstants.ARRAY_OF + JmxConstants.P_SHORT, Utils.getValueType(new short[0]));

        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.STRING, Utils.getValueType(new Vector()));
        try {
            Utils.getValueType(new Vector<Object>(Arrays.asList(new Object())));
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.BIGDECIMAL, Utils.getValueType(new Vector<BigDecimal>(Arrays.asList(new BigDecimal(1)))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.BIGINTEGER, Utils.getValueType(new Vector<BigInteger>(Arrays.asList(new BigInteger("1")))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.BOOLEAN, Utils.getValueType(new Vector<Boolean>(Arrays.asList(Boolean.FALSE))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.BYTE, Utils.getValueType(new Vector<Byte>(Arrays.asList(Byte.valueOf("1")))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.CHARACTER, Utils.getValueType(new Vector<Character>(Arrays.asList((char) 1))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.DOUBLE, Utils.getValueType(new Vector<Double>(Arrays.asList((double) 1))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.FLOAT, Utils.getValueType(new Vector<Float>(Arrays.asList((float) 1))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.INTEGER, Utils.getValueType(new Vector<Integer>(Arrays.asList(1))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.LONG, Utils.getValueType(new Vector<Long>(Arrays.asList((long) 1))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.SHORT, Utils.getValueType(new Vector<Short>(Arrays.asList((short) 1))));
        Assert.assertEquals(JmxConstants.VECTOR_OF + JmxConstants.STRING, Utils.getValueType(new Vector<String>(Arrays.asList(""))));

    }

    @Test
    public void testSerializeToString() throws Exception {
        Assert.assertEquals(new BigDecimal(1).toString(), Utils.serializeToString(new BigDecimal(1)));
        Assert.assertEquals(new BigInteger("1").toString(), Utils.serializeToString(new BigInteger("1")));
        Assert.assertEquals(Boolean.TRUE.toString(), Utils.serializeToString(Boolean.TRUE));
        Assert.assertEquals(Byte.valueOf((byte) 1).toString(), Utils.serializeToString((byte) 1));
        Assert.assertEquals(Character.valueOf((char) 1).toString(), Utils.serializeToString((char) 1));
        Assert.assertEquals(Double.valueOf((char) 1).toString(), Utils.serializeToString((double) 1));
        Assert.assertEquals(Float.valueOf((char) 1).toString(), Utils.serializeToString((float) 1));
        Assert.assertEquals(Integer.valueOf(1).toString(), Utils.serializeToString(1));
        Assert.assertEquals(Long.valueOf(1).toString(), Utils.serializeToString((long) 1));
        Assert.assertEquals(Short.valueOf((short) 1).toString(), Utils.serializeToString((short) 1));

        Assert.assertEquals("\"1\"", Utils.serializeToString("1"));
        Assert.assertEquals("\"\'1\"", Utils.serializeToString("'1"));
        Assert.assertEquals("\"\'1\"\"", Utils.serializeToString("'1\""));
        Assert.assertEquals("\"\'\\\\1\"\"", Utils.serializeToString("'\\1\""));
        Assert.assertEquals("\"\'\\\\1\"\\\\\"", Utils.serializeToString("'\\1\"\\"));

        Assert.assertEquals("1,2", Utils.serializeToString(new BigDecimal[]{new BigDecimal(1), new BigDecimal(2)}));
        Assert.assertEquals("1,2", Utils.serializeToString(new BigInteger[]{new BigInteger("1"), new BigInteger("2")}));
        Assert.assertEquals("true,false", Utils.serializeToString(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        Assert.assertEquals("1,2", Utils.serializeToString(new Byte[]{(byte) 1, (byte) 2}));
        Assert.assertEquals("1,2", Utils.serializeToString(new Character[]{'1', '2'}));
        Assert.assertEquals((double) 1 + "," + (double) 2, Utils.serializeToString(new Double[]{(double)1, (double)2}));
        Assert.assertEquals((float) 1 + "," + (float) 2, Utils.serializeToString(new Float[]{(float)1, (float)2}));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new Integer[]{1, 2}));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new Long[]{(long)1, (long)2}));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new Short[]{(short)1, (short)2}));

        Assert.assertEquals("\"1\",\"2\"", Utils.serializeToString(new String[]{"1", "2"}));

        Assert.assertEquals("true,false", Utils.serializeToString(new boolean[]{true, false}));
        Assert.assertEquals("1,2", Utils.serializeToString(new byte[]{1, 2}));
        Assert.assertEquals("1,2", Utils.serializeToString(new char[]{'1', '2'}));
        Assert.assertEquals((double) 1 + "," + (double) 2, Utils.serializeToString(new double[]{(double)1, (double)2}));
        Assert.assertEquals((float) 1 + "," + (float) 2, Utils.serializeToString(new float[]{(float)1, (float)2}));
        Assert.assertEquals("1,2", Utils.serializeToString(new int[]{1, 2}));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new long[]{(long)1, (long)2}));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new short[]{(short)1, (short)2}));

        Assert.assertEquals("1,2", Utils.serializeToString(new Vector<BigDecimal>(Arrays.asList(new BigDecimal(1), new BigDecimal(2)))));
        Assert.assertEquals("1,2", Utils.serializeToString(new Vector<BigInteger>(Arrays.asList(new BigInteger("1"), new BigInteger("2")))));
        Assert.assertEquals("true,false", Utils.serializeToString(new Vector<Boolean>(Arrays.asList(true, false))));
        Assert.assertEquals((double) 1 + "," + (double) 2, Utils.serializeToString(new Vector<Double>(Arrays.asList((double) 1, (double) 2))));
        Assert.assertEquals((float) 1 + "," + (float) 2, Utils.serializeToString(new Vector<Float>(Arrays.asList((float) 1, (float) 2))));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new Vector<Integer>(Arrays.asList(1, 2))));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new Vector<Long>(Arrays.asList((long)1, (long)2))));
        Assert.assertEquals(1 + "," + 2, Utils.serializeToString(new Vector<Short>(Arrays.asList((short)1, (short)2))));

        Assert.assertEquals("\"1\",\"2\"", Utils.serializeToString(new Vector<String>(Arrays.asList("1", "2"))));
        Assert.assertEquals("\"1\",\"true\"", Utils.serializeToString(new Vector(Arrays.asList("1", Boolean.TRUE))));
        Assert.assertEquals("true,1", Utils.serializeToString(new Vector(Arrays.asList(Boolean.TRUE, "1"))));
    }
}