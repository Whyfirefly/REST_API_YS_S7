package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import pojo.OrderCreate;
import pojo.OrderCreatedMain;

import java.io.File;

import static constants.AnswersWhenChecksFailed.*;
import static random_data.RandomData.*;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;

public class OrderSteps extends RestApi {

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

  @Step("Проверка тела ответа при создании заказа с \"track\"")
  public void checkOrderTrackNotNullNew(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_CREATED)
            .and()
            .assertThat().body("track", notNullValue())
            .log().all();
  }

  @Step("Проверка наличия списка заказов, что он не пустой, статус 200 ОК")
  public void checkGetListOrder() {
    given()
            .spec(requestSpecification())
            .get(ORDER_GET_LIST)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat().body("orders", is(not(empty())))
            .log().all();
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

  @Step("Проверка наличия заказа при его поиске по треккинговому номеру - статус 200 ОК")
  public void checkGetListOrderByValidTrackNumber(int trackNumber) {
    given()
            .spec(requestSpecification())
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat().body("order", is(not(empty())))
            .log().all();
  }

  @Step("Проверка отсутствия заказа при его поиске по несуществующему треккинговому номеру - статус 404")
  public void checkGetListOrderByInvalidTrackNumber() {
    given()
            .spec(requestSpecification())
            .queryParam("t", "999" + RANDOM_ORDER_TRACK)
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and()
            .assertThat().body("message", equalTo(ANSWER_WHEN_GET_LIST_ORDER_BY_INVALID_TRACK_NUMBER))
            .log().all();
  }

  @Step("Проверка получения заказа без треккингового номера - статус 400")
  public void checkGetListOrderByEmptyTrackNumber() {
    given()
            .spec(requestSpecification())
            .queryParam("t", "")
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .and()
            .assertThat().body("message", equalTo(ANSWER_WHEN_GET_LIST_ORDER_WITHOUT_TRACK_NUMBER_OR_COURIER_ID))
            .log().all();
  }

  @Step("Проверка принятия заказа по существующим id заказа и курьера - статус 200 ОК")
  public void checkAcceptanceOrderByValidCourierIdAndOrderId(int courierID, int orderId) {
    given()
            .spec(requestSpecification())
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true))
            .log().all();
  }

  @Step("Проверка повторного принятия одного заказа - статус 409")
  public void checkRepeatAcceptanceOrder(int courierID, int orderId) {
    given()
            .spec(requestSpecification())
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .and().assertThat().body("message", equalTo(ANSWER_WHEN_REPEAT_ACCEPTANCE_ORDER))
            .log().all();
  }

  @Step("Проверка принятия заказа без id заказа - статус 404")
  public void checkAcceptanceOrderWithoutOrderId(int courierID) {
    given()
            .spec(requestSpecification())
            .queryParam("courierId", RANDOM_COURIER_ID)
            .put(ORDER_PUT_ACCEPT_ORDER)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and().assertThat().body("message", equalTo(ANSWER_WHEN_COURIER_OR_ORDER_ID_IS_INVALID))
            .log().all();
  }

  @Step("Проверка принятия заказа без id курьера - статус 400")
  public void checkAcceptanceOrderWithoutCourierId(int orderId) {
    given()
            .spec(requestSpecification())
            .queryParam("courierId", "")
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .and().assertThat().body("message", equalTo(ANSWER_WHEN_GET_LIST_ORDER_WITHOUT_TRACK_NUMBER_OR_COURIER_ID))
            .log().all();
  }

  @Step("Проверка принятия заказа с несуществующим id заказа - статус 404")
  public void checkAcceptanceOrderWithInvalidOrderId(int courierID) {
    given()
            .spec(requestSpecification())
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + RANDOM_ORDER_ID)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and().assertThat().body("message", equalTo(ANSWER_WHEN_ACCEPTANCE_ORDER_WITH_INVALID_ORDER_ID))
            .log().all();
  }

  @Step("Проверка принятия заказа с несуществующим id курьера - статус 404")
  public void checkAcceptanceOrderWithInvalidCourierId(int orderId) {
    given()
            .spec(requestSpecification())
            .queryParam("courierId", "999" + RANDOM_COURIER_ID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and().assertThat().body("message", equalTo(ANSWER_WHEN_ACCEPTANCE_ORDER_WITH_INVALID_COURIER_ID))
            .log().all();
  }
}
