package com.example.stocktracker.TableFragment;

public class ChartData {
    private String chart_stock_name;
    private int chart_current_price;
    private int chart_holdings;
    private float chart_holding_weight;

    public String getChart_stock_name() {
        return chart_stock_name;
    }

    public void setChart_stock_name(String chart_stock_name) {
        this.chart_stock_name = chart_stock_name;
    }

    public int getChart_current_price() {
        return chart_current_price;
    }

    public void setChart_current_price(int chart_current_price) {
        this.chart_current_price = chart_current_price;
    }

    public int getChart_holdings() {
        return chart_holdings;
    }

    public void setChart_holdings(int chart_holdings) {
        this.chart_holdings = chart_holdings;
    }

    public float getChart_holding_weight() {
        return chart_holding_weight;
    }

    public void setChart_holding_weight(float chart_holding_weight) {
        this.chart_holding_weight = chart_holding_weight;
    }

}
