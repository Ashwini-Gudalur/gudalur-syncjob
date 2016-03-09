package org.bahmni.custom.data.dao;

import org.bahmni.custom.data.model.VisitDetail;

import java.util.Date;
import java.util.List;

/**
 * Created by sandeepe on 26/02/16.
 */
public interface VistiDetailDAO {
    List<VisitDetail> getVisitDetails(Date lastSuccessfulRunDate, Date now);
}
