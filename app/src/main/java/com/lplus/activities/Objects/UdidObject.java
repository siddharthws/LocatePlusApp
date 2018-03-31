package com.lplus.activities.Objects;

/**
 * Created by Rohan on 3/31/2018.
 */

public class UdidObject {
    String name;
    String udid;

    public UdidObject(String name, String udid) {
        this.name = name;
        this.udid = udid;
    }

    public UdidObject () {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }
}
