package goRest.model;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class GoRestTodosTest {


    @Test
    public void getAllPosts() {

        given()
                .when()
                .get("https://gorest.co.in/public/v1/todos")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    Response response;
    int todoId =0;

    @Test(priority = 1)
    public void createATodo() {

        String title="Chaque matin on a une réunion a 9h15.";

        response=
                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .contentType(ContentType.JSON)
                        .body("{\n" +
                                "    \"title\": \""+title+"\",\n" +
                                "    \"status\": \"pending\"\n" +
                                "}")
                        .when()
                        .post(" https://gorest.co.in/public/v1/users/46/todos")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response()
        ;
        String returnTitle= response.jsonPath().getString("data.title");
        todoId =response.jsonPath().getInt("data.id");

        Assert.assertEquals(title,returnTitle);
    }

    @Test(dependsOnMethods = {"createATodo"},priority = 2)
    public void updateAPost() {

        String title="CV Hazırlama Teknikleri";

        response=
                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .contentType(ContentType.JSON)
                        .body("{\n" +
                                " \"title\": \""+title+"\",\n"
                                +"\"status\": \"pending\""+
                                "    }")
                        .pathParam("todoId",todoId)
                        .when()
                        .log().uri()
                        .put(" https://gorest.co.in/public/v1/todos/{todoId}")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response()
        ;
        String returnTitle= response.jsonPath().getString("data.title");

        Assert.assertEquals(title,returnTitle);
    }

    @Test(dependsOnMethods = {"createATodo"},priority = 3)
    public void deleteAPost() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("todoId",todoId)
                .when()
                .delete(" https://gorest.co.in/public/v1/todos/{todoId}")
                .then()
                .statusCode(204)
        ;
    }

    @Test(priority = 4)
    public void deleteAPostNegatif() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("todoId",todoId)
                .when()
                .delete(" https://gorest.co.in/public/v1/todos/{todoId}")
                .then()
                .statusCode(404)
        ;
    }
}
