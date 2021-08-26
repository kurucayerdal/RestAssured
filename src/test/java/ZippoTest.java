
import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

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

        for (int i = 1; i <= 10; i++) { //10'uncu sayfaya kadar body içindekileri yazacak


            given()
                    .param("page", i)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @BeforeClass
    public void setup() {
        baseURI = "http://api.zippopotam.us";    //restAssured kendi statik değişkeni tanımlı değer atanıyor.

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()  //paket halinde her testte kullanılacak bir küçük test paketi oluşturduk
                .expectStatusCode(200)                      //Burada olduğu için her testte çalışacak
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void baseUriTest() {

        given()
                .log().uri()
                .when()
                .get("/us/90210") //url'nin başında http yoksa baseUri  değişkeni otonatik olarak geliyor
                .then()
                .log().body()
                .body("places", hasSize(1))
                .statusCode(200)
        ;
    }

    @Test
    public void responseSpecificationTest() {

        given()
                .log().uri()
                .when()
                .get("/us/90210") //url'nin başında http yoksa baseUri  değişkeni otonatik olarak geliyor
                .then()
                .spec(responseSpecification) //yukarıda tanımladığımız bu değişkenin içine log.body,statusCode
        // ve ContentType.JSON kontrollerini bu değişkenle beforeClass'ta yaptık
        ;
    }


    @Test
    public void requestSpecificationTest() {

        given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210") //url'nin başında http yoksa baseUri  değişkeni otonatik olarak geliyor
                .then()
                .spec(responseSpecification) //yukarıda tanımladığımız bu değişkenin içine log.body,statusCode
        // ve ContentType.JSON kontrollerini bu değişkenle beforeClass'ta yaptık
        ;
    }

    //Json extract işlemlerini yapacağız
    @Test
    public void extractingJsonPath() {

        //aşağıdaki sonucu bir String'e atadık ve extract ile dışarı alıp bir değişkene atamış olduk
        String place_name = given()
                //.spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                //.spec(responseSpecification)
                .extract().path("places[0].'place name'")//en sonda olacak. Elemanı dışarı almayı sağlayacak
                                                            //Çıktısı= place_name = Beverly Hills
                ;
        System.out.println("place_name = " + place_name);
    }

    @Test
    public void extractingIntegerTest() {

        //aşağıdaki sonucu bir int'e atadık ve extract ile dışarı alıp bir değişkene atamış olduk
        int limit= given()
                .param("page", 1) // ?page=1 kısmını eklemiş oluyoruz bu değişkenle.aşağıdaki gibi oluyor request linki
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .extract().path("meta.pagination.limit")
        ;
        System.out.println("limit = " + limit); //Çıktısı= limit = 20
    }

    @Test
    public void extractingToStringTest() {

        //aşağıdaki sonucu bir String'e atadık ve extract ile dışarı alıp bir değişkene atamış olduk
        String limit= given()
                .param("page", 1)
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .extract().path("meta.pagination.limit").toString()//gelecek int değeri toString ile Stringe çevirdik
                ;
        System.out.println("limit = " + limit); //Çıktısı= limit = 20
    }


    @Test
    public void extractingIntegerListTest() {

        //data[0].id -> 1.elemanın yani indexi 0 olanın id si
        List<Integer> AllId= given()
                .param("page", 1) // ?page=1 kısmını eklemiş oluyoruz bu değişkenle.aşağıdaki gibi oluyor request linki
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                //.log().body()
                .extract().path("data.id")
                ;
        System.out.println("AllId = " + AllId);
    }

    @Test
    public void extractingStringList() {


        List<String> koyler = given()
                .when()
                .get("/tr/01000")
                .then()
                .log().body()
                .extract().path("places.'place name'")//köylerin listesini aldık
                ;
        System.out.println("koyler = " + koyler);
        Assert.assertTrue(koyler.contains("Büyükdikili Köyü"));
    }

    @Test
    public void extractingJsonPOJO()
    {
        //Her şeyi al Location claas'ı gibi çevir ve location isimli object'e ata

        Location location= given()
                .when()
                .get("/us/90210")
                .then()
                .extract().as(Location.class);
        System.out.println("location = " + location);
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces() = " + location.getPlaces());
        System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());
    }

}
