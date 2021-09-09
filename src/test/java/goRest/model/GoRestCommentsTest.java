package goRest.model;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GoRestCommentsTest {

    @Test
    public void commentsTest() {
// Task 1: https://gorest.co.in/public/v1/comments  Api sinden dönen verilerdeki data yı bir nesne yardımıyla
        //         List olarak alınız.
        List<Comments> commentsList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .extract().jsonPath().getList("data", Comments.class);
        System.out.println("commentsList = " + commentsList);
    }

    @Test
    public void commentsEmailTest() {
        // Bütün Comment lardaki emailleri bir list olarak alınız ve
        // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.
        List<String> emailList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .extract().jsonPath().getList("data.email");
        System.out.println("emailList = " + emailList);
       // Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test
    public void commentsEmailTest2() {
        // Bütün Comment lardaki emailleri bir list olarak alınız ve
        // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.
        List<String> emailList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .extract().path("data.email");
        System.out.println("emailList = " + emailList);
        //Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test
    public void commentsEmailTest3() {
        // Bütün Comment lardaki emailleri bir list olarak alınız ve
        // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .extract().response();
        List<String> emailList = response.jsonPath().getList("data.email", String.class);//String.class yazmasak da olur
        System.out.println("emailList = " + emailList);
       // Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
    }

    @Test
    public void commentsAllInfo() {
        // Task 3 : https://gorest.co.in/public/v1/comments  Api sinden
        // dönen bütün verileri tek bir nesneye dönüştürünüz
        MainClass mainClass =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .extract().as(MainClass.class);

        System.out.println("5. index email = " + mainClass.getData().get(5).getEmail());
        System.out.println("mainClass.getData().get(3).getName() = " + mainClass.getData().get(3).getName());
        System.out.println("current link = " + mainClass.getMeta().getPagination().getLinks().getCurrent());

    }

    int commentId = 0;

    @Test(priority = 1)
    public void createAComment() {
        // Task 4 : https://gorest.co.in/public/v1/comments  Api sine
        // 1 Comments Create ediniz.

        Comments comments = new Comments();
        comments.setName("Erdal Assurence");
        comments.setEmail("erdalassurence@gmail.com");
        comments.setBody("Nous vous garantissons votre future!");

        commentId =
                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .contentType(ContentType.JSON)
                        .body(comments)
                        .when()
                        .post("https://gorest.co.in/public/v1/posts/68/comments")
                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("data.id")
        ;
    }

    @Test(dependsOnMethods = {"createAComment"},priority = 2)
    public void updateAComment() {
        // Task 5 : https://gorest.co.in/public/v1/comments  Api sine
        // 1 Comments update ediniz.

        String postBody="Kurs bitince hepimize hemen işe girmeyi nasip et Allah'ım, Amin!";

        String body=
        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("commentId",commentId)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"body\": \""+postBody+"\" \n" +
                        "}")
                .when()
                .put("https://gorest.co.in/public/v1/comments/{commentId}")
                .then()
                .log().body()
                .extract().jsonPath().getString("data.body")
        ;
        Assert.assertEquals(body,postBody);
    }

    @Test(dependsOnMethods = {"createAComment"},priority = 3)
    public void getAComment() {
        // Task 5 : https://gorest.co.in/public/v1/comments  Api sine
        // 1 Comments update ediniz.

                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .pathParam("commentId",commentId)
                        .when()
                        .get("https://gorest.co.in/public/v1/comments/{commentId}")
                        .then()
                        .log().body()
                        .body("data.id",equalTo(commentId))
                        .statusCode(200)
                ;
    }

    @Test(dependsOnMethods = {"createAComment"},priority = 4)
    public void deleteAComment() {
        // Task 6 : Create edilen Comment ı siliniz. Status kodu kontorl ediniz 204
        // Delete https://gorest.co.in/public/v1/comments/1394

                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .pathParam("commentId",commentId)
                        .when()
                        .delete("https://gorest.co.in/public/v1/comments/{commentId}")
                        .then()
                        .statusCode(204)
                ;
    }

    @Test(dependsOnMethods = {"createAComment"},priority = 5)
    public void deleteACommentNegatif() {
        // Task 7 : silinen edilen Comment ı tekrar siliniz. Status kodu kontorl ediniz 404
        // Delete https://gorest.co.in/public/v1/comments/1394

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("commentId",commentId)
                .when()
                .delete("https://gorest.co.in/public-api/comments/{commentId}")
                .then()
                .statusCode(200)
        ;
    }
}
