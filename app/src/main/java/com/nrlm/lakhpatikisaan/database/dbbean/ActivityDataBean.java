package com.nrlm.lakhpatikisaan.database.dbbean;

import androidx.room.ColumnInfo;

public class ActivityDataBean {

    @ColumnInfo(name = "entryYearCode")
    private String entry_year;
    @ColumnInfo(name = "entryMonthCode")
    private String  entry_month;
    @ColumnInfo(name = "entryCreatedDate")
    private String created_on_android;
    @ColumnInfo(name = "activityCode")
    private String activity_code;
    @ColumnInfo(name = "incomeFrequencyCode")
    private String frequency_code;
    @ColumnInfo(name = "incomeRangCode")
    private String range_code;
    @ColumnInfo(name = "sectorDate")
    private String sector_code;
    @ColumnInfo(name = "flagBeforeAfterNrlm")
    private String flag_before_after_nrlm;

    public ActivityDataBean(String entry_year, String entry_month, String created_on_android, String activity_code, String frequency_code, String range_code, String sector_code, String flag_before_after_nrlm) {
        this.entry_year = entry_year;
        this.entry_month = entry_month;
        this.created_on_android = created_on_android;
        this.activity_code = activity_code;
        this.frequency_code = frequency_code;
        this.range_code = range_code;
        this.sector_code = sector_code;
        this.flag_before_after_nrlm = flag_before_after_nrlm;
    }

    public void setEntry_year(String entry_year) {
        this.entry_year = entry_year;
    }

    public void setEntry_month(String entry_month) {
        this.entry_month = entry_month;
    }

    public void setCreated_on_android(String created_on_android) {
        this.created_on_android = created_on_android;
    }

    public void setActivity_code(String activity_code) {
        this.activity_code = activity_code;
    }

    public void setFrequency_code(String frequency_code) {
        this.frequency_code = frequency_code;
    }

    public void setRange_code(String range_code) {
        this.range_code = range_code;
    }

    public void setSector_code(String sector_code) {
        this.sector_code = sector_code;
    }

    public void setFlag_before_after_nrlm(String flag_before_after_nrlm) {
        this.flag_before_after_nrlm = flag_before_after_nrlm;
    }

    public String getEntry_year() {
        return entry_year;
    }

    public String getEntry_month() {
        return entry_month;
    }

    public String getCreated_on_android() {
        return created_on_android;
    }

    public String getActivity_code() {
        return activity_code;
    }

    public String getFrequency_code() {
        return frequency_code;
    }

    public String getRange_code() {
        return range_code;
    }

    public String getSector_code() {
        return sector_code;
    }

    public String getFlag_before_after_nrlm() {
        return flag_before_after_nrlm;
    }
}
/*{
                        "entry_year": "2021",
                        "entry_month": "12",
                        "created_on_android": "20/12/2021 00:12:32.786765",
                        "activity_code": "1",
                        "frequency_code": "1",
                        "range_code": "1",
                        "sector_code": "1",
                        "flag_before_after_nrlm": "B"

}*/