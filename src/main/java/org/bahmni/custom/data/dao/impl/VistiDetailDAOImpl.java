package org.bahmni.custom.data.dao.impl;

import org.bahmni.custom.data.dao.VistiDetailDAO;
import org.bahmni.custom.data.model.VisitDetail;
import org.bahmni.custom.util.Utils;
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
public class VistiDetailDAOImpl extends JdbcDaoSupport implements VistiDetailDAO{

    public List<VisitDetail> getVisitDetails(Date lastSuccessfulRunDate, Date now) {
        String sql = "select v.date_started,v.date_stopped,v.uuid,vt.name,p.uuid as patient_uuid,p.gender " +
                ",group_concat(cdv.name separator ';') as diagnoses, pi.identifier,p.birthdate  from visit v " +
                "LEFT JOIN visit_type vt on vt.visit_type_id=v.visit_type_id " +
                "  LEFT JOIN person p on p.person_id=v.patient_id " +
                "LEFT JOIN patient_identifier pi on pi.patient_id=v.patient_id "+
                "  LEFT JOIN confirmed_diagnosis_view cdv on cdv.visit_id=v.visit_id " +
                "where v.voided=0 and v.date_stopped>? and v.date_stopped<=? GROUP BY v.visit_id ORDER by v.date_stopped ASC ";
        //visit start, stop, IPD, OPD,Confirmed diagnosises
        List<VisitDetail> visits = getJdbcTemplate().query(sql,new Object[]{lastSuccessfulRunDate,now}, new RowMapper<VisitDetail>() {
            public VisitDetail mapRow(ResultSet rs, int rownumber) throws SQLException {
                VisitDetail e = new VisitDetail();
                e.setStartDate(rs.getTimestamp(1));
                e.setStopDate(rs.getTimestamp(2));
                e.setDate(rs.getTimestamp(2));
                e.setVisitUuid(rs.getString(3));
                e.setVisitType(rs.getString(4));
                e.setPatientUuid(rs.getString(5));
                e.setGender(Utils.GENDER.valueOf(rs.getString(6)));
                e.setDiagnosis(rs.getString(7));
                e.setBahmniId(rs.getString(8));
                e.setBirthDate(rs.getTimestamp(9));
                return e;
            }
        });
        if (visits==null){
            visits = new ArrayList(0);
        }
        return visits;

    }
}
