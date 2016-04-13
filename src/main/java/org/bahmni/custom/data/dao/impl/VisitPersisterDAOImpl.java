package org.bahmni.custom.data.dao.impl;

import org.apache.log4j.Logger;
import org.bahmni.custom.data.dao.VisitPersisterDAO;
import org.bahmni.custom.data.model.VisitDetail;
import org.bahmni.custom.util.SyncException;
import org.bahmni.custom.util.Utils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by sandeepe on 26/02/16.
 */
public class VisitPersisterDAOImpl extends JdbcDaoSupport implements VisitPersisterDAO {

    final static Logger logger = Logger.getLogger(VisitPersisterDAOImpl.class);
    static List<String> OPD_VT = Arrays.asList("General", "OPD", "Followup", "Emergency");
    static List<String> IPD_VT = Arrays.asList("IPD");

    public void saveVisits(List<VisitDetail> visitDetails) throws SyncException {
        for (VisitDetail visitDetail : visitDetails) {
            int openERPPatientId = Utils.getOpenERPPatientId(visitDetail, getJdbcTemplate(), logger);
            int visitTypeId = getVisitTypeId(visitDetail.getVisitType());
            if (openERPPatientId>0){
                try {
                    insertVisit(visitDetail, openERPPatientId, visitTypeId);
                }catch (Exception e){
                    logger.error("Failed to insert Visit :"+ visitDetail.toString(), e);
                    throw new SyncException("Failed to insert visit:"+visitDetail.toString());
                }
            }else {
                logger.error("Patient Not found in ERP :-"+ visitDetail);
                throw new SyncException("Patient Not found in ERP :-"+ visitDetail);
            }
        }
    }

    private int getVisitTypeId(String visitType) {
        if (OPD_VT.contains(visitType)){
            return Utils.VISIT_TYPES.OPD.ordinal();
        }
        if (IPD_VT.contains(visitType)){
            return Utils.VISIT_TYPES.IPD.ordinal();
        }
        return Utils.VISIT_TYPES.UNKNOWN.ordinal();
    }
//Date is kept in IST since mysql has data in ist and posgres has it in GMT
    private void insertVisit(VisitDetail detail, int openERPPatientId, int visitTypeId) throws ParseException {
        String sql = "insert into syncjob_visit (erp_patient_id,diagnoses,visit_uuid,visit_startdate" +
                ",visit_stopdate,visit_type,visit_type_id,date) values (?,?,?,?,?,?,?,?)";
        getJdbcTemplate().update(sql,openERPPatientId, detail.getDiagnosis(),detail.getVisitUuid(),Utils.convertISTToGMT(detail.getStartDate())
                , Utils.convertISTToGMT(detail.getStopDate()), detail.getVisitType(), visitTypeId, Utils.convertISTToGMT(detail.getDate()));
        insertPatientExtras(detail, openERPPatientId);
    }


    private void insertPatientExtras(VisitDetail detail, int openERPPatientId) throws ParseException {
        if (!patientSynced(openERPPatientId)) {
            String sql = "insert into syncjob_patient_extn (erp_id,gender,birthdate) values (?,?,?)";
            getJdbcTemplate().update(sql, openERPPatientId, detail.getGender().name(), Utils.convertISTToGMT(detail.getBirthDate()));
        }
    }

    private boolean patientSynced(int openERPPatientId) {
        String sql = "SELECT count(erp_id) FROM syncjob_patient_extn where erp_id=?";
        Integer ret = null;
        try {
            ret = getJdbcTemplate().queryForObject(sql, new Object[]{openERPPatientId},Integer.class);
        } catch (Exception e) {
            logger.error("Cannot get Last Successful run time Setting default: ", e);
        }
        if (ret == null || ret <1) {
            return false;
        }
        return true;
    }

    public Timestamp getLastSuccessfulRunDate() {
        return Utils.getMaxDate("syncjob_visit", "date", getJdbcTemplate(), logger);
    }

}
