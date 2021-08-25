
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {

        given()
                //hazırlık işlemlerini yapacağız


                .when()
                //link ve aksiyon işlemleri


                //test ve extract işlemleri
                .then()

        ;
    }

    @Test
    public void statusCodeTest() {

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
    public void contentTypeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
        ;
    }


    @Test
    public void logTest() {

        given()
                .log().all()  //request kısmı
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
        ;
    }

    @Test
    public void checkStateInResponseBody() {

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
    public void checkStateInResponseBodyArray() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))
                .statusCode(200)
        ;

    }

    @Test
    public void checkJsonHasItem() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places.state", hasItem("California")) // tüm state'lerin içinde California var mı diye arıyor
                .statusCode(200)
        ;
    }

    @Test
    public void testTwoWord() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                //arasında boşluk olan key'lerde  keyin başına ve sonuna tek tırnak konur('place name')
                .statusCode(200)
        ;
    }

    @Test
    public void testHasSize() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places", hasSize(1))// verilen pathdeki listin size kontrolü
                .statusCode(200)
        ;
    }

    @Test
    public void combiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                //1'den fazla assertion yapılabilir
                .statusCode(200)
        ;
    }

    @Test
    public void pathParametreTest() {

        given()
                .pathParam("country", "us")
                .pathParam("zipKod", "90210")
                .log().uri() //request tarafı, burada url'yi alabiliyoruz
                .when()
                .get("http://api.zippopotam.us/{country}/{zipKod}")
                .then()
                .log().body()
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void pathParametreTest2() {

        String country = "us";

        for (int i = 90210; i < 90214; i++) {
//            String zipKod= i.toString();
            given()
                    .pathParam("country", country)
                    .pathParam("zipKod", i)
                    .log().uri() //request tarafı, burada url'yi alabiliyoruz
                    .when()
                    .get("http://api.zippopotam.us/{country}/{zipKod}")
                    .then()
                    .log().body()
                    .body("places", hasSize(1))
            ;
        }
    }

    @Test
    public void queryParameterTest() {

        given()
                .param("page", 1) // ?page=1 kısmını eklemiş oluyoruz bu değişkenle.aşağıdaki gibi oluyor request linki
                .log().uri()                // Request URI:	https://gorest.co.in/public/v1/users?page=1
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1))
        ;
    }


    @Test
    public void queryParameterTest2() {

        for (int i = 1; i < 10; i++) {


            given()
                    .param("page", i) // ?page=1 kısmını eklemiş oluyoruz bu değişkenle.aşağıdaki gibi oluyor request linki
                    .log().uri()                // Request URI:	https://gorest.co.in/public/v1/users?page=1
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

}
