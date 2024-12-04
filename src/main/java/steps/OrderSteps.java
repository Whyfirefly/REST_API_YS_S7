package steps;

import constants.Urls;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import pojo.OrderCreate;

import java.io.File;

import static constants.Urls.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {

  //Base uri присваиваем через request specification в RestAssured
  public static RequestSpecification requestSpecification() {
    return given().log().all()
            .contentType(ContentType.JSON)
            .baseUri(Urls.BASE_URL);
  }

  @Step("Создание нового заказа с использованием {order} _ прописываем строку")
  public Response createOrderWithLine(OrderCreate order) {
    return requestSpecification()
            .body(order)
            .when()
            .post(ORDER_POST_CREATE);
  }

  @Step("Создание заказа из JSON файла")
  public Response createNewOrderFromJsonInFile(){
    File json = new File("src/test/resources/newOrders.json");
          return requestSpecification()
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
            .assertThat().body("track", notNullValue());
  }

}
