package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.OrderCreate;
import pojo.OrderCreatedMain;

import java.io.File;

import static constants.Urls.*;
import static io.restassured.RestAssured.given;


public class OrderStepsApi extends RestApi {

  @Step("Создание нового заказа с использованием {order} _ прописываем строку")
  public Response createOrderWithLine(OrderCreate order) {
    return given()
            .spec(requestSpecification())
            .and()
            .body(order)
            .when()
            .post(ORDER_POST_CREATE);
  }

  @Step("Создание заказа из JSON файла")
  public Response createNewOrderFromJsonInFile() {
    File json = new File("src/test/resources/newOrders.json");
    return given()
            .spec(requestSpecification())
            .and()
            .body(json)
            .when()
            .post(ORDER_POST_CREATE);
  }

  @Step("Получаем id заказа по треккинговому номеру")
  public Integer getOrderId(int trackNumber) {
    return given()
            .spec(requestSpecification())
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            //десериализуем JSON
            .body().as(OrderCreatedMain.class).getOrder().getId();
  }

  @Step("Получаем track заказа по треккинговому номеру для его отмены")
  public String getOrderTrack(int trackNumber) {
    return given()
            .spec(requestSpecification())
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            //десериализуем JSON
            .body().as(OrderCreatedMain.class).getOrder().getTrack();
  }

}
