package ensp.reseau.wiatalk.ui;

import java.io.Serializable;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class IntentExtra {
    private String TAG;
    private Serializable SerializableValue;
    private Integer intValue;
    private String stringValue;

    public IntentExtra(String TAG, Serializable serializableValue, int intValue, String stringValue) {
        this.TAG = TAG;
        SerializableValue = serializableValue;
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public IntentExtra(String TAG, Serializable serializableValue) {
        this.TAG = TAG;
        SerializableValue = serializableValue;
    }

    public IntentExtra(String TAG, int intValue) {
        this.TAG = TAG;
        this.intValue = intValue;
    }

    public IntentExtra(String TAG, String stringValue) {
        this.TAG = TAG;
        this.stringValue = stringValue;
    }

    public String getTAG() {
        return TAG;
    }

    public Serializable getSerializableValue() {
        return SerializableValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
