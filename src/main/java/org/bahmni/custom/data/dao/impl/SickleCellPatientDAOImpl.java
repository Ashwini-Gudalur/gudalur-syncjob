package org.bahmni.custom.data.dao.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bahmni.custom.data.dao.SickleCellPatientDAO;
import org.bahmni.custom.data.model.Patient;
import org.bahmni.custom.util.SyncException;
import org.bahmni.custom.util.Utils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class SickleCellPatientDAOImpl extends JdbcDaoSupport implements SickleCellPatientDAO
{
	final static Logger logger = Logger.getLogger(SickleCellPatientDAOImpl.class);
	public static final String SYNCJOB_SICKLE_CELL_PATIENT = "syncjob_sickle_cell_patient";

	public void addPatients(List<Patient> enrolledPatient) throws SyncException {
		for (Patient patient : enrolledPatient) {
			int openERPPatientId = Utils.getOpenERPPatientId(patient, getJdbcTemplate(), logger);
			if (openERPPatientId>0){
				try {
					insertSCPatient(patient, openERPPatientId);
				}catch (Exception e){
					logger.error("Failed to insert patient :"+ patient.toString(), e);
					throw new SyncException("Failed to insert patient :"+ patient.toString());
				}
			}else {
				logger.error("Patient Not found in ERP :-"+ patient);
				throw new SyncException("Patient Not found in ERP :-"+ patient);
			}
		}
	}

	private void insertSCPatient(Patient patient, int openERPPatientId) throws ParseException {
		String sql = "insert into " + SYNCJOB_SICKLE_CELL_PATIENT + " (erp_id,date) values (?,?)";
		getJdbcTemplate().update(sql,openERPPatientId,Utils.convertISTToGMT(patient.getDate()));
	}

	public Timestamp getLastSuccessfulRunDate() {
		return Utils.getMaxDate(SYNCJOB_SICKLE_CELL_PATIENT, "date", getJdbcTemplate(), logger);
	}

}




