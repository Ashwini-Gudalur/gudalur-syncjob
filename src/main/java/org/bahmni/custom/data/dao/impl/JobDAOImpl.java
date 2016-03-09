package org.bahmni.custom.data.dao.impl;

import org.apache.log4j.Logger;
import org.bahmni.custom.data.dao.JobDAO;
import org.bahmni.custom.data.model.Job;
import org.bahmni.custom.util.Utils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sandeepe on 26/02/16.
 */
public class JobDAOImpl extends JdbcDaoSupport implements JobDAO {

    final static Logger logger = Logger.getLogger(JobDAOImpl.class);
    public static final String SYNCJOB_JOB = "syncjob_job";

    public Timestamp getLastSuccessfulRunDate() {
        return Utils.getMaxDate(SYNCJOB_JOB, "date", getJdbcTemplate(), logger);
    }



    public Job addJob(Job job) {
        String sql = "INSERT INTO " + SYNCJOB_JOB + " " +
                "(status, logFile, date) VALUES (?, ?, ?)";
        getJdbcTemplate().update(
                sql, job.getStatus(), job.getLogFile(), job.getDate());
        Integer jobId = getJdbcTemplate().queryForObject("SELECT last_value FROM " + SYNCJOB_JOB + "_id_seq", Integer.class);
        job.setId(jobId);
        return job;
    }

    public void updateJob(Job job) {
        String sql = "UPDATE " + SYNCJOB_JOB + " set status=?, logFile=?, date=? where id=?";
        java.sql.Date sqlDate = new java.sql.Date(job.getDate().getTime());
        getJdbcTemplate().update(
                sql, job.getStatus(), job.getLogFile(), sqlDate,job.getId());
    }
}
