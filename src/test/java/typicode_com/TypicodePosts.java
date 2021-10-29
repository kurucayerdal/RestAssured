package typicode_com;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TypicodePosts {

    private Posts posts = new Posts();

    @BeforeClass
    public void beforeClass() {
        baseURI = "https://jsonplaceholder.typicode.com/posts";
    }

    @Test(priority = 1)
    public void getAllPosts() {

        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(priority = 2)
    public void getAPost() {

        given()
                .pathParam("id", 1)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(priority = 3)
    public void postAPostWithObject() {

        posts.setUserId(1);
        posts.setBody("barabar");
        posts.setTitle("fricky");

        given()
                .contentType(ContentType.JSON)
                .body(posts)
                .when()
                .post()
                .then()
                .statusCode(201)
                .log().body()
        ;
    }

    @Test(priority = 4)
    public void postAPostWithMap() {

        String title = "fricky";
        String body = "barabar";
        String userId = "1";

        Map<String, String> postMap = new HashMap<>();
        postMap.put("title", title);
        postMap.put("body", body);
        postMap.put("userId", userId);

        given()
                .contentType(ContentType.JSON)
                .body(postMap)
                .when()
                .post()
                .then()
                .statusCode(201)
                .log().body()
                .body("title", equalTo(title))
                .body("body", equalTo(body))
                .body("userId", equalTo(userId))
        ;
    }

    @Test(priority = 5)
    public void updateAPost() {

        String body = "I wrote something for testing...";
        String title = "Test title";

        posts.setBody(body);
        posts.setTitle(title);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(posts)
                .when()
                .put("/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("title", equalTo(title))
                .body("body", equalTo(body))
        ;
    }

    @Test(priority = 6)
    public void deleteAPost() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(200)
        ;
    }

    @Test(priority = 7)
    public void getAllPostsInAList() {

        List<Posts> postsList =
                given()
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract().jsonPath().getList("", Posts.class);
        for (Posts p : postsList) {
            System.out.println("post = " + p);
        }
    }

}
