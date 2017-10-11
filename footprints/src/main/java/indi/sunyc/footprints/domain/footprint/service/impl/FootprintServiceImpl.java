package indi.sunyc.footprints.domain.footprint.service.impl;

import indi.sunyc.base.common.model.Result;
import indi.sunyc.base.util.DateUtil;
import indi.sunyc.base.util.IdGenerateUtil;
import indi.sunyc.footprints.domain.footprint.dao.IFootprintDao;
import indi.sunyc.footprints.domain.footprint.entity.Footprint;
import indi.sunyc.footprints.domain.footprint.service.IFootprintService;
import indi.sunyc.footprints.domain.message.entity.Message;
import indi.sunyc.footprints.domain.place.entity.Marker;
import indi.sunyc.footprints.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
@Service("FootprintServiceImpl")
public class FootprintServiceImpl implements IFootprintService {

    @Autowired
    IFootprintDao footprintDao;

    @Override
    public Result create(User user, Marker marker, Message message) {
        Footprint footprint = new Footprint();
        footprint.setId(IdGenerateUtil.getInstance().nextId().toString());
        footprint.setUser(user);
        footprint.setMarker(marker);
        footprint.setMessage(message);
        footprint.setDate(DateUtil.newDateTimeString());
        footprintDao.save(footprint);
        return Result.newSuccResult(footprint);
    }


}
