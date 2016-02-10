package uk.co.drnaylor.web.outreachgraphs.data;

public class BinData {

    private final long count;
    private final int bin;

    public BinData(int bin, long count) {
        this.bin = bin;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public int getBin() {
        return bin;
    }
}
