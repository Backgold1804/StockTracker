package com.example.stocktracker.TableFragment;


public class TableItemData {
    String country;
    String title;
    String ticker;
    int current_price;
    int yesterday_price;
    int average_unit_price;
    int holding_quantity;
    int profit;
    float profit_rate;
    float holding_weight;
    int date;
    String exchange;
    int unit_price;


    public TableItemData(String country, String title, String ticker, int current_price, int yesterday_price, int average_unit_price, int holding_quantity, int profit, float profit_rate, float holding_weight, int date, String exchange, int unit_price) {
        this.country = country;
        this.title = title;
        this.ticker = ticker;
        this.current_price = current_price;
        this.yesterday_price = yesterday_price;
        this.average_unit_price = average_unit_price;
        this.holding_quantity = holding_quantity;
        this.profit = profit;
        this.profit_rate = profit_rate;
        this.holding_weight = holding_weight;
        this.date = date;
        this.exchange = exchange;
        this.unit_price = unit_price;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicker() { return ticker; }
    public void setTicker(String code) { this.ticker = ticker; }

    public int getCurrentPrice() { return current_price; }
    public void setCurrentPrice(int current_price) { this.current_price = current_price; }

    public int getYesterdayPrice() { return yesterday_price; }
    public void setYesterdayPrice(int yesterday_price) { this.yesterday_price = yesterday_price; }

    public int getAverageUnitPrice() { return average_unit_price; }
    public void setAverageUnitPrice(int average_unit_price) { this.average_unit_price = average_unit_price; }

    public int getHoldingQuantity() { return holding_quantity; }
    public void setHoldingQuantity(int holding_quantity) { this.holding_quantity = holding_quantity; }

    public int getProfit() { return profit; }
    public void setProfit(int profit) { this.profit = profit; }

    public float getProfitRate() { return profit_rate; }
    public void setProfitRate(float profit_rate) { this.profit_rate = profit_rate; }

    public float getHoldingWeight() { return holding_weight; }
    public void setHoldingWeight(float holding_weight) { this.holding_weight = holding_weight; }

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }

    public String getExchange() {
        return exchange;
    }
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getUnitPrice() { return unit_price; }
    public void setUnitPrice(int unit_price) { this.unit_price = unit_price; }
}
