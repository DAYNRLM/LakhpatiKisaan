package com.nrlm.lakhpatikisaan.network.model.request;

import com.nrlm.lakhpatikisaan.network.model.response.SupportiveMastersResponseBean;

import java.io.Serializable;
import java.util.List;

public class SyncEntriesRequestBean implements Serializable {

    private String login_id  ;
    private String imei_no;
    private String device_name ;
    private String  location_coordinate;
    private String state_short_name ;
    private List<SyncEntry> nrlm_entry_sync;

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getLocation_coordinate() {
        return location_coordinate;
    }

    public void setLocation_coordinate(String location_coordinate) {
        this.location_coordinate = location_coordinate;
    }

    public String getState_short_name() {
        return state_short_name;
    }

    public void setState_short_name(String state_short_name) {
        this.state_short_name = state_short_name;
    }

    public List<SyncEntry> getNrlm_entry_sync() {
        return nrlm_entry_sync;
    }

    public void setNrlm_entry_sync(List<SyncEntry> nrlm_entry_sync) {
        this.nrlm_entry_sync = nrlm_entry_sync;
    }

    public static class SyncEntry{
        private String shg_code ;
        private String shg_member_code;
        private List<ActivityData> activities_data_sync;

        public String getShg_code() {
            return shg_code;
        }

        public void setShg_code(String shg_code) {
            this.shg_code = shg_code;
        }

        public String getShg_member_code() {
            return shg_member_code;
        }

        public void setShg_member_code(String shg_member_code) {
            this.shg_member_code = shg_member_code;
        }

        public List<ActivityData> getActivities_data_sync() {
            return activities_data_sync;
        }

        public void setActivities_data_sync(List<ActivityData> activities_data_sync) {
            this.activities_data_sync = activities_data_sync;
        }
    }

    public static class ActivityData{
        private String entry_year;
        private String entry_month ;
        private String created_on_android;
        private String activity_code;
        private String frequency_code;
        private String range_code;
        private String sector_code;
        private String flag_before_after_nrlm;

        public String getEntry_year() {
            return entry_year;
        }

        public void setEntry_year(String entry_year) {
            this.entry_year = entry_year;
        }

        public String getEntry_month() {
            return entry_month;
        }

        public void setEntry_month(String entry_month) {
            this.entry_month = entry_month;
        }

        public String getCreated_on_android() {
            return created_on_android;
        }

        public void setCreated_on_android(String created_on_android) {
            this.created_on_android = created_on_android;
        }

        public String getActivity_code() {
            return activity_code;
        }

        public void setActivity_code(String activity_code) {
            this.activity_code = activity_code;
        }

        public String getFrequency_code() {
            return frequency_code;
        }

        public void setFrequency_code(String frequency_code) {
            this.frequency_code = frequency_code;
        }

        public String getRange_code() {
            return range_code;
        }

        public void setRange_code(String range_code) {
            this.range_code = range_code;
        }

        public String getSector_code() {
            return sector_code;
        }

        public void setSector_code(String sector_code) {
            this.sector_code = sector_code;
        }

        public String getFlag_before_after_nrlm() {
            return flag_before_after_nrlm;
        }

        public void setFlag_before_after_nrlm(String flag_before_after_nrlm) {
            this.flag_before_after_nrlm = flag_before_after_nrlm;
        }
    }
}
 /*{
        "login_id": "HRKSVISHAKHA",
        "imei_no": "111",
        "device_name": "OPPO--CPH1933",
        "location_coordinate": "1111",
        "state_short_name": "hr",

        "nrlm_entry_sync": [{
                "shg_code": "3344",
                "shg_member_code": "2334",
                "activities_data_sync": [{
                        "entry_year": "2021",
                        "entry_month": "12",
                        "created_on_android": "20/12/2021 00:12:32",
                        "activity_code": "1",
                        "frequency_code": "1",
                        "range_code": "1",
                        "sector_code": "1",
                        "flag_before_after_nrlm": "B"
                }]
        }]
}*/