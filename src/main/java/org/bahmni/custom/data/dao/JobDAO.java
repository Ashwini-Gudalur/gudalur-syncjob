package org.bahmni.custom.data.dao;

import org.bahmni.custom.data.model.Job;

import java.util.Date;

/**
 * Created by sandeepe on 26/02/16.
 */
public interface JobDAO {
    public Date getLastSuccessfulRunDate();

    Job addJob(Job job);

    void updateJob(Job job);
}
