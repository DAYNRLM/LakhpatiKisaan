package com.nrlm.lakhpatikisaan.network.model.response;

import java.util.List;

public class SeccResponseBean {
    private int status;
    private SeccResponseBean.Error error;

    private List<SeccData> secc_data;

    public List<SeccData> getSecc_data() {
        return secc_data;
    }

    public void setSecc_data(List<SeccData> secc_data) {
        this.secc_data = secc_data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SeccResponseBean.Error getError() {
        return error;
    }

    public void setError(SeccResponseBean.Error error) {
        this.error = error;
    }

    public static class SeccData{
      private String father_name;
      private String member_name;
      private String secc_no;
      private String shg_member_code;

        public String getFather_name() {
            return father_name;
        }

        public void setFather_name(String father_name) {
            this.father_name = father_name;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getSecc_no() {
            return secc_no;
        }

        public void setSecc_no(String secc_no) {
            this.secc_no = secc_no;
        }

        public String getShg_member_code() {
            return shg_member_code;
        }

        public void setShg_member_code(String shg_member_code) {
            this.shg_member_code = shg_member_code;
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
