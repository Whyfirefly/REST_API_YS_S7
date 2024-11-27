package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.OrderCreate;

import static constants.Urls.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {
  @Step("Создание нового заказа с использованием {order}")
  public Response createOrder(OrderCreate order) {
    return given()
            .header("Content-type", "application/json")
            .body(order)
            .when()
            .post(ORDER_POST_CREATE);
  }

  @Step("Проверка body при возвращении ненулевого ответа с \"track\"")
  public void checkOrderTrackNotNullNew(Response response) {
    response.then()
            .statusCode(201)
            .and()
            .assertThat().body("track", notNullValue());
  }

  @Step("Получение списка заказов")
  public Response getOrdersList() {
    return given()
            .header("Content-type", "application/json")
            .when()
            .get(ORDER_GET_LIST);
  }

  @Step("Проверка, что список заказов не пустой")
  public void checkOrderListNotNullNew(Response response) {
    response.then()
            .statusCode(200)
            .and()
            .assertThat().body("orders", notNullValue());
  }

}
