package com.heroslender.updater.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
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
    private static final String GITHUB_LATEST_RELEASE_URL = "https://api.github.com/repos/%s/%s/releases/latest";

    @Getter private static final GithubUpdateChecker instance = new GithubUpdateChecker();
    public static void main(String[] args) throws IOException {
        String latest = GithubUpdateChecker.getInstance().fetchLatest("heroslender", "HeroSpawners");
        System.out.println("latest: " + latest);
    }

    @Override
    public @NotNull String fetchLatest(@NotNull String owner, @NotNull String repo) throws IOException {
        Objects.requireNonNull(owner, "owner cannot be null");
        Objects.requireNonNull(repo, "repo cannot be null");

        URL url = new URL(String.format(GITHUB_LATEST_RELEASE_URL, owner, repo));
        InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        JsonObject json = new Gson().fromJson(new JsonReader(reader), JsonObject.class);
        if (!json.has("tag_name")) {
            throw new NoVersionsFoundException();
        }

        return json.get("tag_name").getAsString();
    }

    @Override
    public @NotNull UpdateCheckResult checkVersion(@NotNull String currentVersion, @NotNull String remoteVersion) {
        Version current = Version.fromString(currentVersion);
        Version remote = Version.fromString(remoteVersion);

        return new UpdateCheckResult(current, remote);
    }

    @Override
    public @NotNull UpdateCheckResult checkVersion(@NotNull String currentVersion, @NotNull String repoOwner, @NotNull String repoId) throws IOException {
        String latest = fetchLatest(repoOwner, repoId);

        return checkVersion(currentVersion, latest);
    }
}
