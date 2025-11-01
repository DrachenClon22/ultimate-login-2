package com.drachenclon.dreg.VersionHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public final class Version implements Comparable<Version> {
    private final int _major;
    private final int _minor;
    private final int _patch;

    public Version(int major, int minor, int patch) {
        _major = major;
        _minor = minor;
        _patch = patch;
    }

    public Version(String version) {
        int major = 0, minor = 0, patch = 0;

        try {
            if (version != null && !version.isEmpty()) {
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(version);
                int[] parts = {0, 0, 0};
                int i = 0;

                while (m.find() && i < 3) {
                    try {
                        parts[i] = Integer.parseInt(m.group());
                    } catch (NumberFormatException e) {
                        parts[i] = Integer.MAX_VALUE;
                    }
                    i++;
                }

                major = parts[0];
                minor = parts[1];
                patch = parts[2];
            }
        } catch (Exception ignored) {}

        this._major = major;
        this._minor = minor;
        this._patch = patch;
    }

    @Override
    public int compareTo(Version ver) {
        int maj = Integer.compare(_major, ver._major);
        int min = Integer.compare(_minor, ver._minor);
        int patch = Integer.compare(_patch, ver._patch);

        if (maj == 1) {
            return 1;
        } else if (maj == 0) {
            if (min == 1) {
                return 1;
            } else if (min == 0) {
                if (patch == 1) {
                    return 1;
                } else if (patch == 0) {
                    return 0;
                }
            }
        }
        return -1;
    }
    @Override
    public String toString() {
        return getVersionString();
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Version)) {
            return false;
        }

        Version v = (Version) o;
        return v._major == this._major && v._minor == this._minor && v._patch == this._patch;
    }

    public String getVersionString() {
        return _major + "." + _minor + "." + _patch;
    }

    public Version getVersion() {
        return this;
    }
}
