package org.bahmni.custom.util;

import org.apache.log4j.Logger;
import org.bahmni.custom.data.model.Patient;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sandeepe on 26/02/16.
 */
public class Utils {

    private static String LOG_PREFIX = "bahmni_custom_sync_";

    public static String SICKLE_CELL_PROGRAM_NAME = "Sickle Cell Anemia Program";

    public static String getLogFile(Date now) {
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        return LOG_PREFIX + format.format(now) + ".log";
    }

    public static int getOpenERPPatientId(Patient patient, JdbcTemplate jdbcTemplate, Logger logger) {
        String sql = "SELECT id from res_partner where uuid= ? and ref=?";
        Integer ret = -1;
        try {
            ret = jdbcTemplate.queryForObject(sql, Integer.class, patient.getPatientUuid(), patient.getBahmniId());
        } catch (Exception e) {
            logger.error("Cannot get Last Successful run time Setting default: ", e);
        }
        return ret;
    }

    public static Timestamp getMaxDate(String tableName, String date, JdbcTemplate jdbcTemplate, Logger logger) {
        String sql = "SELECT max(" + date + ") FROM " + tableName + "";
        Timestamp ret = null;
        try {
            ret = jdbcTemplate.queryForObject(sql, Timestamp.class);
        } catch (Exception e) {
            logger.error("Cannot get Last Successful run time Setting default: ", e);
        }
        if (ret == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1970, 0, 1);
            ret = new Timestamp(calendar.getTimeInMillis());
        }
        return ret;
    }


    public enum STATUS {STARTED, FAILED, SUCCESS}

    ;

    public enum GENDER {M, F, O}

    ;

    public enum VISIT_TYPES {UNKNOWN, IPD, OPD}

    ;

    public static Timestamp convertISTToGMT(Timestamp input) throws ParseException {
        SimpleDateFormat local = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        local.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = local.format(input);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Timestamp t = new Timestamp(formatter.parse(format).getTime());
        return t;
    }
}
