package com.heroslender.updater;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^v?(\\d+)\\.(\\d+)\\.(\\d+)(-SNAPSHOT|-RELEASE)?$");
    private final int major;
    private final int minor;
    private final int patch;
    private final boolean snapshot;

    private Version(int major, int minor, int patch, boolean snapshot) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.snapshot = snapshot;
    }

    /**
     * Parses the version from the given string.
     *
     * @param string the version string
     * @return The Version object
     * @throws IllegalArgumentException if the version is not in a valid format
     * @throws NullPointerException     if the version string is null
     */
    @NotNull
    public static Version fromString(@NotNull String string) {
        Objects.requireNonNull(string, "string cannot be null.");

        Matcher matcher = VERSION_PATTERN.matcher(string);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(string + " is not in valid version format. e.g. 1.10.1");
        }

        return new Version(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), "-SNAPSHOT".equals(matcher.group(4)));
    }

    public static void main(String[] args) {
        fromString("1.2.3");
        fromString("v1.2.3");
        fromString("1.12.3");
        fromString("v1.12.3");
        fromString("1.21.3-SNAPSHOT");
        fromString("v1.12.3-RELEASE");
    }

    public boolean isHigherThan(Version o) {
        return compareTo(o) > 0;
    }

    public boolean isHigherThanOrEqualTo(Version o) {
        return compareTo(o) >= 0;
    }

    public boolean isLowerThan(Version o) {
        return compareTo(o) < 0;
    }

    public boolean isLowerThanOrEqualTo(Version o) {
        return compareTo(o) <= 0;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Version that = (Version) o;
        return major == that.major &&
                minor == that.minor &&
                patch == that.patch &&
                snapshot == that.snapshot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, snapshot);
    }

    @Override
    public String toString() {
        return "v" + major + "." + minor + "." + patch + (isSnapshot() ? "-SNAPSHOT" : "");
    }

    @Override
    public int compareTo(Version o) {
        if (major < o.major) {
            return -1;
        } else if (major > o.major) {
            return 1;
        } else { // equal major
            if (minor < o.minor) {
                return -1;
            } else if (minor > o.minor) {
                return 1;
            } else { // equal minor
                if (patch < o.patch) {
                    return -1;
                } else if (patch > o.patch) {
                    return 1;
                } else {
                    if (snapshot && !o.snapshot) {
                        return -1;
                    }
                    return 0; // o is the same version as this.
                }
            }
        }
    }
}