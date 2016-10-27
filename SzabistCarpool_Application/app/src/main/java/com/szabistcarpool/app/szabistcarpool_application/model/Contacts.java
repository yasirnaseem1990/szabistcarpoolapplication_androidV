package com.szabistcarpool.app.szabistcarpool_application.model;

/**
 * Created by ttwyf on 5/15/2016.
 */
public class Contacts {
    private String name;
    private String email;
    private String mobielno;

    public Contacts(String username,String useremail,String mobile){
        this.setName(username);
        this.setEmail(useremail);
        this.setMobielno(mobile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobielno() {
        return mobielno;
    }

    public void setMobielno(String mobielno) {
        this.mobielno = mobielno;
    }
}
