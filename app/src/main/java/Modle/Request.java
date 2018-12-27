package Modle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    String name;
    String contact;
    double total;
    List<Order> tickets;
    String userUid;
    int numberOfTickets;

    public Request() {
    }

    public Request(String name, String contact, double total, List<Order> tickets,String userUid) {
        this.name = name;
        this.contact = contact;
        this.total = total;
        this.tickets = tickets;
        this.userUid = userUid;
    }
    public Request(Map<String,Object> map){
        this.userUid = (String) map.get("userUid");
        this.name = (String) map.get("name");
        this.contact = (String) map.get("contact");

        List<HashMap> maps = (List<HashMap>) map.get("tickets");
        this.numberOfTickets = map.size();
        this.total = (double) map.get("total");

    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
    public double getTotal() {
        return total;
    }

    public List<Order> getTickets() {
        return tickets;
    }

    public String getUserUid() {
        return userUid;
    }
}

