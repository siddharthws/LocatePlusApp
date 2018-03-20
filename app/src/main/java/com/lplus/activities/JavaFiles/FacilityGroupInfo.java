package com.lplus.activities.JavaFiles;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/20/2018.
 */

public class FacilityGroupInfo {
    private String name;
    private ArrayList<FacilityChildInfo> list = new ArrayList<FacilityChildInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FacilityChildInfo> getList() {
        return list;
    }

    public void setList(ArrayList<FacilityChildInfo> list) {
        this.list = list;
    }
}
