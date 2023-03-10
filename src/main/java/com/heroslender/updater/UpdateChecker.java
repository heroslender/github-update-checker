package com.heroslender.updater;


import com.heroslender.updater.impl.GithubUpdateChecker;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface UpdateChecker {

    /**
     * Fetches the latest version from the repository releases.
     *
     * @param owner The owner of the repository
     * @param repo  The repository ID
     * @return The latest version
     * @throws com.heroslender.updater.exceptions.NoVersionsFoundException if there are no versions available
     * @throws IOException                                                 if an I/O exception occurs.
     */
    @NotNull
    ReleaseData fetchLatest(@NotNull String owner, @NotNull String repo) throws IOException;

    /**
     * Compares two versions.
     *
     * @param currentVersion The currently running version
     * @param remoteVersion  The latest version available in the repository
     * @return the comparison result
     */
    @NotNull
    UpdateCheckResult checkVersion(@NotNull String currentVersion, @NotNull String remoteVersion);

    /**
     * Fetches the latest version from the repository releases and compares
     * it against the current version.
     *
     * @param currentVersion The currently running version
     * @param repoOwner      The owner of the repository
     * @param repoId         The repository ID
     * @return the comparison result
     * @throws com.heroslender.updater.exceptions.NoVersionsFoundException if there are no versions available
     * @throws IOException                                                 if an I/O exception occurs.
     */
    @NotNull
    UpdateCheckResult checkVersion(@NotNull String currentVersion, @NotNull String repoOwner, @NotNull String repoId) throws IOException;
}
