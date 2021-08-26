import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class deneme {

    @Test
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
                    .param("page",i)
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(200)
                    .log().body()
                    .body("meta.pagination.page",equalTo(i))
            ;
        }
    }

}
