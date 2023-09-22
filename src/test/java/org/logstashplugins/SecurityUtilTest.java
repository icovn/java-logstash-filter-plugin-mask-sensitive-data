package org.logstashplugins;

import static org.junit.Assert.*;

import org.junit.Test;

public class SecurityUtilTest {

    @Test
    public void maskSensitiveNotContainPattern() {
        String input = "Customer phone:0123456789, has phone:0123456789";
        String pattern = "phone=";
        String expected = "Customer phone:0123456789, has phone:0123456789";
        assertEquals(expected, SecurityUtil.maskSensitive(input, pattern));
    }

    @Test
    public void maskSensitiveContainSingleInstances() {
        String input = "Customer phone:0123456789, email:abc@gmail.com";
        String pattern = "phone:";
        String expected = "Customer phone:***, email:abc@gmail.com";
        assertEquals(expected, SecurityUtil.maskSensitive(input, pattern));
    }

    @Test
    public void maskSensitiveContainSingleInstanceWithoutSpace() {
        String input = "Customer phone:0123456789";
        String pattern = "phone:";
        String expected = "Customer phone:***";
        assertEquals(expected, SecurityUtil.maskSensitive(input, pattern));
    }

    @Test
    public void maskSensitiveContainMultipleInstances() {
        String input = "Customer phone:0123456789, has phone:0123456789";
        String pattern = "phone:";
        String expected = "Customer phone:***, has phone:***";
        assertEquals(expected, SecurityUtil.maskSensitive(input, pattern));
    }

    @Test
    public void maskSensitiveContainMultipleInstancesComplex() {
        String input = "Customer phone:0123456789, email:abc@gmail.com, has phone:0123456789";
        String pattern = "phone:";
        String expected = "Customer phone:***, email:abc@gmail.com, has phone:***";
        assertEquals(expected, SecurityUtil.maskSensitive(input, pattern));
    }

    @Test
    public void maskSensitiveContainMultiplePatterns() {
        String input = "Customer phone:0123456789, email:abc@gmail.com, has phone:0123456789";
        String patterns = "phone:,email:";
        String expected = "Customer phone:***, email:***, has phone:***";
        assertEquals(expected, SecurityUtil.maskSensitiveMultiple(input, patterns));
    }

    @Test
    public void maskSensitiveContainMultiplePatternsComplex() {
        String input = "Customer phone:0123456789, email=abc@gmail.com, id:9, has phone:0123456789";
        String patterns = "phone:,email=";
        String expected = "Customer phone:***, email=***, id:9, has phone:****";
        assertEquals(expected, SecurityUtil.maskSensitiveMultiple(input, patterns));
    }
}