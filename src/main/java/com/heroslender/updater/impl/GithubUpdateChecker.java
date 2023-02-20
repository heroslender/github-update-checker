package com.heroslender.updater.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.heroslender.updater.ReleaseData;
import com.heroslender.updater.UpdateCheckResult;
import com.heroslender.updater.UpdateChecker;
import com.heroslender.updater.Version;
import com.heroslender.updater.exceptions.NoVersionsFoundException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class GithubUpdateChecker implements UpdateChecker {
    public static final String GITHUB_LATEST_RELEASE_URL = "https://github.com/%s/%s/releases/latest";
    private static final String GITHUB_LATEST_RELEASE_API_URL = "https://api.github.com/repos/%s/%s/releases/latest";
    @Getter private static final GithubUpdateChecker instance = new GithubUpdateChecker();

    @Override
    public @NotNull ReleaseData fetchLatest(@NotNull String owner, @NotNull String repo) throws IOException {
        Objects.requireNonNull(owner, "owner cannot be null");
        Objects.requireNonNull(repo, "repo cannot be null");

        URL url = new URL(String.format(GITHUB_LATEST_RELEASE_API_URL, owner, repo));
        InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        JsonObject json = new Gson().fromJson(new JsonReader(reader), JsonObject.class);
        if (!json.has("tag_name")) {
            throw new NoVersionsFoundException();
        }

        String download = null;
        JsonArray assets = json.get("assets").getAsJsonArray();
        if (assets.size() > 0) {
            JsonObject asset = assets.get(0).getAsJsonObject();
            download = asset.get("browser_download_url").getAsString();
        }

        return new ReleaseData(json.get("tag_name").getAsString(), download);
    }

    @Override
    public @NotNull UpdateCheckResult checkVersion(@NotNull String currentVersion, @NotNull String remoteVersion) {
        Version current = Version.fromString(currentVersion);
        Version remote = Version.fromString(remoteVersion);

        return new UpdateCheckResult(current, remote, null);
    }

    @Override
    public @NotNull UpdateCheckResult checkVersion(@NotNull String currentVersion, @NotNull String repoOwner, @NotNull String repoId) throws IOException {
        ReleaseData latest = fetchLatest(repoOwner, repoId);

        Version current = Version.fromString(currentVersion);
        Version remote = Version.fromString(latest.getVersion());

        return new UpdateCheckResult(current, remote, latest.getDownloadUrl());
    }

}
