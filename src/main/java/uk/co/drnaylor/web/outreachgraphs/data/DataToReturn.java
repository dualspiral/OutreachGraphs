package uk.co.drnaylor.web.outreachgraphs.data;

import uk.co.drnaylor.web.outreachgraphs.dataservice.StateHolder;

import java.util.Map;

public class DataToReturn {

    public final String title;

    public final long[][] data;

    public DataToReturn(String title, Map<Integer, Long> all, Map<Integer, Long> one) {
        this.title = title;

        this.data = new long[2][StateHolder.maxBin - StateHolder.minBin + 1];

        for (int i = StateHolder.minBin; i <= StateHolder.maxBin; i++) {
            data[0][i - StateHolder.minBin] = all.getOrDefault(i, 0L);
            data[1][i - StateHolder.minBin] = one.getOrDefault(i, 0L);
        }
    }
}
