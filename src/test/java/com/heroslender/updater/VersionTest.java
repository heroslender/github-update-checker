package com.heroslender.updater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    public void testMajor() throws Exception {
        Version v2_9_1 = Version.fromString("v2.9.1");

        assertEquals(2, v2_9_1.getMajor());
        assertEquals(9, v2_9_1.getMinor());
        assertEquals(1, v2_9_1.getPatch());
        assertEquals(v2_9_1.toString(), "v2.9.1");

        assertTrue(v2_9_1.isHigherThan(Version.fromString("v1.10.1")));
        assertTrue(v2_9_1.isHigherThanOrEqualTo(Version.fromString("v1.9.1")));
    }

    @Test
    public void testMinor() throws Exception {
        Version v1_10_1 = Version.fromString("v1.10.1");

        assertEquals(1, v1_10_1.getMajor());
        assertEquals(10, v1_10_1.getMinor());
        assertEquals(1, v1_10_1.getPatch());

        assertEquals(v1_10_1.toString(), "v1.10.1");

        assertTrue(Version.fromString("v1.9.1").isLowerThan(v1_10_1));
        assertTrue(Version.fromString("v1.9.1").isLowerThanOrEqualTo(v1_10_1));
    }

    @Test
    public void testPatch() throws Exception {
        Version v1_9_2 = Version.fromString("v1.9.2");

        assertEquals(1, v1_9_2.getMajor());
        assertEquals(9, v1_9_2.getMinor());
        assertEquals(2, v1_9_2.getPatch());

        assertEquals(v1_9_2.toString(), "v1.9.2");
        assertEquals(v1_9_2, Version.fromString("v1.9.2"));

        assertTrue(v1_9_2.isHigherThan(Version.fromString("v1.9.1")));
    }

    @Test
    public void testSnapshot() throws Exception {
        Version v1_9_2 = Version.fromString("v1.9.2");

        assertEquals(1, v1_9_2.getMajor());
        assertEquals(9, v1_9_2.getMinor());
        assertEquals(2, v1_9_2.getPatch());

        assertEquals(v1_9_2.toString(), "v1.9.2");
        assertEquals(v1_9_2, Version.fromString("v1.9.2"));

        assertFalse(v1_9_2.isSnapshot());
        assertFalse(Version.fromString("v1.9.2-RELEASE").isSnapshot());
        assertTrue(Version.fromString("v1.9.2-SNAPSHOT").isSnapshot());
    }
}