import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class deneme {

    @Test
    public void test1(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places[0].longitude",equalTo("-118.4065"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))
                ;
    }

    @Test
    public void test2(){

        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(200)
                .log().all()
                .body("data[2].name",equalTo("Colin Schmidt"))
                ;
    }

}
