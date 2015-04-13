package br.com.altamira.report.manufacturing;

public class MfgOrderMaterial {

    private int amount;
    private String material;
    private String color;
    private int request;
    private String client;
    private String delivery;
    private int weightTotal;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public int getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(int weightTotal) {
        this.weightTotal = weightTotal;
    }

}
