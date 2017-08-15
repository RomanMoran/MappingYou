package romanmoran.com.mappingyou.data;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

/**
 * Created by roman on 24.07.2017.
 */

@Parcel
public class Event extends Item implements Parcelable {

    public String StartDat ;
    public String EndDate ;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStartDat() {
        return StartDat;
    }

    public void setStartDat(String startDat) {
        StartDat = startDat;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.StartDat);
        dest.writeString(this.EndDate);
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

    public Event() {
    }

    protected Event(android.os.Parcel in) {
        this.id = in.readString();
        this.StartDat = in.readString();
        this.EndDate = in.readString();
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

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(android.os.Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
