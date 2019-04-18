package com.litvinov.globustest.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs")
public class ValCurs {

    @Attribute(name = "Date")
    private String date;
    @Attribute
    private String name;

    @ElementList(inline=true)
    private List<Valute> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Valute> getValutes() {
        return list;
    }

    public void setValutes(List<Valute> valutes) {
        this.list = valutes;
    }
}
