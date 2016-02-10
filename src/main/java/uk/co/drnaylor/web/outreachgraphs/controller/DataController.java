package uk.co.drnaylor.web.outreachgraphs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.drnaylor.web.outreachgraphs.data.BinData;
import uk.co.drnaylor.web.outreachgraphs.dataservice.DataHolder;
import uk.co.drnaylor.web.outreachgraphs.dataservice.H2DataService;

import java.time.Instant;
import java.util.List;

@RestController
public class DataController {

    private final DataHolder holder;
    private final H2DataService service;

    @Autowired
    public DataController(DataHolder holder, H2DataService service) {
        this.holder = holder;
        this.service = service;
    }

    @RequestMapping(value = "/getdata", method = {RequestMethod.GET})
    public List<BinData> getBinData() {
        return holder.getData();
    }

    @RequestMapping(value = "/gettimedata", method = {RequestMethod.GET})
    public List<BinData> getBinData(@RequestParam(value = "from", required = false, defaultValue = "0") long from, @RequestParam(value = "to", required = false, defaultValue = "0") long to) {
        if (to == 0) {
            return holder.getData(Instant.ofEpochSecond(from), Instant.now());
        }

        return holder.getData(Instant.ofEpochSecond(from), Instant.ofEpochSecond(to));
    }

    @RequestMapping(value = "/postdata", method = {RequestMethod.POST})
    public boolean postData(@RequestParam(value = "bin", required = true) int bin) {
        holder.addData(bin);
        return true;
    }

    @RequestMapping(value = "/save")
    public boolean save() {
        return true;
    }
}
