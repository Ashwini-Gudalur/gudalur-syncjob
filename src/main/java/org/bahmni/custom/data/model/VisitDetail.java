package org.bahmni.custom.data.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sandeepe on 26/02/16.
 */
public class VisitDetail extends Patient {
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setStopDate(Timestamp stopDate) {
        this.stopDate = stopDate;
    }

    private Timestamp startDate;
    private Timestamp stopDate;
    private String visitUuid;
    private String visitType;
    private String diagnosis;

    public String getVisitUuid() {
        return visitUuid;
    }

    public void setVisitUuid(String visitUuid) {
        this.visitUuid = visitUuid;
    }

    @Override
    public String toString() {
        return "VisitDetail{" +
                "startDate=" + startDate +
                ", stopDate=" + stopDate +
                ", visitUuid='" + visitUuid + '\'' +
                ", visitType='" + visitType + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getStopDate() {
        return stopDate;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
