package deneme.mersys;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MersysCountry {

    String token = "";

    @Test
    public void login() {

        token = given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"richfield.edu\",\"password\": \"Richfield2020!\"}")
                .when()
                .post("https://demo.mersys.io/auth/login")
                .then()
                .log().body()
                .statusCode(200)
                .extract().jsonPath().getString("access_token")
        ;
    }

    @Test(dependsOnMethods = {"login"})
    public void getCountryList() {

        List<String> countryListUI = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("https://demo.mersys.io/school-service/api/countries")
                .then()
                .extract().jsonPath().getList("name");
//        for (String s : countryListUI) {
//            System.out.println("s = " + s);
//        }
    }


    Response response;

    @Test(dependsOnMethods = {"login"},priority = 1)
    public void createCountry() {

        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": null,\n" +
                        "    \"name\": \"" + randomCountry() + "\",\n" +
                        "    \"code\": \"" + randomCountryCode() + "\",\n" +
                        "    \"translateName\": [],\n" +
                        "    \"hasState\": false\n" +
                        "}")
                .when()
                .post("https://demo.mersys.io/school-service/api/countries")
                .then()
                .statusCode(201)
                .log().body()
                .extract().response()
        ;
    }


    @Test(dependsOnMethods = {"login"},priority = 2)
    public void creatreCountryNegatif() {

        String countryName=response.jsonPath().getString("name");
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": null,\n" +
                        "    \"name\": \"" + countryName + "\",\n" +
                        "    \"code\": \"" + randomCountryCode() + "\",\n" +
                        "    \"translateName\": [],\n" +
                        "    \"hasState\": false\n" +
                        "}")
                .when()
                .post("https://demo.mersys.io/school-service/api/countries")
                .then()
                .log().body()
                .body("status",equalTo(400))
                .body("message",containsString("already exists."))
        ;
    }

    @Test(dependsOnMethods = {"login"},priority = 3)
    public void editCountry() {

        String countryID=response.jsonPath().getString("id");

        System.out.println("countryID = " + countryID);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": \""+countryID+"\",\n" +
                        "    \"name\": \""+randomCountry()+"\",\n" +
                        "    \"code\": \"ERD\",\n" +
                        "    \"translateName\": [],\n" +
                        "    \"hasState\": false\n" +
                        "}")
                .when()
                .put("https://demo.mersys.io/school-service/api/countries")
                .then()
                .statusCode(200)
                .log().body()
                .body("id",equalTo(countryID))
                .body("code",equalTo("ERD"))
        ;
    }

    @Test(dependsOnMethods = {"login"},priority = 4)
    public void deleteCountry() {

        String countryID=response.jsonPath().getString("id");

        System.out.println("countryID = " + countryID);

        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("countryID",countryID)
                .when()
                .delete("https://demo.mersys.io/school-service/api/countries/{countryID}")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = {"login"},priority = 5)
    public void editCountryNegatif() {

        String countryID=response.jsonPath().getString("id");

        System.out.println("countryID = " + countryID);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"id\": \""+countryID+"\",\n" +
                        "    \"name\": \""+randomCountry()+"\",\n" +
                        "    \"code\": \"ERD\",\n" +
                        "    \"translateName\": [],\n" +
                        "    \"hasState\": false\n" +
                        "}")
                .when()
                .log().uri()
                .put("https://demo.mersys.io/school-service/api/countries")
                .then()
                .log().body()
        ;
    }


    @Test(dependsOnMethods = {"login"},priority = 6)
    public void deleteCountryNegatif() {

        String countryID=response.jsonPath().getString("id");

        System.out.println("countryID = " + countryID);

        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("countryID",countryID)
                .log().uri()
                .when()
                .delete("https://demo.mersys.io/school-service/api/countries/{countryID}")
                .then()
                .body("status",equalTo(404))
        ;
    }

    public String randomCountry() {

        return  RandomStringUtils.randomAlphabetic(8);
    }

    public String randomCountryCode() {

        return randomCountry().toUpperCase().substring(0, 3);
    }
}
