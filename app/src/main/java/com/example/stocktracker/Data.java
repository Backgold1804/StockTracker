package com.example.stocktracker;

import java.util.Map;

public class Data {

    public Map<String, Object> datas;
    public String response_cd;
    public String response_msg;

    public Map<String, Object> getDatas() {
        return datas;
    }

    public String getResponse_cd() {
        return response_cd;
    }

    public String getResponse_msg() {
        return response_msg;
    }
}
