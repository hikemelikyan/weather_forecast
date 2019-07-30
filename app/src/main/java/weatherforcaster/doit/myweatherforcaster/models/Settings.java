package weatherforcaster.doit.myweatherforcaster.models;

import android.content.Context;
import android.content.SharedPreferences;

import weatherforcaster.doit.myweatherforcaster.common.Common;

public class Settings {
    private Context mContext;
    private SharedPreferences mShared;


    private String unitType;
    private String frequency;
    private boolean switchPosition;

    public Settings(Context mContext) {
        this.mContext = mContext;
        mShared = mContext.getSharedPreferences(Common.SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        if (mShared.getString("unit", "").equals("imperial")) {
            unitType = "°F";
        } else if (mShared.getString("unit", "").equals("metric")) {
            unitType = "°C";
        }
        frequency = mShared.getString("frequency", "");
        switchPosition = mShared.getBoolean("switch", false);
    }

    public boolean getSwitchPosition() {
        return switchPosition;
    }

    public void setSwitchPosition(boolean switchPosition) {
        this.switchPosition = switchPosition;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
