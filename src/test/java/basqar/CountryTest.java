package basqar;

import basqar.model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

//    url : https://demo.mersys.io/auth/login
//    giden body :
//    {
//        "username": "richfield.edu",
//            "password": "Richfield2020!",
//            "rememberMe": true
//    }

//    POST : token oluşturulduğu için

    private Cookies cookies;
    private Country country = new Country();
    private String randomCountryName = randomString(7);
    private String randomCountryCode = randomString(3);
    private String countryID;

    @BeforeClass
    public void loginBasqar() {

        baseURI = "https://demo.mersys.io";

        Map<String, String> credential = new HashMap<>();
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");

        cookies = given()
                .body(credential)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                //.log().body()
                .extract().response().getDetailedCookies()
        ;
    }

    @Test
    public void createCountry() {

        country.setName(randomCountryName);
        country.setCode(randomCountryCode);

        countryID = given()
                .cookies(cookies)  // login ile gelen bilgiyi token vs. cookie olarak beforeclass ta aldık ve şimdi burada gönderiyoruz
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(201)
                .body("name", equalTo(randomCountryName))
                .body("code", equalTo(randomCountryCode))
                .log().body()
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = {"createCountry"})
    public void createCountryNegative() {

        country.setName(randomCountryName);
        country.setCode(randomCountryCode);

        given()
                .cookies(cookies)  // login ile gelen bilgiyi token vs. cookie olarak beforeclass ta aldık ve şimdi burada gönderiyoruz
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(400)
                .body("message", equalTo("The Country with Name \"" + randomCountryName + "\" already exists."))
                .log().body();
    }

    @Test(dependsOnMethods = {"createCountryNegative"})
    public void updateCountry() {

        String cntryName = randomString(8);
        String cntryCode = randomString(3);
        country.setName(cntryName);
        country.setCode(cntryCode);
        country.setId(countryID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
                .log().body()
                .body("name", equalTo(cntryName))
                .body("code", equalTo(cntryCode))
                .body("id", equalTo(countryID))
        ;
    }

    @Test(dependsOnMethods = {"updateCountry"})
    public void deleteCountry() {

        given()
                .cookies(cookies)
                .pathParam("countryID", countryID)
                .when()
                .delete("/school-service/api/countries/{countryID}")
                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = {"deleteCountry"})
    public void deleteCountryNegative() {

        given()
                .cookies(cookies)
                .pathParam("countryID", countryID)
                .when()
                .delete("/school-service/api/countries/{countryID}")
                .then()
                .statusCode(404)
        ;
    }


    public String randomString(int count) {
        return RandomStringUtils.randomAlphabetic(count).toUpperCase();
    }
}
