package com.nrlm.lakhpatikisaan.network.model.response;

import java.util.List;

public class CheckDeleteShgResponseBean {

    private int status;
    private Error error;
    private List<DeletedShgData> shg_data;

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

    public List<DeletedShgData> getShg_data() {
        return shg_data;
    }

    public void setShg_data(List<DeletedShgData> shg_data) {
        this.shg_data = shg_data;
    }

    public static class DeletedShgData{
        private String shg_code;

        public String getShg_code() {
            return shg_code;
        }

        public void setShg_code(String shg_code) {
            this.shg_code = shg_code;
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
    "shg_data": [
        {
            "shg_code": "246023"
        },
        {
            "shg_code": "247249"
        }
    ]
}*/