package romanmoran.com.mappingyou.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 24.07.2017.
 */


@org.parceler.Parcel
public class User extends Item implements Parcelable {
    public static String TAG = User.class.getCanonicalName();

    public String vkId;
    public String fbId;
    public String email;
    public String PhNumber;
    public String DateOfBirth;
    public String Gener;
    public String VisFl;
    public String LastTime;
    public String Status ;
    public String token;
    public String pass ;
    public List<Event> events ;
    //public LatLng mPosition;
    public  String date;


    public String text;

    public String destination_address;
    public String message;
    public Uri uri;

    public Uri getUri() {
        return uri;
    }


    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }

    //public LatLng getmPosition() {return mPosition;}

    //public void setmPosition(LatLng mPosition) {this.mPosition = mPosition;}


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User(){}

    public User(String email, String pass, String sign) {
        this.email = email;
        this.pass = pass;
        this.sign = sign;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhNumber() {
        return PhNumber;
    }

    public void setPhNumber(String phNumber) {
        PhNumber = phNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGener() {
        return Gener;
    }

    public void setGener(String gener) {
        this.Gener = gener;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getVisFl() {
        return VisFl;
    }

    public void setVisFl(String visFl) {
        VisFl = visFl;
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

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }


    /*@Override
    public LatLng getPosition() {
        *//*double x = Double.parseDouble(X);
        double y = Double.parseDouble(Y);
        return new LatLng(x,y);*//*
        return mPosition;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.vkId);
        dest.writeString(this.fbId);
        dest.writeString(this.email);
        dest.writeString(this.PhNumber);
        dest.writeString(this.DateOfBirth);
        dest.writeString(this.Gener);
        dest.writeString(this.VisFl);
        dest.writeString(this.LastTime);
        dest.writeString(this.Status);
        dest.writeString(this.token);
        dest.writeString(this.pass);
        dest.writeList(this.events);
        dest.writeString(this.date);
        dest.writeString(this.text);
        dest.writeString(this.destination_address);
        dest.writeString(this.message);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.event, flags);
        dest.writeParcelable(this.mPosition, flags);
        dest.writeString(this.Name);
        dest.writeString(this.picUrl);
        dest.writeString(this.X);
        dest.writeString(this.Y);
        dest.writeString(this.sign);
        dest.writeString(this.Desc);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.vkId = in.readString();
        this.fbId = in.readString();
        this.email = in.readString();
        this.PhNumber = in.readString();
        this.DateOfBirth = in.readString();
        this.Gener = in.readString();
        this.VisFl = in.readString();
        this.LastTime = in.readString();
        this.Status = in.readString();
        this.token = in.readString();
        this.pass = in.readString();
        this.events = new ArrayList<Event>();
        in.readList(this.events, Event.class.getClassLoader());
        this.date = in.readString();
        this.text = in.readString();
        this.destination_address = in.readString();
        this.message = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.event = in.readParcelable(Event.class.getClassLoader());
        this.mPosition = in.readParcelable(LatLng.class.getClassLoader());
        this.Name = in.readString();
        this.picUrl = in.readString();
        this.X = in.readString();
        this.Y = in.readString();
        this.sign = in.readString();
        this.Desc = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
