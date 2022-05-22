package com.example.stocktracker.TableFragment;

public class TradingData {
    private int date;
    private String exchange;
    private float unit_price;
    private int quantity;

    public TradingData(int date, String exchange, float unit_price, int quantity) {
        this.date = date;
        this.exchange = exchange;
        this.unit_price = unit_price;
        this.quantity = quantity;
    }

    public int getDate() { return date; }
    public void setDate(int date) { this.date = date; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public float getUnitPrice() { return unit_price; }
    public void setUnitPrice(float unit_price) { this.unit_price = unit_price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
