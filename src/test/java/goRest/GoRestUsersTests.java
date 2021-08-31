package goRest;

import goRest.model.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {


    @Test
    public void getUsers() {

        List<User> userList = given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                //.log().body()
                .extract().jsonPath().getList("data", User.class)
                //data'yı User class'ı olarak list'e dönüştür- aradaki bir elemanı alıp dönüştürmek için jsonPath kullanıyoruz
                //istediğimiz formata çevirebiliyoruz String, int float vs.
                //kök veriyi almak istesek as() metodu ile alabilirdik ama aradaki herhangi bir elemanı almak için
                // .extract().path kullansaydık 3-4 tane class yazmamız gerekecekti ama jsonPath bizi bu zahmetten kurtarıyor.
                ;

        for (User u : userList) {
            System.out.println("user = " + u);
        }

    }


    // Create User için POSTMAN de yapılanlar

    //    JSON olarak gidecek body  {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
//
//    header ın içine
//    Authorization  Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f
//
//    POST ile https://gorest.co.in/public/v1/users çağırdık
//    id yi okuduk ve global bir değişkene attık ki, diğer reqest larde kullanabilelim

    int userId = 0; // class içinde geçerli bir int tanımladık ve test içindeki json nesnesini ona atadık

    @Test
    public void createUser() {

        userId = given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"erdal keser\", \"gender\":\"male\", \"email\":\"" + randomEmail() + "\", \"status\":\"active\"}")
                .when()
                .post("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("userId = " + userId);

    }

    public String randomEmail() {

        String randomString = RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString + "@gmail.com";
    }

    @Test
    public void getUserById() {

        createUser();
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users" + "/" + userId)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }


    @Test(dependsOnMethods = "createUser")
    public void getUserById_2() {

        given()
                .pathParam("userId", userId)
                .when()
                .get("https://gorest.co.in/public/v1/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(userId))
        ;
    }

    @Test(dependsOnMethods = "createUser")
    public void updateUserById() {

        String isim = "ediz bahtiyar";

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + isim + "\"}")
                .pathParam("userId", userId)
                .when()
                .put("https://gorest.co.in/public/v1/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.name", equalTo(isim))
        ;


    }

}

