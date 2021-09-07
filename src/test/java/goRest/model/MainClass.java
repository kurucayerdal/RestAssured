package goRest.model;

import java.util.List;

public class MainClass {

    private Meta meta;
    private List<Comments> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Comments> getData() {
        return data;
    }

    public void setData(List<Comments> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MainClass{" +
                "meta=" + meta +
                ", comments=" + data +
                '}';
    }
}
