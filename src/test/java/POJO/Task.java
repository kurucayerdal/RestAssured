package POJO;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Task {

    /**
     * Task 1
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */

    @Test
    public void task1() {

        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }


    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     * expect BODY to be equal to "203 Non-Authoritative Information"
     */

    @Test
    public void task2() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))
        ;
    }

    @Test
    public void task2_2() {
        String bodyText =
                given()
                        .when()
                        .get("https://httpstat.us/203")
                        .then()
                        .statusCode(203)
                        .contentType(ContentType.TEXT)
                        .extract().body().asString();
        Assert.assertTrue(bodyText.equalsIgnoreCase("203 Non-Authoritative Information"));
    }


    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void task3() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;
    }


    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */
    @Test
    public void task4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;
    }

    @Test
    public void task4_2() {

        boolean completed =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("completed", equalTo(false))
                        .extract().path("completed");

        Assert.assertFalse(completed);
    }


    /**
     * Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     * title = "fugiat veniam minus"
     * userId = 1
     */
    @Test
    public void task5() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[2].title", equalTo("fugiat veniam minus"))
                .body("[2].userId", equalTo(1))
        ;
    }

    @Test
    public void task5_2() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title[2]", equalTo("fugiat veniam minus"))
                .body("userId[2]", equalTo(1))
        ;
    }


    /**
     * Task 6
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */

    @Test
    public void task6() {

        User user =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(User.class);

        System.out.println("user = " + user);
        System.out.println("user.getUserId() = " + user.getUserId());
        System.out.println("user.getTitle() = " + user.getTitle());
        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.isCompleted() = " + user.isCompleted());
    }


    /**
     * Task 7
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into Array of POJOs
     */

    @Test
    public void task7() {

        User[] users = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .extract().as(User[].class);
        System.out.println("users = " + Arrays.toString(users));
    }


    /** Task 8
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into List of POJOs
     */
    @Test
    public void task8() {

        Object[] users = Arrays.stream(given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .extract().as(User[].class)).toArray();

        System.out.println("users = " + Arrays.toString(users));

    }

    @Test
    public void task8_2() {

        User[] users = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .extract().as(User[].class);

        List<User> userList= Arrays.asList(users);

        System.out.println("userList = " + userList);
    }

}




