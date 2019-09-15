package com.my.baselib.lib.view.addressselector.model;

import java.io.Serializable;

public class City implements Serializable {
    public long id;
    public long province_id;
    public String name;

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", province_id=" + province_id +
                ", name='" + name + '\'' +
                '}';
    }
}