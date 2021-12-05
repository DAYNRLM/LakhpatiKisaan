package com.nrlm.lakhpatikisaan.network.model.response;

import java.util.List;

public class SupportiveMastersResponseBean {

    private int status;
    private Error error;
    private List<Sector> sectors;
    private List<IncomeFrequency> income_frequencies;


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

      public static class IncomeFrequency{
        private int frequency_id;
        private String frequency_name;
        private List<IncomeRange>income_range;

          public int getFrequency_id() {
              return frequency_id;
          }

          public void setFrequency_id(int frequency_id) {
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
        private int range_id;
        private String range_name;


        public int getRange_id() {
            return range_id;
        }

        public void setRange_id(int range_id) {
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
        private int sector_code;
        private List<Activity> activities;

        public String getSector_name() {
            return sector_name;
        }

        public void setSector_name(String sector_name) {
            this.sector_name = sector_name;
        }

        public int getSector_code() {
            return sector_code;
        }

        public void setSector_code(int sector_code) {
            this.sector_code = sector_code;
        }

        public List<Activity> getActivities() {
            return activities;
        }

        public void setActivities(List<Activity> activities) {
            this.activities = activities;
        }
    }

    public static class Activity{
        private int activity_code;
        private String activity_name;

        public int getActivity_code() {
            return activity_code;
        }

        public void setActivity_code(int activity_code) {
            this.activity_code = activity_code;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }
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