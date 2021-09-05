package goRest;

import goRest.model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {


    @Test(enabled = false) //bu testi devre dışı bıraktık
    public void getUsers() {

        List<User> userList = given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                //.log().body()
                .extract().jsonPath().getList("data", User.class)
                //data'yı User class'ı olarak list'e dönüştür- aradaki bir elemanı alıp dönüştürmek için jsonPath kullanıyoruz
                //istediğimiz formata çevirebiliyoruz String, int float vs.
                //kök veriyi almak istesek as() metodu ile alabilirdik ama aradaki herhangi bir elemanı almak için
                // .extract().path kullansaydık 3-4 tane class yazmamız gerekecekti ama jsonPath bizi bu zahmetten kurtarıyor.
                ;

        for (User u : userList) {
            System.out.println("user = " + u);
        }

    }


    // Create User için POSTMAN de yapılanlar

    //    JSON olarak gidecek body  {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
//
//    header ın içine
//    Authorization  Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f
//
//    POST ile https://gorest.co.in/public/v1/users çağırdık
//    id yi okuduk ve global bir değişkene attık ki, diğer reqest larde kullanabilelim

    int userId = 0; // class içinde geçerli bir int tanımladık ve test içindeki json nesnesini ona atadık

    @Test
    public void createUser() {

        userId = given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"erdal keser\", \"gender\":\"male\", \"email\":\"" + randomEmail() + "\", \"status\":\"active\"}")
                .when()
                .post("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("userId = " + userId);

    }

    public String randomEmail() {

        String randomString = RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString + "@gmail.com";
    }

    @Test(enabled = false)
    public void getUserById() {

        createUser();
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users" + "/" + userId)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }


    @Test(dependsOnMethods = "createUser",priority = 1)
    public void getUserById_2() {

        given()
                .pathParam("userId", userId)
                .when()
                .get("https://gorest.co.in/public/v1/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(userId))
        ;
    }

    @Test(dependsOnMethods = "createUser",priority = 2)
    public void updateUserById() {

        String isim = "ediz bahtiyar";

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON) //body'e bir değer gönderdiğimiz zaman mutlaka formatını seçmemiz lazım
                .body("{\"name\":\"" + isim + "\"}")
                .pathParam("userId", userId)
                .when()
                .put("https://gorest.co.in/public/v1/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.name", equalTo(isim))
        ;
    }

    @Test(dependsOnMethods = "createUser",priority = 3)
    public void deleteUserById() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .pathParam("userId", userId)
                .when()
                .delete("https://gorest.co.in/public/v1/users/{userId}")
                .then()
                .statusCode(204)
        ;
    }

    @Test(priority = 4)
    public void deleteUserByIdNegatifTest() {

        given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .delete("https://gorest.co.in/public-api/users/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .body("code",equalTo(404))
        ;
    }

    /*
    istediğim veriyi istediğim tipte nasıl alırım? ama bu extract işleminde amaç bu.
{
    "meta": {  meta
        "pagination": {   pagination
            "total": 1633,  -> .extract.path("meta.pagination.total")   -> int total , ototatik int dönüşümü kendisi de int olduğu için.
                            -> .extract.jsonPath.getInt("meta.pagination.total")  -> int e dönüştürüp öyle int e eşitliyor.
            "pages": 82,
            "page": 1,
            "limit": 20,
            "links": {     link
                "previous": null,
                "current": "https://gorest.co.in/public/v1/users?page=1",
                "next": "https://gorest.co.in/public/v1/users?page=2"
            }
        }
    },
    "data": [ User
        {
            "id": 1685,
            "name": "Qmzn",        -> bütün nameleri almak için    extract.path("data.name") -> List<String> değişkene atarken çevirecek
                                   -> extract.jsonPath().getList("data.name") -> list e çevirip öyle vermek olacaktı
            "email": "qvjsulha@gmail.com",
            "gender": "male",
            "status": "active"
        },
        {
            "id": 1687,
            "name": "Mavie Test 3",
            "email": "test3@email.com",
            "gender": "female",
            "status": "active"
        }
   ]
}


-> Bütün veriyi toplu olarak almak için -> 4 tane class yazman lazım ve -> extract.as(Genel.class)

public class Genel
{
     Meta meta;
     List<User> data;
}

genel.getMeta().getPagination().getTotal    -> bütün verilere bu şekilde ulaşabiliyorum.
-------------------------------------------------------------------------------------------------

jsonPath esas nerede devreye giriyor, yukarıdakilerde neyi yapamıyoruz ki jsonPath e ihtiyacımız oluyor ?


1- Sadece Linkleri bir Class tipinde elde etmek istiyorum ?

         extract.jsonPath.getObject("meta.pagination.links", Link.class)

         bu sorunu jsonPath çözebiliyor.Diğer classları yazmama gerek olmadan


2- tip dönüşümünü veriyi alırken yapması da bir avantaj.

  tek bir değişken almak için
        path veya jsonPath kullanılabilir

        tüm veriye ihtiyacın varsa .as(Genel.class)
        kullanılacak

        verinin içinden bir bölümü bir clasa atmak
        istersen jsonPath kullanılacak
     */
    @Test
    public void responseSample(){

        Response donenSonuc=  //dönen sonuçların hepsi bir değişkene atıldı
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .extract().response();

        //Şimdi lazım olan istediklerimizi tekrar request yapmadan tek tek alabiliriz.
        List<User> userList=donenSonuc.jsonPath().getList("data",User.class);
        int total=donenSonuc.jsonPath().getInt("meta.pagination.total");
        int limit=donenSonuc.jsonPath().getInt("meta.pagination.limit");
        User firstUser= donenSonuc.jsonPath().getObject("data[0]",User.class);

        System.out.println("userList = " + userList);
        System.out.println("total = " + total);
        System.out.println("limit = " + limit);
        System.out.println("firstUser = " + firstUser);
    }

    @Test
    public void createUserBodyMap() {

        Map<String,String> newUser=new HashMap<>();//verileri istersek Map olarak gönderebiliriz.ContentType.JSON onu json a çeviriyor
        newUser.put("name","orhan");
        newUser.put("gender","male");
        newUser.put("email",randomEmail());
        newUser.put("status","active");

        userId = given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body(newUser) //body'nin içine direk map'i yazıyoruz.
                .when()
                .post("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("userId = " + userId);
    }

    @Test
    public void createUserBodyObject() {

        User newUser=new User(); //bir nesne  oluşturup onu da gönderebiliriz, yine json a çevriliyor.
        newUser.setName("suphi");
        newUser.setGender("male");
        newUser.setEmail(randomEmail());
        newUser.setStatus("active");

        userId = given()
                .header("Authorization", "Bearer 636144d160083b1ed3acb97f4192dc601314b4d4ebd93a270c328bd3b61cebdf")
                .contentType(ContentType.JSON)
                .body(newUser) //body'nin içine direk User nesnesini yazıyoruz.
                .log().body()
                .when()
                .post("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("userId = " + userId);
    }


    @Test()
    public void takeAUserName() {

        String isim =   given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .extract().path("data.find{it.id==36}.name");  //id'si 36 olan elemanın name'ini alıyoruz.

        System.out.println("isim = " + isim);
    }

    @Test()
    public void takeAGroupOfUser() {

        List<String> list =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .extract().path("data.findAll{it.id<60}.name")//id'si 60'tan küçük olanları List olarak aldık
                ;
        System.out.println("isim = " + list);
    }


}

