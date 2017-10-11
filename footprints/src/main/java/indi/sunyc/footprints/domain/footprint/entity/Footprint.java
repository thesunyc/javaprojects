package indi.sunyc.footprints.domain.footprint.entity;

import indi.sunyc.footprints.domain.message.entity.Message;
import indi.sunyc.footprints.domain.place.entity.Marker;
import indi.sunyc.footprints.domain.user.entity.User;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class Footprint {

    private String id;
    private Message message;
    private User user;
    private Marker marker;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
