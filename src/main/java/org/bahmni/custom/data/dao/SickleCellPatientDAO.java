package org.bahmni.custom.data.dao;

import org.bahmni.custom.data.model.Patient;
import org.bahmni.custom.util.SyncException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface SickleCellPatientDAO
{
	void addPatients(List<Patient> enrolledPatient) throws SyncException;

	Timestamp getLastSuccessfulRunDate();
}




