package com.nrlm.lakhpatikisaan.network.model.response;

import java.util.List;

public class MasterDataResponseBean {
    private int status;
    private Error error;
    private List<MasterData> location_master;

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

    public List<MasterData> getLocation_master() {
        return location_master;
    }

    public void setLocation_master(List<MasterData> location_master) {
        this.location_master = location_master;
    }

    public static class Error extends Throwable {
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

    public static class MasterData{
        private String block_code;
        private String gp_code;
        private String block_name;
        private String gp_name ;
        private String village_code ;
        private String village_name ;
        private String shg_name ;
        private String shg_code ;
        private String member_code ;
        private String member_name  ;
        private String clf_code ;
        private String clf_name  ;
        private String vo_code  ;
        private String vo_name  ;
        private String member_joining_date  ;
        private String last_entry_after_nrlm  ;
        private String last_entry_before_nrlm  ;
        private String lgd_village_code  ;
        private String secc_no_flag  ;
        private String aadhaar_verified_status  ;
        private String gender;
        private String belonging_name;

        public String getBelonging_name() {
            return belonging_name;
        }

        public void setBelonging_name(String belonging_name) {
            this.belonging_name = belonging_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAadhaar_verified_status() {
            return aadhaar_verified_status;
        }

        public void setAadhaar_verified_status(String aadhaar_verified_status) {
            this.aadhaar_verified_status = aadhaar_verified_status;
        }

        public String getLgd_village_code() {
            return lgd_village_code;
        }

        public void setLgd_village_code(String lgd_village_code) {
            this.lgd_village_code = lgd_village_code;
        }

        public String getSecc_no_flag() {
            return secc_no_flag;
        }

        public void setSecc_no_flag(String secc_no_flag) {
            this.secc_no_flag = secc_no_flag;
        }

        public String getBlock_code() {
            return block_code;
        }

        public void setBlock_code(String block_code) {
            this.block_code = block_code;
        }

        public String getGp_code() {
            return gp_code;
        }

        public void setGp_code(String gp_code) {
            this.gp_code = gp_code;
        }

        public String getBlock_name() {
            return block_name;
        }

        public void setBlock_name(String block_name) {
            this.block_name = block_name;
        }

        public String getGp_name() {
            return gp_name;
        }

        public void setGp_name(String gp_name) {
            this.gp_name = gp_name;
        }

        public String getVillage_code() {
            return village_code;
        }

        public void setVillage_code(String village_code) {
            this.village_code = village_code;
        }

        public String getVillage_name() {
            return village_name;
        }

        public void setVillage_name(String village_name) {
            this.village_name = village_name;
        }

        public String getShg_name() {
            return shg_name;
        }

        public void setShg_name(String shg_name) {
            this.shg_name = shg_name;
        }

        public String getShg_code() {
            return shg_code;
        }

        public void setShg_code(String shg_code) {
            this.shg_code = shg_code;
        }

        public String getMember_code() {
            return member_code;
        }

        public void setMember_code(String member_code) {
            this.member_code = member_code;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getClf_code() {
            return clf_code;
        }

        public void setClf_code(String clf_code) {
            this.clf_code = clf_code;
        }

        public String getClf_name() {
            return clf_name;
        }

        public void setClf_name(String clf_name) {
            this.clf_name = clf_name;
        }

        public String getVo_code() {
            return vo_code;
        }

        public void setVo_code(String vo_code) {
            this.vo_code = vo_code;
        }

        public String getVo_name() {
            return vo_name;
        }

        public void setVo_name(String vo_name) {
            this.vo_name = vo_name;
        }

        public String getMember_joining_date() {
            return member_joining_date;
        }

        public void setMember_joining_date(String member_joining_date) {
            this.member_joining_date = member_joining_date;
        }

        public String getLast_entry_after_nrlm() {
            return last_entry_after_nrlm;
        }

        public void setLast_entry_after_nrlm(String last_entry_after_nrlm) {
            this.last_entry_after_nrlm = last_entry_after_nrlm;
        }

        public String getLast_entry_before_nrlm() {
            return last_entry_before_nrlm;
        }

        public void setLast_entry_before_nrlm(String last_entry_before_nrlm) {
            this.last_entry_before_nrlm = last_entry_before_nrlm;
        }
    }
}
/*{
	"status": 1,
	"error": {
		"code": "E200",
		"message": "Success"
	},
	"location_master": [{
		"block_name": "ACHHNERA",
		"gp_name": "ABAHDE PURA",
		"village_code": "3120002002002",
		"village_name": "ABHADEPURA",
		"shg_name": null,
		"shg_code": null,
		"member_code": "2870810",
		"member_name": "RANJEETA",
		"clf_code": "UP/AG/AERA/19453",
		"clf_name": "JHASHI KI RANI  PRERNA MAHILA  SHANKUL SAMITI",
		"vo_code": "UP/AG/AERA/ABA/249109",
		"vo_name": "SAFALTA PRERNA GRAM SANGATHAN",
		"member_joining_date": null,
		"last_entry_after_nrlm": null,
		"last_entry_before_nrlm": null
	}]
}*/