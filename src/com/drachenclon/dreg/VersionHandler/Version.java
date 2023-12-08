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
        Integer[] ver = null;
        try {
            version = version.replaceAll("[^\\d.]", "");
            ver = Arrays.stream(version.split("\\.")).map(x->Integer.parseInt(x)).toArray(size->new Integer[size]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _major = ver != null ? ver[0] : 0;
        _minor = ver != null ? ver[1] : 0;
        _patch = ver != null ? ver[2] : 0;
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
