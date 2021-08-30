package deneme.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class place {

    private String placeName;
    private String longitude;
    private String state;
    private String stateAbbreviation;
    private String latitude;

    @JsonProperty("place name")
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("state abbreviation")
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getState() {
        return state;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "place{" +
                "placeName='" + placeName + '\'' +
                ", longitude='" + longitude + '\'' +
                ", state='" + state + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
