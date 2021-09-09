package goRest.model;

import java.util.List;

public class TodosMain {

    private Meta meta;
    private List<Todos> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Todos> getData() {
        return data;
    }

    public void setData(List<Todos> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TodosMain{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }
}
