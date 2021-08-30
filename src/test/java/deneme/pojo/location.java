package deneme.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class location {


    private String postCode = "90210";
    private String country = "United States";
    private String countryAbbreviation = "US";
    private List<place> places;


    @JsonProperty("post code")
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("country abbreviation")
    public void setCountryAbbreviation(String countryAbbreviation) {
        this.countryAbbreviation = countryAbbreviation;
    }

    public void setPlaces(List<place> places) {
        this.places = places;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryAbbreviation() {
        return countryAbbreviation;
    }

    public List<place> getPlaces() {
        return places;
    }

    @Override
    public String toString() {
        return "location{" +
                "postCode='" + postCode + '\'' +
                ", country='" + country + '\'' +
                ", countryAbbreviation='" + countryAbbreviation + '\'' +
                ", places=" + places +
                '}';
    }
}
