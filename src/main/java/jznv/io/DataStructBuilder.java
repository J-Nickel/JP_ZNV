package jznv.io;

public class DataStructBuilder<T> {
    private final String[] invalidValues;
    private final int inTableIndex_s;
    private final int inTableIndex_e;

    public DataStructBuilder(int s, int e, String[] invalidValues) {
        this.invalidValues = invalidValues;
        inTableIndex_s = s;
        inTableIndex_e = e;
    }
}