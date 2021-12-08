package com.nrlm.lakhpatikisaan.utils;

public class PreferenceKeyManager {
    private static final String PrefLoginSessionKey="PREF_LOGIN_SESSION_KEY";
    private static final String PrefSelectedMemberCode="PrefSelectedMemberCode";
    private static final String PrefSelectedBlockCode="PrefSelectedBlockCode";
    private static final String PrefSelectedGpCode="PrefSelectedGpCode";
    private static final String PrefSelectedVillageCode="PrefSelectedVillageCode";
    private static final String PrefSelectedShgCode="PrefSelectedShgCode";
    private static final String PrefSelectedClfCode="PrefSelectedClfCode";
    private static final String PrefSelectedVoCode="PrefSelectedVoCode";

    public static String getPrefSelectedClfCode() {
        return PrefSelectedClfCode;
    }

    public static String getPrefSelectedVoCode() {
        return PrefSelectedVoCode;
    }

    public static String getPrefSelectedBlockCode() {
        return PrefSelectedBlockCode;
    }

    public static String getPrefSelectedGpCode() {
        return PrefSelectedGpCode;
    }

    public static String getPrefSelectedVillageCode() {
        return PrefSelectedVillageCode;
    }

    public static String getPrefSelectedShgCode() {
        return PrefSelectedShgCode;
    }

    public static String getPrefSelectedMemberCode() {
        return PrefSelectedMemberCode;
    }

    public static String getPrefLoginSessionKey() {
        return PrefLoginSessionKey;
    }
}
