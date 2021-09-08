package goRest.model;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GoRestPostsTest {

    @BeforeClass
    public void beforeClass()
    {
        baseURI = "https://gorest.co.in/public/v1";
    }

    @Test
    public void getAllPosts() {
// Task 1 : https://gorest.co.in/public/v1/posts  API sinden dönen data bilgisini bir class yardımıyla
        // List ini alınız.

       List<Posts> list=
        given()
                .when()
                .get("/posts")
                .then()
                .log().body()
                .extract().jsonPath().getList("data",Posts.class)
        ;
      for (Posts p : list)
      {
          System.out.println("post = " + p);
      }
    }

    @Test
    public void getAPost() {
        // Task 2 : https://gorest.co.in/public/v1/posts  API sinden sadece 1 kişiye ait postları listeletiniz.
        //  https://gorest.co.in/public/v1/users/87/posts

        List<Posts> list=
                given()
                        .when()
                        .get("/users/87/posts")
                        .then()
                        .log().body()
                        .extract().jsonPath().getList("data",Posts.class)
                ;
        for (Posts p : list)
        {
            System.out.println("post = " + p);
        }
    }

    Response response;
    int postId=0;

    @Test(priority = 1)
    public void createAPost() {

        // Task 4 : https://gorest.co.in/public/v1/posts  API sine 87 nolu usera ait bir post create ediniz.

        String sendBody= "Rest Assured test body";
        String title="Test restAssured";

        Posts posts=new Posts();
        posts.setBody(sendBody);
        posts.setTitle(title);

        response=
        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body(posts)
                .when()
                .post("/users/87/posts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().response()
        ;
        String returnTitle= response.jsonPath().getString("data.title");
        String returnBody= response.jsonPath().getString("data.body");
        postId=response.jsonPath().getInt("data.id");

        Assert.assertEquals(sendBody,returnBody);
        Assert.assertEquals(title,returnTitle);
    }

    @Test(enabled = false)
    public void createAPost_2() {

        // Task 4 : https://gorest.co.in/public/v1/posts  API sine 87 nolu usera ait bir post create ediniz.

        String sendBody= "Rest Assured test body";
        String title="Test restAssured";

        Posts posts=new Posts();
        posts.setBody(sendBody);
        posts.setTitle(title);
        posts.setUser_id(87);

        response=
                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .contentType(ContentType.JSON)
                        .body(posts)
                        .when()
                        .post("/posts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response()
        ;
        String returnTitle= response.jsonPath().getString("data.title");
        String returnBody= response.jsonPath().getString("data.body");
        postId=response.jsonPath().getInt("data.id");

        Assert.assertEquals(sendBody,returnBody);
        Assert.assertEquals(title,returnTitle);
    }

    @Test(dependsOnMethods = "createAPost")
    public void getAUserPost() {

// Task 5 : Create edilen Post ı get yaparak id sini kontrol ediniz.

                given()
                        .pathParam("postId",postId)
                        .when()
                        .get("posts/{postId}")
                        .then()
                        .statusCode(200)
                        .body("data.id",equalTo(postId))
                ;
    }


    @Test(dependsOnMethods = {"createAPost"},priority = 2)
    public void updateAPost() {

        // Task 6 : Create edilen Post un body sini güncelleyerek, bilgiyi kontrol ediniz.

        String sendBody= "MySQL i sakın unutma!";

        response=
                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .contentType(ContentType.JSON)
                        .body("{\n" + " \"body\": \""+sendBody+"\"\n" + "}")
                        .pathParam("postId",postId)
                        .when()
                        .log().uri()
                        .put("/posts/{postId}")
                        .then()
                        .log().body()
                        .extract().response()
                ;

        String returnBody= response.jsonPath().getString("data.body");

        Assert.assertEquals(sendBody,returnBody);
    }

    @Test(dependsOnMethods = {"createAPost"},priority = 3)
    public void deleteAPost() {

                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .pathParam("postId",postId)
                        .when()
                        .delete("/posts/{postId}")
                        .then()
                        .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = {"createAPost"},priority = 4)
    public void deleteAPostNegatif() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("postId",postId)
                .when()
                .delete("/posts/{postId}")
                .then()
                .statusCode(404)
        ;
    }

    @Test
    public void getAllPostsAsObject() {
// Task 3 : https://gorest.co.in/public/v1/posts  API sinden dönen bütün bilgileri tek bir nesneye atınız

       PostsMain postsMain=
                given()
                        .when()
                        .get("/posts")
                        .then()
                        .extract().as(PostsMain.class);

        System.out.println("getTotal() = " + postsMain.getMeta().getPagination().getTotal());
        System.out.println("getData() = " +  postsMain.getData().get(1).getBody());
        System.out.println("getTitle() = " + postsMain.getData().get(4).getTitle());
        System.out.println("getTitle() = " + postsMain.getData().get(5).getId());
        System.out.println("getTitle() = " + postsMain.getData().get(3).getUser_id());
    }
}
