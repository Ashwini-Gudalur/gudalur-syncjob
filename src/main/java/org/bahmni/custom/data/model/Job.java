package org.bahmni.custom.data.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sandeepe on 26/02/16.
 */
public class Job {

    private int id;

    private Timestamp date;

    private String status;

    private String logFile;

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
