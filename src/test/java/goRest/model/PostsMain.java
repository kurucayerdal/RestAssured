package goRest.model;

import java.util.List;

public class PostsMain {

    private Meta meta;
    private List<Posts> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Posts> getData() {
        return data;
    }

    public void setData(List<Posts> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PostsMain{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }
}
