package uk.co.drnaylor.web.outreachgraphs;

import uk.co.drnaylor.web.outreachgraphs.dataservice.StateHolder;

import java.io.IOException;
import java.util.logging.Logger;

public class SaveDaemon implements Runnable {

    private final StateHolder holder;

    public SaveDaemon(StateHolder holder) {
        this.holder = holder;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Sleep thread has been interrupted");
                break;
            }

            try {
                holder.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while(true);
    }
}
