package com.example.stocktracker.TableFragment;

public class TradingItemData {
    int my_stock_uid;
    String date;
    String stock_name;
    String exchange;
    int order_price;
    int order_amount;

    public int getMy_stock_uid() {
        return my_stock_uid;
    }

    public void setMy_stock_uid(int my_stock_uid) {
        this.my_stock_uid = my_stock_uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }

    public int getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(int order_amount) {
        this.order_amount = order_amount;
    }
}
