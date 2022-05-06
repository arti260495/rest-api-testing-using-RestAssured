import PojoClasses.House;
import PojoClasses.User;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class ApiTests {

    RequestSpecification requestSpec = given()
            .baseUri("http://77.50.236.203:4880")
            .contentType(ContentType.JSON);

    @Test
    public void checkUsersInfo(){
        // получение списка пользователей через jsonPath
        List <User> users = requestSpec
                .when()
                .get("/users")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("", User.class);

        // проверка конкретных значений
        Assert.assertEquals(users.get(1).getFirstName(), "Petr");
        Assert.assertEquals(users.get(1).getSecondName(), "Petrov");
        Assert.assertEquals(users.get(1).getSex(), "MALE");
        Assert.assertEquals((users.get(1).getMoney()), (Double)99999.0);
        Assert.assertEquals(users.get(1).getId().toString(), "2");
        Assert.assertEquals(users.get(1).getAge().toString(), "30");
    }

    @Test
    public void checkUserInfoById(){
        //получение пользователя по id, проверка значений
        Response response = requestSpec
                .when()
                .pathParam("id", 1)
                .get("/user/{id}")
                .then().log().all()
                .body("firstName", equalTo("Vasiliy"))
                .body("secondName", notNullValue())
                .body("age", greaterThan(30))
                .body("sex", not("FEMALE"))
                .extract().response();
    }

    @Test
    public void checkCarsInfo(){
        // получение списка автомобилей через TypeRef
        List<Map<String, Object>> cars = requestSpec
                .when()
                .get("/cars")
                .then().log().all()
                .extract().as(new TypeRef<List<Map<String, Object>>>() {});

        // проверка конкретных значений
        Assert.assertEquals(cars.get(0).get("EngineType"), "Electric");
        Assert.assertEquals(cars.get(0).get("Mark"), "Tesla");
        Assert.assertEquals(cars.get(0).get("Model"), "Model X");
        Assert.assertEquals(cars.get(0).get("Price"), 70000.00);
        Assert.assertEquals(cars.get(0).get("Id"), 1);
    }

    @Test
    public void AddCar() {
        //создание автомобиля (через JSONObject)
        JSONObject requestBody = new JSONObject();
        requestBody.put("engineType", "Diesel");
        requestBody.put("mark", "Ford");
        requestBody.put("model", "Mustang");
        requestBody.put("price", 99999.00);

        requestSpec
                .body(requestBody.toString())
                .when().post("/addCar").then()
                .log().all()
                .statusCode(201);
    }

    @Test
    public void AddUser()  {
        //создание пользователя (через POJO)

        User user = new User();
        user.setFirstName("Jhonny");
        user.setSecondName("Depp");
        user.setAge(45);
        user.setSex("MALE");
        user.setMoney(999999.0);

        requestSpec
                .body(user)
                .when().post("/addUser").then()
                .log().all()
                .statusCode(201);
    }

    @Test
    public void AddHouse(){
        //создание парковок
        House.ParkingPlace parkingPlace1 = new House.ParkingPlace();
        House.ParkingPlace parkingPlace2 = new House.ParkingPlace();

        parkingPlace1.setIsWarm(true);
        parkingPlace1.setIsCovered(true);
        parkingPlace1.setPlacesCount(3);

        parkingPlace2.setIsWarm(false);
        parkingPlace2.setIsCovered(false);
        parkingPlace2.setPlacesCount(2);

        List<House.ParkingPlace> parkingPlaces = new ArrayList<>();
        parkingPlaces.add(parkingPlace1);
        parkingPlaces.add(parkingPlace2);

        //создание дома
        House house = new House();
        house.setFloorCount(2);
        house.setPrice(93939);
        house.setParkingPlaces(parkingPlaces);

        requestSpec
                .body(house)
                .when().post("/addHouse").then()
                .log().all()
                .statusCode(201);
    }

    //покупка машины
    @Test
    public void BuyCar(){

        requestSpec
                .pathParam("userId", 1)
                .pathParam("carId", 2)
                .when().post("/user/{userId}/buyCar/{carId}").then()
                .log().all()
                .statusCode(200);

    }

    //покупка дома
    @Test
    public void Settle(){
        requestSpec
                .pathParam("houseId", 3)
                .pathParam("userId", 1)
                .when().post("/house/{houseId}/settle/{userId}").then()
                .log().all()
                .statusCode(200);
    }

    //заработать денег
    @Test
    public void Money(){
        requestSpec
                .pathParam("userId", 1)
                .pathParam("amount", 898989.00)
                .when().post("/user/{userId}/money/{amount}").then()
                .log().all()
                .statusCode(200);
    }



}
