
import io.restassured.http.ContentType;
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
                .statusCode(200) //status kontrolü - hata olmadığında bir şey yazmıyor!

               ;
    }

    @Test
    public void contentTypeTest(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
                ;
    }


    @Test
    public void logTest(){

        given()
                .log().all()  //request kısmı
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                ;
    }

    @Test
    public void checkStateInResponseBody(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States")) //Dışarıda ayrı bir assert yazmadan sonucu kontrol etmeye
                                                                    //yarayan metod hamcrest yöntemi.
                .statusCode(200)
                ;

    }

    @Test
    public void checkStateInResponseBodyArray(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))
                .statusCode(200)
        ;

    }



}
