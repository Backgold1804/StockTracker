package com.example.stocktracker.TableFragment;

public class ChartData {
    String chart_stock_name;
    int chart_current_price;
    int chart_holdings;
    float chart_profit_rate;

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

    public float getChart_profit_rate() {
        return chart_profit_rate;
    }

    public void setChart_profit_rate(float chart_profit_rate) {
        this.chart_profit_rate = chart_profit_rate;
    }
}
