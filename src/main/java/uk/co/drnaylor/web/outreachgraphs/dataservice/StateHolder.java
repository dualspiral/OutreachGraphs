package uk.co.drnaylor.web.outreachgraphs.dataservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import uk.co.drnaylor.web.outreachgraphs.SaveDaemon;
import uk.co.drnaylor.web.outreachgraphs.data.DataToReturn;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StateHolder {

    public final static int minBin = -8;
    public final static int maxBin = 8;

    private final Map<Long, Integer> data = new HashMap<>();
    private final Map<Long, Integer> currentThrowData = new HashMap<>();
    private Map<Integer, Long> cache = null;
    private ShowState state = ShowState.ALL;
    private DataToReturn returnData = null;
    private DataToReturn returnDataAll = null;
    private final Path file = Paths.get("data.json");

    @Autowired
    private ApplicationContext appContext;

    public StateHolder() throws IOException {
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        Thread tr = new Thread(new SaveDaemon(this));

        // This thread doesn't block the JVM shutdown.
        tr.setDaemon(true);
        tr.start();
    }

    public synchronized void load() throws IOException {
        if (Files.exists(file)) {
            BufferedReader br = new BufferedReader(new FileReader(file.toFile()));
            Gson gson = new Gson();
            Map<Long, Integer> m = gson.getAdapter(new TypeToken<Map<Long, Integer>>() {}).fromJson(br);
            data.clear();
            data.putAll(m);
        }
    }

    public synchronized void save() throws IOException {
        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        FileWriter bw = new FileWriter(file.toFile(), false);
        bw.write(new Gson().getAdapter(new TypeToken<Map<Long, Integer>>(){}).toJson(data));
        bw.close();
    }

    public DataToReturn getData(boolean all) {
        if (returnData == null) {
            returnData = new DataToReturn(this.state.title, getAll(false), getCurrent(false), state);
            returnDataAll = new DataToReturn(this.state.title, getAll(true), getCurrent(true), state);
        }

        return all ? returnDataAll : returnData;
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

    public void clearTransient() {
        currentThrowData.clear();
        returnData = null;
    }

    public void addData(long milliseconds, int bin) {
        if (bin <= maxBin && bin >= minBin) {
            currentThrowData.put(milliseconds, bin);
            returnData = null;
        }
    }

    private Map<Integer, Long> getAll(boolean all) {
        if (!all && this.state == ShowState.CURRENT) {
            return new HashMap<>();
        }

        if (cache == null) {
            cache = data.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            for (int i = minBin; i <= maxBin; i++) {

                // Make sure the map still has the key in it.
                if (!cache.containsKey(i)) {
                    cache.put(i, 0L);
                }
            }
        }

        return cache;
    }

    private Map<Integer, Long> getCurrent(boolean all) {
        if (!all && this.state == ShowState.ALL || currentThrowData.isEmpty()) {
            return new HashMap<>();
        }

        return currentThrowData.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public enum ShowState {
        CURRENT("Throw Distribution (Current only)"),
        ALL_CURRENT("Throw Distribution"),
        ALL("Throw Distribution");

        final String title;

        ShowState(String title) {
            this.title = title;
        }
    }
}
