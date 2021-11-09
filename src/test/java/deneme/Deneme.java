package deneme;

import deneme.pojo.location;
import goRest.model.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Deneme {

    @Test(groups = "Smoke")
    public void test1() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places[0].longitude", equalTo("-118.4065"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
        ;
    }

    @Test
    public void test2() {

        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(200)
                .log().all()
                .body("data[2].name", equalTo("Colin Schmidt"))
        ;
    }

    @Test
    public void test3() {

        String code = "90210";
        String country = "us";

        given()
                .pathParam("codePostal", code)
                .pathParam("country", country)
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{country}/{codePostal}")
                .then()
                .log().body()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
        ;
    }

    @Test
    public void test4() {

        for (int i = 90210; i <= 90213; i++) {

            given()
                    .pathParam("code", i)
                    .log().uri()
                    .when()
                    .get("http://api.zippopotam.us/us/{code}")
                    .then()
                    .log().body()
            ;
        }
    }

    @Test
    public void test5() {

        given()
                .when()
                .get("https://demo.mersys.io/school-service/api/countries")
                .then()
                .body(hasSize(641))
                .log().body()
        ;
    }

    @Test
    public void test6() {

        for (int i = 1; i <= 10; i++) {

            given()
                    .log().uri()
                    .param("page", i)
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(200)
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @BeforeClass
    public void beforeClass() {

        baseURI = "http://api.zippopotam.us";
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.METHOD)
                .log(LogDetail.STATUS)
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void test7() {

        given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                .spec(responseSpecification)
        ;
    }

    @Test
    public void test8() {

        String placeName = given()
                .when()
                .get("/us/90210")
                .then()
                .extract().path("places[0].'place name'");
        System.out.println("placeName = " + placeName);
    }

    @Test
    public void test9() {

        int pageCount = given()
                .param("page", 1)
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .spec(responseSpecification)
                .extract().path("meta.pagination.pages");
        System.out.println("pageCount = " + pageCount);
    }

    @Test
    public void test10() {

        List<Integer> idList = given()
                .param("page", 1)
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .spec(responseSpecification)
                .extract().path("data.id");
        System.out.println("idList = " + idList);
    }

    @Test
    public void test11() {

        List<String> villageList = given()
                .when()
                .get("/tr/18000")
                .then()
                .log().body()
                .body("places.'place name'", hasItem("Yukariçavuş Köyü"))
                .extract().path("places.'place name'");
        System.out.println("villageList = " + villageList);
        System.out.println("villageList.size() = " + villageList.size());
        Assert.assertTrue(villageList.contains("Aşağipelitözü Köyü"));
    }

    @Test
    public void test12() {

        location location = given()
                .when()
                .get("/us/90210")
                .then()
                .log().body()
                .extract().as(location.class);
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces() = " + location.getPlaces());
        System.out.println("location.getCountryAbbreviation() = " + location.getCountryAbbreviation());
        System.out.println("location.getPostCode() = " + location.getPostCode());
        System.out.println("location = " + location);
    }

    @Test
    public void test13() {

        List<User> userList = given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .extract().jsonPath().getList("data", User.class);
        System.out.println("userList = " + userList);
        for (User u : userList) {
            System.out.println("user = " + u);
        }
    }


    int userID = 0;

    @Test
    public void test14() {

        userID = given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Erdal Bakkal\", \"gender\":\"male\", \"email\":\"" + randomEmail() + "\", \"status\":\"active\"}")
                .when()
                .post("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("userID = " + userID);
    }

    public String randomEmail() {

        String randomString = RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString + "@hotmail.com";
    }

    @Test(dependsOnMethods = {"test14"}, priority = 1)
    public void test15() {

        given()
                .pathParam("userID", userID)
                .when()
                .get("https://gorest.co.in/public/v1/users/{userID}")
                .then()
                .body("data.id", equalTo(userID))
        ;
        System.out.println("userID = " + userID);
    }

    @Test(dependsOnMethods = {"test14"}, priority = 2)
    public void test16() {

        String name = "Ahmet Hamdi Tanpınar";
        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\"}")
                .pathParam("userID", userID)
                .when()
                .put("https://gorest.co.in/public/v1/users/{userID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.name", equalTo(name))
        ;
    }

    @Test(dependsOnMethods = {"test14"}, priority = 3)
    public void test17() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("userID", userID)
                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")
                .then()
                .statusCode(204)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = {"test17"}, priority = 4)
    public void test18() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("userID", userID)
                .when()
                .delete("https://gorest.co.in/public-api/users/{userID}")
                .then()
                .statusCode(200)
                .log().body()
                .body("code", equalTo(404))
        ;
    }


}
