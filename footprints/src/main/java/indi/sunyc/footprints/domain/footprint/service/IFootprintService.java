package indi.sunyc.footprints.domain.footprint.service;

import indi.sunyc.base.common.model.Result;
import indi.sunyc.footprints.domain.message.entity.Message;
import indi.sunyc.footprints.domain.place.entity.Marker;
import indi.sunyc.footprints.domain.user.entity.User;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public interface IFootprintService {

    public Result create(User user, Marker marker, Message message);
}
