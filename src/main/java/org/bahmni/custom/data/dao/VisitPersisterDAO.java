package org.bahmni.custom.data.dao;

import org.bahmni.custom.data.model.VisitDetail;
import org.bahmni.custom.util.SyncException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by sandeepe on 26/02/16.
 */
public interface VisitPersisterDAO {
    void saveVisits(List<VisitDetail> visitDetails) throws SyncException;

    Timestamp getLastSuccessfulRunDate();
}
