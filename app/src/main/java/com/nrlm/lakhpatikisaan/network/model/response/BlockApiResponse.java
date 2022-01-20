package com.nrlm.lakhpatikisaan.network.model.response;

import com.google.gson.Gson;

import java.util.ArrayList;
public class BlockApiResponse{

    public static BlockApiResponse jsonToJava(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, BlockApiResponse.class);
    }

    public String javaToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public int status;
    public Error error;
    public ArrayList<BlockResponse> blockResponse;


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

    public ArrayList<BlockResponse> getBlockResponse() {
        return blockResponse;
    }

    public void setBlockResponse(ArrayList<BlockResponse> blockResponse) {
        this.blockResponse = blockResponse;
    }

    public static class BlockResponse{
        public String block_code;
        public String block_name;


        public String getBlock_code() {
            return block_code;
        }

        public void setBlock_code(String block_code) {
            this.block_code = block_code;
        }

        public String getBlock_name() {
            return block_name;
        }

        public void setBlock_name(String block_name) {
            this.block_name = block_name;
        }

    }

    public static class Error{
        public String code;
        public String message;


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