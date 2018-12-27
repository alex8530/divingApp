package Modle;

import com.diverapp.diverapp.Trip;

import java.util.HashMap;
import java.util.Map;

public class Order {
//    private int id;
    private String prodictId;
    private String prodictName;
    private int quantity;
    private double price;
    private int discount;
    private String providerID;
    private Trip trip;
    private String bankAccount;
    public Order(){

    }

    public Order(String prodictId,String provider, String prodictName, int quantity, double price, int discount ,String bankAccount) {
//        this.id = id;
        this.prodictId = prodictId;
        this.prodictName = prodictName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.bankAccount = bankAccount;
        this.providerID = provider;
    }
    public Order(Map<String,Object> map){
    }
    public Trip getTrip() {
        return trip;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getProviderID() {
        return providerID;
    }
    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

//    public int getId() {
//        return id;
//    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
        result.put("prodictId", prodictId);
        result.put("prodictName", prodictName);
        result.put("quantity",quantity);
        result.put("price",price);
        result.put("discount",discount);
        result.put("providerID",providerID);
        return result;
    }
    public String getProdictId() {
        return prodictId;
    }

    public String getProdictName() {
        return prodictName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public void setProdictId(String prodictId) {
        this.prodictId = prodictId;
    }

    public void setProdictName(String prodictName) {
        this.prodictName = prodictName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

//    public Object getId() {
//        return id;
//    }
}
