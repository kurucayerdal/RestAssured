package deneme;

import goRest.model.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Deneme2 {

    private int id;
    private String random;

    @Test(priority = 1)
    public void getUserList(){
        List<User> userList =
                given()
                        .when()
                        .get("https://gorest.co.in/public-api/users")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("data",not(empty()))
                        .extract().jsonPath().getList("data",User.class)
                ;  for (User user: userList) {

            System.out.println(user + " \n");
        }
    }

    private String getRandomEmail(){

        return RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
    }

    @Test(priority = 2)
    public void createUser(){
        id=
                given()
                        .header("Authorization","Bearer 1c4a732f4fefeb5b4b335ed36eea45888797ff613b7d116e3daa39afbf20b98f")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"Ömer Özdemir\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")
                        .when()
                        .post("https://gorest.co.in/public-api/users")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .body("code",equalTo(201))
                        .extract().jsonPath().get("data.id")
        ;

        random =
                given()
                        .header("Authorization","Bearer 1c4a732f4fefeb5b4b335ed36eea45888797ff613b7d116e3daa39afbf20b98f")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"Ömer Özdemir\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")
                        .when()
                        .post("https://gorest.co.in/public-api/users")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .body("code",equalTo(201))
                        .extract().jsonPath().getString("data.email");
        System.out.println(random);
    }

    @Test(dependsOnMethods = "createUser")
    public void CreateUserNgtv(){

        given()
                .header("Authorization","Bearer 1c4a732f4fefeb5b4b335ed36eea45888797ff613b7d116e3daa39afbf20b98f")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Ömer Özdemir\", \"gender\":\"male\", \"email\":\"" + random + "\", \"status\":\"active\"}")
                .when()
                .post("https://gorest.co.in/public-api/users")
                .then()
                .log().body()
                .body("code",equalTo(422));
        System.out.println(random);
    }

    @Test(priority = 3,dependsOnMethods = "createUser")
    public void getId(){

        given()
                .pathParam("id",id)
                .when()
                .get("https://gorest.co.in/public-api/users/{id}")
                .then()
                .statusCode(200)
                .body("data.id",equalTo(id));
        System.out.println("id : " + id);
    }

    @Test(priority = 4)
    public void update(){

        String updateName = "ahmet Özdemir";

        given()
                .header("Authorization","Bearer 1c4a732f4fefeb5b4b335ed36eea45888797ff613b7d116e3daa39afbf20b98f")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"ahmet Özdemir\"}")
                .pathParam("id",id)
                .when()
                .put("https://gorest.co.in/public-api/users/{id}")
                .then()
                .statusCode(200)
                .body("data.id",equalTo(id))
                .body("data.name",equalTo(updateName))
                .log().body();
    }

    @Test(priority = 5)
    public void delete(){
        given()
                .header("Authorization","Bearer 1c4a732f4fefeb5b4b335ed36eea45888797ff613b7d116e3daa39afbf20b98f")
                .contentType(ContentType.JSON)
                .pathParam("id",id)
                .when()
                .delete("https://gorest.co.in/public-api/users/{id}")
                .then()
                .statusCode(200)
                .body("code",equalTo(204))
        ;
    }

    @Test(priority = 6)
    public void deleteNgtv(){
        given()
                .header("Authorization","Bearer 1c4a732f4fefeb5b4b335ed36eea45888797ff613b7d116e3daa39afbf20b98f")
                .contentType(ContentType.JSON)
                .pathParam("id",id)
                .when()
                .delete("https://gorest.co.in/public-api/users/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("code",equalTo(404))
        ;
    }
}
