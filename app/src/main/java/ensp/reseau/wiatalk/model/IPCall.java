package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class IPCall {

    public static final int MODE_VOCAL = 1;
    public static final int MODE_VIDEO = 2;

    @SerializedName("_id") @Expose private String _id;
    @SerializedName("from") @Expose private User caller;
    @SerializedName("to") @Expose private User called;
    @SerializedName("date") @Expose private long call_timestamp;
    @SerializedName("duration") @Expose private int duration;
    @SerializedName("mode") @Expose private int mode;
    @SerializedName("type") @Expose private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String callerId;
    private String calledId;

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getCalledId() {
        return calledId;
    }

    public void setCalledId(String calledId) {
        this.calledId = calledId;
    }

    public IPCall() {
    }

    public IPCall(String _id, User caller, User called, long call_timestamp, int duration, int mode) {
        this._id = _id;
        this.caller = caller;
        this.called = called;
        this.call_timestamp = call_timestamp;
        this.duration = duration;
        this.mode = mode;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getCaller() {
        return caller;
    }

    public void setCaller(User caller) {
        this.caller = caller;
    }

    public User getCalled() {
        return called;
    }

    public void setCalled(User called) {
        this.called = called;
    }

    public long getCall_timestamp() {
        return call_timestamp;
    }

    public void setCall_timestamp(long call_timestamp) {
        this.call_timestamp = call_timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
