package jznv.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DataSet {
    private final String name;
    private final List<Data> data = new ArrayList<>();

    public DataSet(String name, Data... data) {
        this.name = name;
        this.data.addAll(List.of(data));
    }

    public void add(Data data) {
        this.data.add(data);
    }
}