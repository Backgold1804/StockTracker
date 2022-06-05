package com.example.stocktracker.TableFragment;


import java.util.List;

public class TableItemData {
    int my_stock_uid;
    String country;
    String stock_name;
    String ticker;
    int current_price;
    int blended_price;
    int holdings;
    int profit;
    int amount_price;
    float profit_rate;
    List<TradingItemData> data_list;

    public int getMy_stock_uid() {
        return my_stock_uid;
    }

    public void setMy_stock_uid(int my_stock_uid) {
        this.my_stock_uid = my_stock_uid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(int current_price) {
        this.current_price = current_price;
    }

    public int getBlended_price() {
        return blended_price;
    }

    public void setBlended_price(int blended_price) {
        this.blended_price = blended_price;
    }

    public int getHoldings() {
        return holdings;
    }

    public void setHoldings(int holdings) {
        this.holdings = holdings;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public float getProfit_rate() {
        return profit_rate;
    }

    public void setProfit_rate(float profit_rate) {
        this.profit_rate = profit_rate;
    }

    public int getAmount_price() {
        return amount_price;
    }

    public void setAmount_price(int amount_price) {
        this.amount_price = amount_price;
    }

    public List<TradingItemData> getData_list() {
        return data_list;
    }

    public void setData_list(List<TradingItemData> data_list) {
        this.data_list = data_list;
    }
}
