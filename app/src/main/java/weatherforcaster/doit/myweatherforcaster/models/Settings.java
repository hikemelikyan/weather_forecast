package weatherforcaster.doit.myweatherforcaster.models;

public class Settings {
    private String unitType = "°C";
    private String frequency = "Once a day";
    private boolean switchPosition = true;

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
