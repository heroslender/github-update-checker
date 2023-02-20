package com.heroslender.updater;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class UpdateCheckResult {
    @NotNull private final Version currentVersion;
    @NotNull private final Version remoteVersion;
    @Nullable private final String downloadUrl;

    public Status getStatus() {
        if (currentVersion.isLowerThan(remoteVersion)) {
            return Status.OUTDATED;
        } else if (currentVersion.isHigherThan(remoteVersion)) {
            return Status.RUNNING_AHEAD;
        } else {
            return Status.UPDATED;
        }
    }

    public enum Status {
        OUTDATED,
        UPDATED,
        RUNNING_AHEAD
    }
}