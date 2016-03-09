package org.bahmni.custom.data.dao;

import org.bahmni.custom.data.model.Patient;

import java.util.Date;
import java.util.List;

/**
 * Created by sandeepe on 26/02/16.
 */
public interface ProgramDAO {
    public List<Patient> getEnrolledPatient(Date startDate, Date endDate);
}
