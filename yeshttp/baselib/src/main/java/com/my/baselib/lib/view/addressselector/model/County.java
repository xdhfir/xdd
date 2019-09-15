package com.my.baselib.lib.view.addressselector.model;

import java.io.Serializable;

public class County implements Serializable {
    public long id;
    public long city_id;
    public String name;

    @Override
    public String toString() {
        return "County{" +
                "id=" + id +
                ", city_id=" + city_id +
                ", name='" + name + '\'' +
                '}';
    }
}