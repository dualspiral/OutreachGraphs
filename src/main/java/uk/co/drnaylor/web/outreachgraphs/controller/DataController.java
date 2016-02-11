package uk.co.drnaylor.web.outreachgraphs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.drnaylor.web.outreachgraphs.data.DataToReturn;
import uk.co.drnaylor.web.outreachgraphs.dataservice.H2DataService;
import uk.co.drnaylor.web.outreachgraphs.dataservice.StateHolder;

@RestController
public class DataController {

    private final StateHolder holder;
    private final H2DataService service;

    @Autowired
    public DataController(StateHolder holder, H2DataService service) {
        this.holder = holder;
        this.service = service;
    }

    @RequestMapping(value = "/getdata", method = {RequestMethod.GET})
    public DataToReturn getBinData() {
        return holder.getData();
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

    @RequestMapping(value = "/save")
    public boolean save() {
        return true;
    }
}
