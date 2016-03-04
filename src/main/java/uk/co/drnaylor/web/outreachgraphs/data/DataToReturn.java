package uk.co.drnaylor.web.outreachgraphs.data;

import uk.co.drnaylor.web.outreachgraphs.dataservice.StateHolder;

import java.util.Map;
import java.util.stream.Collectors;

public class DataToReturn {

    public final String title;

    public final StateHolder.ShowState state;

    public final Map<Integer, Long> dataMap;

    public final long[][] data;

    public final long numberOfThrows;

    public DataToReturn(String title, Map<Integer, Long> all, Map<Integer, Long> one, StateHolder.ShowState state) {
        this.title = title;
        this.dataMap = all;

        this.state = state;

        this.data = new long[2][StateHolder.maxBin - StateHolder.minBin + 1];

        for (int i = StateHolder.minBin; i <= StateHolder.maxBin; i++) {
            data[0][i - StateHolder.minBin] = all.getOrDefault(i, 0L);
            data[1][i - StateHolder.minBin] = one.getOrDefault(i, 0L);
        }

        this.numberOfThrows = all.values().stream().collect(Collectors.summingLong(x -> x));
    }
}
