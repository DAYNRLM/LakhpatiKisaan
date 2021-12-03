package com.nrlm.lakhpatikisaan.network.model.response;

import java.util.List;

public class SupportiveMastersResponseBean {

    private int status;
    private Error error;
    private List<Sector> sectors;
    private List<IncomeFrequency> income_frequencies;
    private ValidationConditions vald_cond;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public List<IncomeFrequency> getIncome_frequencies() {
        return income_frequencies;
    }

    public void setIncome_frequencies(List<IncomeFrequency> income_frequencies) {
        this.income_frequencies = income_frequencies;
    }

    public ValidationConditions getVald_cond() {
        return vald_cond;
    }

    public void setVald_cond(ValidationConditions vald_cond) {
        this.vald_cond = vald_cond;
    }

    public static class ValidationConditions{
        private String entries_editable ;

        public String getEntries_editable() {
            return entries_editable;
        }

        public void setEntries_editable(String entries_editable) {
            this.entries_editable = entries_editable;
        }
    }


    public static class IncomeFrequency{
        private String frequency_id;
        private String frequency_name;
        private List<IncomeRange>income_range;

        public String getFrequency_id() {
            return frequency_id;
        }

        public void setFrequency_id(String frequency_id) {
            this.frequency_id = frequency_id;
        }

        public String getFrequency_name() {
            return frequency_name;
        }

        public void setFrequency_name(String frequency_name) {
            this.frequency_name = frequency_name;
        }

        public List<IncomeRange> getIncome_range() {
            return income_range;
        }

        public void setIncome_range(List<IncomeRange> income_range) {
            this.income_range = income_range;
        }
    }
    public static class IncomeRange{
        private String range_id;
        private String range_name;


        public String getRange_id() {
            return range_id;
        }

        public void setRange_id(String range_id) {
            this.range_id = range_id;
        }

        public String getRange_name() {
            return range_name;
        }

        public void setRange_name(String range_name) {
            this.range_name = range_name;
        }
    }

    public static class Sector{
        private String sector_name;
        private String sector_id;
        private List<Activity> activities;

        public String getSector_name() {
            return sector_name;
        }

        public void setSector_name(String sector_name) {
            this.sector_name = sector_name;
        }

        public String getSector_id() {
            return sector_id;
        }

        public void setSector_id(String sector_id) {
            this.sector_id = sector_id;
        }

        public List<Activity> getActivities() {
            return activities;
        }

        public void setActivities(List<Activity> activities) {
            this.activities = activities;
        }
    }

    public static class Activity{
        private String activity_id;
        private String activity_name;


    }
    public static class Error{
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}


    /*{
        "status": 1,
        "error": {
                "code": "E200",
                "message": "Success"
        },

        "sectors": [{
                "sector_name": "",
                "sector_id": "",
                "activities": [{
                        "activity_id": "",
                        "activity_name": ""
                }]
        }],

        "income_frequencies": [{
                "frequency_name": "",
                "frequency_id": "",
                "income_range": [{
                        "range_id": "",
                        "range_name": ""
                }]
        }],
        "vald_cond": {

 "before_nrlm_entry_editable": "0/1",
"after_nrlm_entry_editable": "0/1"
        }
}*/