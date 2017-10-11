package indi.sunyc.footprints.domain.footprint.dao.impl;

import indi.sunyc.footprints.domain.footprint.dao.IFootprintDao;
import indi.sunyc.footprints.domain.footprint.entity.Footprint;
import indi.sunyc.mongo.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by ChamIt-001 on 2017/10/11.
 */
@Repository("FootprintDaoImpl")
public class FootprintDaoImpl extends BaseDaoImpl implements IFootprintDao {

    @Override
    public Class getEntityClass() {
        return Footprint.class;
    }
}
