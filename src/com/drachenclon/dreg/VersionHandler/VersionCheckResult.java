package com.drachenclon.dreg.VersionHandler;

public final class VersionCheckResult {
    public final boolean Result;
    public final String Message;

    public VersionCheckResult(boolean result, String message) {
        Result = result;
        Message = message;
    }
}
