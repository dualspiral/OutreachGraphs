package uk.co.drnaylor.web.outreachgraphs.dataservice;

import org.springframework.stereotype.Service;
import uk.co.drnaylor.web.outreachgraphs.data.DataToReturn;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StateHolder {

    public final static int minBin = -9;
    public final static int maxBin = 9;

    private final Map<Long, Integer> data = new HashMap<>();
    private final Map<Long, Integer> currentThrowData = new HashMap<>();
    private Map<Integer, Long> cache = null;
    private ShowState state = ShowState.ALL;
    private DataToReturn returnData = null;

    public DataToReturn getData() {
        if (returnData == null) {
            returnData = new DataToReturn(this.state.title, getAll(), getCurrent());
        }

        return returnData;
    }

    public Map<Integer, Long> getData(Instant from, Instant to) {
        return data.entrySet().stream().filter(x -> {
            Instant s  = Instant.ofEpochMilli(x.getKey());
            return s.isBefore(to) && s.isAfter(from);
        }).collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
    }

    public Instant getEarliestInstant() {
        Optional<Long> ol = data.keySet().stream().sorted().findFirst();
        if (ol.isPresent()) {
            return Instant.ofEpochMilli(ol.get());
        }

        return Instant.now();
    }

    public ShowState getState() {
        return this.state;
    }

    public void setState(ShowState state) {
        this.state = state;
        returnData = null;
    }

    public void mergeData() {
        data.putAll(currentThrowData);
        currentThrowData.clear();
        returnData = null;
        cache = null;
    }

    public void addData(long milliseconds, int bin) {
        if (bin <= maxBin && bin >= minBin) {
            currentThrowData.put(milliseconds, bin);
            returnData = null;
        }
    }

    private Map<Integer, Long> getAll() {
        if (this.state == ShowState.CURRENT) {
            return new HashMap<>();
        }

        if (cache == null) {
            cache = data.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        }

        return cache;
    }

    private Map<Integer, Long> getCurrent() {
        if (this.state == ShowState.ALL || currentThrowData.isEmpty()) {
            return new HashMap<>();
        }

        return currentThrowData.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public enum ShowState {
        CURRENT("Current Session: Distribution"),
        ALL_CURRENT("All throws vs. Current Session"),
        ALL("Distribution of all throws");

        final String title;

        ShowState(String title) {
            this.title = title;
        }
    }
}
