package com.ubs.gedit.gedex.actor.util;

import junit.framework.TestCase;

public class DistributionTest extends TestCase {
    public void testNormInv() throws Exception {

        // Test values taken from http://support.microsoft.com/kb/827358
        double result = Distribution.NormInv(0.5, 100, 10);
        assertEquals(100, result, 0.0001);

        result = Distribution.NormInv(0.6, 100, 10);
        assertEquals(102.533471, result, 0.0001);

        result = Distribution.NormInv(0.9, 100, 10);
        assertEquals(112.8155157, result, 0.0001);

        result = Distribution.NormInv(0.95, 100, 10);
        assertEquals(116.4485363, result, 0.0001);

        result = Distribution.NormInv(0.975, 100, 10);
        assertEquals(119.5996398, result, 0.0001);

        result = Distribution.NormInv(0.001, 100, 10);
        assertEquals(69.09767694, result, 0.0001);

        result = Distribution.NormInv(0.0001, 100, 10);
        assertEquals(62.80983515, result, 0.0001);

        result = Distribution.NormInv(0.00001, 100, 10);
        assertEquals(57.35109206, result, 0.0001);

        result = Distribution.NormInv(0.000001, 100, 10);
        assertEquals(52.46575659, result, 0.0001);

        result = Distribution.NormInv(0.0000003, 100, 10);
        assertEquals(50.08782702, result, 0.0001);

        result = Distribution.NormInv(0.0000002, 100, 10);
        assertEquals(49.3104225, result, 0.0001);
    }
}
