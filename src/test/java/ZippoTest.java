
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test(){

        given()
                //hazırlık işlemlerini yapacağız


                .when()
                //link ve aksiyon işlemleri


                //test ve extract işlemleri
                .then()


        ;
    }

    @Test
    public void statusCodeTest(){

        // given().when().then(); normalde böyle yazılıyor ama okunaklılık için aşağıdaki gibi yazdık


        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() //sadece body'i yazıyor
                .log().all() //tüm response'u yazıyor
        ;
    }




}
