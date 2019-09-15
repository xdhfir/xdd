package com.my.baselib.lib.view.sortlistview;

/**
 * create by Administrator at 2017/3/30
 * description:
 */
public class ContactSortModel {
    private String name;//显示的数据
    private String sortLetters;//显示数据拼音的首字母

    public ContactSortModel() {
    }

    public ContactSortModel(String name, String sortLetters) {
        this.name = name;
        this.sortLetters = sortLetters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String toString() {
        return "ContactSortModel{" +
                "name='" + name + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                '}';
    }
}
