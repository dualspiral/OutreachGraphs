package uk.co.drnaylor.web.outreachgraphs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.drnaylor.web.outreachgraphs.data.DataToReturn;
import uk.co.drnaylor.web.outreachgraphs.dataservice.StateHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    private final StateHolder holder;

    @Autowired
    public DataController(StateHolder holder) {
        this.holder = holder;
    }

    @RequestMapping(value = "/getdata", method = {RequestMethod.GET})
    public DataToReturn getBinData(@RequestParam(value = "all", defaultValue = "false", required = false) boolean all) {
        return holder.getData(all);
    }

    @RequestMapping(value = "/postdata", method = {RequestMethod.POST})
    public boolean postData(@RequestParam(value = "time", required = true) long timeInSeconds, @RequestParam(value = "bin", required = true) int bin) {
        holder.addData(timeInSeconds, bin);
        return true;
    }

    @RequestMapping(value = "/setstate", method = {RequestMethod.POST})
    public boolean setState(@RequestParam(value = "state", defaultValue = "all") String state) {
        switch (state.toLowerCase()) {
            case "current":
                holder.setState(StateHolder.ShowState.CURRENT);
                break;
            case "allcurrent":
                holder.setState(StateHolder.ShowState.ALL_CURRENT);
                break;
            default:
                holder.setState(StateHolder.ShowState.ALL);
                break;
        }

        return true;
    }

    @RequestMapping(value = "/commitdata", method = {RequestMethod.POST})
    public boolean commitData() {
        holder.mergeData();
        return true;
    }

    @RequestMapping(value = "/discarddata", method = {RequestMethod.POST})
    public boolean discardData() {
        holder.clearTransient();
        return true;
    }

    @RequestMapping(value = "/getState", method = {RequestMethod.GET})
    public Map<String, String> getState() {
        Map<String, String> s = new HashMap<>();
        s.put("state", holder.getState().name());
        return s;
    }

    @RequestMapping(value = "/save")
    public boolean save() {
        try {
            holder.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @RequestMapping(value = "/keepalive")
    public boolean keepAlive() {
        return true;
    }
}
