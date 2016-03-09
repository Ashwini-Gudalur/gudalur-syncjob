package org.bahmni.custom.common;

import org.bahmni.custom.data.dao.*;
import org.bahmni.custom.data.model.Job;
import org.bahmni.custom.data.model.Patient;
import org.bahmni.custom.data.model.VisitDetail;
import org.bahmni.custom.util.SyncException;
import org.bahmni.custom.util.Utils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");

        JobDAO jobDAO = (JobDAO) context.getBean("jobDAO");

        Timestamp now = new Timestamp(new Date().getTime());
        Job job = new Job();
        job.setDate(now);
        job.setStatus(Utils.STATUS.STARTED.name());
        job.setLogFile(Utils.getLogFile(now));
        job = jobDAO.addJob(job);

        try {
            SickleCellPatientDAO sickleCellPatientDAO = (SickleCellPatientDAO) context.getBean("sickleCellPatientDAO");

            Timestamp lastSuccessfulRunDateForPatient = sickleCellPatientDAO.getLastSuccessfulRunDate();
            ProgramDAO programDAO = (ProgramDAO) context.getBean("programDAO");
            List<Patient> enrolledPatient = programDAO.getEnrolledPatient(lastSuccessfulRunDateForPatient, now);

            sickleCellPatientDAO.addPatients(enrolledPatient);

            VisitPersisterDAO visitPersisterDAO = (VisitPersisterDAO) context.getBean("visitPersisterDAO");
            Timestamp lastSuccessfulRunDateForVisit = visitPersisterDAO.getLastSuccessfulRunDate();
            VistiDetailDAO vistiDetailDAO = (VistiDetailDAO) context.getBean("vistiDetailDAO");
            List<VisitDetail> visitDetails = vistiDetailDAO.getVisitDetails(lastSuccessfulRunDateForVisit, now);
            visitPersisterDAO.saveVisits(visitDetails);
            job.setStatus(Utils.STATUS.SUCCESS.name());
            jobDAO.updateJob(job);
        } catch (SyncException e) {
            e.printStackTrace();
            job.setStatus(Utils.STATUS.FAILED.name());
            jobDAO.updateJob(job);
        }

        
    }
}
