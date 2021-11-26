package com.nrlm.lakhpatikisaan.network.model.request;

import java.io.Serializable;

public class ContactsRequestBean implements Serializable {

    private String state_name;
    private String district_name;

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    /*{"state_name":"TAMIL NADU","district_name":"DINDIGUL"}*/
}
