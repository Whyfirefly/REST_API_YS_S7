import constants.Urls;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import java.io.File;

import static io.restassured.RestAssured.given;
import static constants.Urls.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class OrderGetListTest {
  OrderSteps orderSteps;

  @Before
  public void setUp() {
    RestAssured.baseURI = BASE_URL;
    orderSteps = new OrderSteps();
  }

  @Step("Создание заказов для того, чтобы проверить список заказов")
  @Test
  public void createNewOrderAndCheckResponseJsonInFile1(){
    File json = new File("src/test/resources/newOrders.json");
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .post(ORDER_POST_CREATE);
              response.then().assertThat().statusCode(201)
                      .and()
                      .assertThat().body("track", Matchers.notNullValue());
        System.out.println(response.body().asString());
  }

  @Test
  public void createNewOrderAndCheckResponseJsonInFile2(){
    File json = new File("src/test/resources/newOrders.json");
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .post(ORDER_POST_CREATE);
    response.then().assertThat().statusCode(201)
            .and()
            .assertThat().body("track", Matchers.notNullValue());
    System.out.println(response.body().asString());
  }

  //не могу понять, почему этот шаг работает через раз то ок, то 504
  @Step("Проверка наличия списка заказов")
  @Test
  @DisplayName("Получение списка заказов")
  @Description("Получение списка заказов, проверка наличия списка, что он не пустой")
  public void orderGetList() {
           given().log().all()
            .baseUri(Urls.BASE_URL)
            .get(ORDER_GET_LIST)
            .then()
            .statusCode(200)
            .and()
            .assertThat().body("orders", is(not(empty())));
  }
}