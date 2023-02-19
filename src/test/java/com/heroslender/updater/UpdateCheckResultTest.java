package com.heroslender.updater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateCheckResultTest {

    @Test
    public void testOutdated() {
        UpdateCheckResult result = new UpdateCheckResult(
                Version.fromString("1.0.0"),
                Version.fromString("1.0.1")
        );

        assertEquals(UpdateCheckResult.Status.OUTDATED, result.getStatus());
    }

    @Test
    public void testUpdated() {
        UpdateCheckResult result = new UpdateCheckResult(
                Version.fromString("1.0.0"),
                Version.fromString("1.0.0")
        );

        assertEquals(UpdateCheckResult.Status.UPDATED, result.getStatus());
    }

    @Test
    public void testAhead() {
        UpdateCheckResult result = new UpdateCheckResult(
                Version.fromString("1.1.0"),
                Version.fromString("1.0.0")
        );

        assertEquals(UpdateCheckResult.Status.RUNNING_AHEAD, result.getStatus());
    }
}
