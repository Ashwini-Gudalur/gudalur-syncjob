package org.bahmni.custom.data.dao.impl;

import org.apache.log4j.Logger;
import org.bahmni.custom.data.dao.ProgramDAO;
import org.bahmni.custom.data.model.Patient;
import org.bahmni.custom.util.Utils;
import org.bahmni.custom.util.Utils.GENDER;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sandeepe on 26/02/16.
 */
public class ProgramDAOImpl extends JdbcDaoSupport implements ProgramDAO {

    final static Logger logger = Logger.getLogger(ProgramDAOImpl.class);

    public List<Patient> getEnrolledPatient(Date startDate, Date endDate) {
        String sql = "select pp.patient_id,p.uuid, pi.identifier,p.gender,pp.date_enrolled from patient_program pp " +
                "LEFT JOIN person p on person_id=patient_id " +
                "LEFT JOIN patient_identifier pi on pi.patient_id=pp.patient_id " +
                "where pp.program_id=(select program_id from program where name='" +
                Utils.SICKLE_CELL_PROGRAM_NAME+"') " +
                "and pp.date_enrolled>? and pp.date_enrolled<=? ORDER by pp.date_enrolled ASC ";
        List<Patient> patients = getJdbcTemplate().query(sql,new Object[]{startDate,endDate}, new RowMapper<Patient>() {
            public Patient mapRow(ResultSet rs, int rownumber) throws SQLException {
                Patient e = new Patient();
                e.setId(rs.getInt(1));
                e.setPatientUuid(rs.getString(2));
                e.setBahmniId(rs.getString(3));
                e.setGender(GENDER.valueOf(rs.getString(4)));
                e.setDate(rs.getTimestamp(5));
                return e;
            }
        });
        if (patients==null){
            patients = new ArrayList(0);
        }
        return patients;
    }
}
