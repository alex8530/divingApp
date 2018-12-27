package Modle;

import java.util.Map;

public class Notifi {

    String type;
    String firstName;
    String secondName;

    public Notifi(String type ,String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.type = type;
    }

    public Notifi(Map<String,Object> map){
        this.secondName = (String)map.get("title");
        this.secondName = (String) map.get("body");
    }
    public void setTitle(String title) {
        this.firstName = title;
    }

    public void setBody(String body) {
        this.secondName = body;
    }

    public String getTitle() {
        return firstName;
    }

    public String getBody() {
        return secondName;
    }
}
