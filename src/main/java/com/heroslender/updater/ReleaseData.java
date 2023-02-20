package com.heroslender.updater;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class ReleaseData {
    @NotNull
    private final String version;
    @Nullable
    private final String downloadUrl;
}
