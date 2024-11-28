package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.OrderCreate;

import static constants.Urls.*;
import static io.restassured.RestAssured.given;


public class OrderSteps {
  @Step("Создание нового заказа с использованием {order}")
  public Response createOrder(OrderCreate order) {
    return given()
            .header("Content-type", "application/json")
            .body(order)
            .when()
            .post(ORDER_POST_CREATE);
  }

  @Step("Получение списка заказов")
  public Response getOrdersList() {
    return given()
            .header("Content-type", "application/json")
            .when()
            .get(ORDER_GET_LIST);
  }

}
