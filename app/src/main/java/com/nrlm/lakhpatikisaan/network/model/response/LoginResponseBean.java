package com.nrlm.lakhpatikisaan.network.model.response;

public class LoginResponseBean {

    private int status;
    private Error error;

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

  /*  {
        "status": 1,
            "error": {
        "code": "E200",
                "message": "Success"
    }
    }*/