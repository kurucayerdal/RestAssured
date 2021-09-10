package goRest.model;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GoRestTodosTest {


    @BeforeClass
    public void beforeClass() {
        baseURI = "https://gorest.co.in/public/v1";
    }

//Ödev
//    @Test
//    public void getFirstTodo(){
//
//        // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
//        // zaman olarak ilk todo nun hangi userId ye ait olduğunu bulunuz
//
//        String time=
//        given()
//                .when()
//                .get("/todos")
//                .then()
//                .log().body()
//                .extract().jsonPath().getString("")
//        ;
//    }

    int totalPage = 0;

    @Test
    public void getLastTodoId() {

        // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
        //en son todonun hangi userId ye ait olduğunu bulunuz

        List<Todos> todosList =
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .log().body()
                        .extract().jsonPath().getList("data", Todos.class);
        int maxId = 0;
        for (int i = 0; i < todosList.size(); i++) {
            if (todosList.get(i).getId() > maxId) {
                maxId = todosList.get(i).getId();
            }
        }
        System.out.println("maxId = " + maxId);

    }

    Response response;

    @Test
    public void totalPageFind() {

        response = given()
                .when()
                .get("/todos")
                .then()
                .extract().response();
        totalPage = response.jsonPath().getInt("meta.pagination.pages");

    }

    @Test(dependsOnMethods = "totalPageFind")
    public void getBiggestTodoId() {

        // Task 2: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
        //         en büyük id ye sahip todonun id sini BÜTÜN PAGE leri dikkate alarak bulunuz.

        int maxId = 0;

        for (int i = 1; i <= totalPage; i++) {

            List<Todos> todosList =
                    given()
                            .param("page", i)
                            .when()
                            .get("/todos")
                            .then()
                            .extract().jsonPath().getList("data", Todos.class);

            for (int j = 1; j < todosList.size(); j++) {
                if (todosList.get(j).getId() > maxId) {
                    maxId = todosList.get(j).getId();
                }
            }
        }
        System.out.println("maxId = " + maxId);
    }

    @Test
    public void getBiggestTodoIdDoWhile() {

        // Task 2: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
        //         en büyük id ye sahip todonun id sini BÜTÜN PAGE leri dikkate alarak bulunuz.

        int maxId = 0, totalPage = 0, page = 1;

        do {
            Response response =
                    given()
                            .param("page", page)
                            .when()
                            .get("/todos")
                            .then()
                            .extract().response();

            if (page == 1) {
                totalPage = response.jsonPath().getInt("meta.pagination.pages");
            }
            List<Todos> todosList = response.jsonPath().getList("data", Todos.class);

            for (int j = 1; j < todosList.size(); j++) {
                if (todosList.get(j).getId() > maxId) {
                    maxId = todosList.get(j).getId();
                }
            }
            page++;
        } while (page <= totalPage);

        System.out.println("maxId = " + maxId);
    }

    @Test
    public void getAllTodoIds() {

        // Task 3 : https://gorest.co.in/public/v1/todos  Api sinden
        // dönen bütün bütün sayfalardaki bütün idleri tek bir Liste atınız.

        List<Integer> allToDoList = new ArrayList<>();

        int totalPage = 0, page=1;

        do {
            Response response =
                    given()
                            .param("page", page)
                            .when()
                            .get("/todos")
                            .then()
                            .extract().response();

            if (page == 1) {
                totalPage = response.jsonPath().getInt("meta.pagination.pages");
            }
            List<Integer> idList = response.jsonPath().getList("data.id");

            allToDoList.addAll(idList);

            page++;
        } while (page <= totalPage);
        System.out.println("allToDoList = " + allToDoList);
    }


    @Test
    public void getAllTodos() {

        given()
                .when()
                .get("/todos")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void getATodos() {

        List<Todos> todosList =
                given()
                        .when()
                        .get("/users/46/todos")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath().getList("data", Todos.class);

        for (Todos t : todosList) {
            System.out.println("todo = " + t);
        }
    }

    @Test
    public void getAllTodosAsObject() {

        TodosMain todosMain =
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .statusCode(200)
                        .extract().as(TodosMain.class);

        System.out.println("todosMain.getData().get(1).getTitle() = " + todosMain.getData().get(1).getTitle());
        System.out.println("todosMain.getData().get(4).getDue_on() = " + todosMain.getData().get(4).getDue_on());
        System.out.println("todosMain.getData().get(5).getStatus() = " + todosMain.getData().get(5).getStatus());
        System.out.println("todosMain.getData().get(3).getUser_id() = " + todosMain.getData().get(3).getUser_id());
        System.out.println("todosMain.getData().get(8).getId() = " + todosMain.getData().get(8).getId());
        System.out.println("getLinks().getCurrent() = " + todosMain.getMeta().getPagination().getLinks().getCurrent());
    }

    int todoId = 0;

    @Test(priority = 1)
    public void createATodo() {
// Task 4 : https://gorest.co.in/public/v1/todos  Api sine
        // 1 todo Create ediniz.

        String title = "Chaque matin on a une réunion a 9h15.";
        String dueOn = "2021-09-11T00:00:00.000+05:30";
        String status = "pending";
        Todos todos = new Todos();
        todos.setTitle(title);
        todos.setStatus(status);
        todos.setDue_on(dueOn);
        todos.setUser_id(46);

        todoId =
                given()
                        .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                        .contentType(ContentType.JSON)
                        .body(todos)
                        .when()
                        .post("/todos")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .body("data.title", equalTo(title))
                        .body("data.status", equalTo(status))
                        .body("data.due_on", equalTo(dueOn))
                        .extract().jsonPath().getInt("data.id");

        System.out.println("todoId = " + todoId);
    }

    @Test(dependsOnMethods = {"createATodo"}, priority = 2)
    public void getTodo() {
        // Task 5 : Create edilen ToDoyu get yaparak id sini kontrol ediniz.
        given()
                .pathParam("todoId", todoId)
                .when()
                .get("/todos/{todoId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(todoId));
    }

    @Test(dependsOnMethods = {"createATodo"}, priority = 3)
    public void updateATodo() {

        String title = "How can we handle the difficulties?";
        String status= "completed";

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body("{\n" +"\"title\": \""+title+"\",\n" +"\"status\": \""+status+"\"\n" +"}")
                .pathParam("todoId", todoId)
                .when()
                .log().uri()
                .put("/todos/{todoId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.title", equalTo(title))
                .body("data.status",equalTo(status));
    }

    @Test(dependsOnMethods = {"createATodo"}, priority = 4)
    public void deleteATodo() {
// Task 7 : Create edilen ToDoyu siliniz. Status kodu kontorl ediniz 204

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("todoId", todoId)
                .when()
                .delete("/todos/{todoId}")
                .then()
                .statusCode(204);
    }

    @Test(priority = 5)
    public void deleteATodoNegatif() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("todoId", todoId)
                .when()
                .delete("https://gorest.co.in/public-api/todos/{todoId}")
                .then()
                .body("code", equalTo(404))
                .statusCode(200);
    }
}
