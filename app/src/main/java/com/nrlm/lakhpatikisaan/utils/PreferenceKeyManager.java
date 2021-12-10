package com.nrlm.lakhpatikisaan.utils;

public class PreferenceKeyManager {
    private static final String PrefLoginSessionKey="PREF_LOGIN_SESSION_KEY";
    private static final String PrefSelectedMemberCode="PrefSelectedMemberCode";
    private static final String ForgotMobileNumber="forgotmobile";
    private static final String RandomOtp="genratedOtp";
    private static final String PREF_KEY_MPIN= "prfMPIN";
    private static final String PREF_KEY_LOGIN_DONE = "Longin";

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
