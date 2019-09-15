package com.my.baselib.lib.view.addressselector.model;

import java.io.Serializable;

public class Province implements Serializable{
    public long id;
    public String name;

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}