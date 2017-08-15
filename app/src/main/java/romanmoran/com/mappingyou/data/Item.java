package romanmoran.com.mappingyou.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

import romanmoran.com.mappingyou.utility.Utility;

/**
 * Created by roman on 31.07.2017.
 */

public class Item implements ClusterItem {

    public String id ;
    public User user;
    public Event event;
    public LatLng mPosition;
    public String Name ;
    public String picUrl;
    public String X ;
    public String Y ;
    public String sign ;

    public List<Event> eventList;
    public List<User> userList;

    public void setCoordinates(Location location){
        setX(Utility.getStringFromDouble(location.getLatitude()));
        setY(Utility.getStringFromDouble(location.getLongitude()));
    }

    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(Utility.getDoubleFromString(getX()));
        location.setLongitude(Utility.getDoubleFromString(getY()));
        return location;
    }

    public LatLng getLatLang(){
        return new LatLng(Utility.getDoubleFromString(getX()),Utility.getDoubleFromString(getY()));
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String Desc ;

    public LatLng getmPosition() {
        return mPosition;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public void setmPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }
}
