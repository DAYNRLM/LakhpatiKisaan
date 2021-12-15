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
    private static final String PrefLoginId="PrefLoginId";
    private static final String PrefStateShortName="PrefStateShortName";
    private static final String PrefImeiNo="PrefImeiNo";
    private static final String PrefDeviceinfo="PrefDeviceInfo";
    private static final String ForgotMobileNumber="forgotmobile";
    private static final String RandomOtp="genratedOtp";
    private static final String PREF_KEY_MPIN= "prfMPIN";
    private static final String PREF_KEY_LOGIN_DONE = "Longin";
    public static String getPrefImeiNo() {
        return PrefImeiNo;
    }
    public static String getPrefDeviceinfo() {
        return PrefDeviceinfo;
    }

    public static String getPrefStateShortName() {
        return PrefStateShortName;
    }

    public static String getPrefLoginId() {
        return PrefLoginId;
    }

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


    public static String getPrefKeyLoginDone() {
        return PREF_KEY_LOGIN_DONE;
    }





    public static String getPrefKeyMpin() {
        return PREF_KEY_MPIN;
    }


    public static String getRandomOtp() {
        return RandomOtp;
    }



    public static String getForgotMobileNumber() {
        return ForgotMobileNumber;
    }



    public static String getPrefSelectedMemberCode() {
        return PrefSelectedMemberCode;
    }

    public static String getPrefLoginSessionKey() {
        return PrefLoginSessionKey;
    }
}
