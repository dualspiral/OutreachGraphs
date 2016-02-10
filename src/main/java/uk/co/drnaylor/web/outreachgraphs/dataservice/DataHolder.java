package uk.co.drnaylor.web.outreachgraphs.dataservice;

import org.springframework.stereotype.Service;
import uk.co.drnaylor.web.outreachgraphs.data.BinData;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataHolder {

    private final Map<Long, Integer> data = new HashMap<>();
    private List<BinData> cache = null;

    public List<BinData> getData() {
        if (cache == null) {
            cache = data.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().map(x -> new BinData(x.getKey(), x.getValue())).collect(Collectors.toList());
        }

        return cache;
    }

    public List<BinData> getData(Instant from, Instant to) {
        return data.entrySet().stream().filter(x -> {
            Instant s  = Instant.ofEpochSecond(x.getKey());
            return s.isBefore(to) && s.isAfter(from);
        }).collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting())).entrySet().stream().map(x -> new BinData(x.getKey(), x.getValue())).collect(Collectors.toList());
    }

    public Instant getEarliestInstant() {
        Optional<Long> ol = data.keySet().stream().sorted().findFirst();
        if (ol.isPresent()) {
            return Instant.ofEpochSecond(ol.get());
        }

        return Instant.now();
    }

    public void addData(int bin) {
        cache = null;
        data.put(Instant.now().getEpochSecond(), bin);
    }
}
