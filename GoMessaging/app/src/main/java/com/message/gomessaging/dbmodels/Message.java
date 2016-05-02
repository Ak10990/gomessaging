package com.message.gomessaging.dbmodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.message.gomessaging.utils.DateUtils;

import java.util.Date;

/**
 * Created by Akanksha on 26/4/16.
 * Message Parcelable
 */
public class Message implements Parcelable {

    private long id;
    private Date date;
    private String user;
    private String body;
    private int type;

    public Message(long id, Date date, String user, String body, int type) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.body = body;
        this.type = type;
    }

    protected Message(Parcel in) {
        id = in.readLong();
        date = DateUtils.getDateTime(in.readString());
        user = in.readString();
        body = in.readString();
        type = in.readInt();
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getFromTo() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", date=" + date +
                ", fromTo='" + user + '\'' +
                ", body='" + body + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(date.toString());
        dest.writeString(user);
        dest.writeString(body);
        dest.writeInt(type);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
