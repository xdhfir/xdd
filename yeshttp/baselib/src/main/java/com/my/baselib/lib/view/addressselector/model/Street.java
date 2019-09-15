package com.my.baselib.lib.view.addressselector.model;

import java.io.Serializable;

public class Street implements Serializable {
    public long id;
    public long county_id;
    public String name;

    @Override
    public String toString() {
        return "Street{" +
                "id=" + id +
                ", county_id=" + county_id +
                ", name='" + name + '\'' +
                '}';
    }
}