package com.nrlm.lakhpatikisaan.network.model.response;

import java.io.Serializable;
import java.util.List;

public class ContactsResponseBean implements Serializable {

    private List<ContactsInfo> data;

    public List<ContactsInfo> getData() {
        return data;
    }

    public void setData(List<ContactsInfo> data) {
        this.data = data;
    }


   public static class ContactsInfo {

        private String mobile;
        private String district_name;
        private String block_name;
        private String state_name;
        private String employee_name;
        private String designation;
        private String email;

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getBlock_name() {
            return block_name;
        }

        public void setBlock_name(String block_name) {
            this.block_name = block_name;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
