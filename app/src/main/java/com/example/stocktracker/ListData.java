package com.example.stocktracker;

import java.util.List;
import java.util.Map;

public class ListData {
    public List<Map<String, Object>> datas;
    public String response_cd;
    public String response_msg;

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public String getResponse_cd() {
        return response_cd;
    }

    public String getResponse_msg() {
        return response_msg;
    }
}
