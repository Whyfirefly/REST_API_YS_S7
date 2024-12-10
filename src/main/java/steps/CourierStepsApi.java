package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CreateCourier;
import pojo.LoginCourier;

import static random_data.RandomData.RANDOM_COURIER_ID;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class CourierStepsApi extends RestApi {

  @Step("Создание курьера")
  public Response createCourier(CreateCourier createCourier) {
    return given()
            .spec(requestSpecification())
            .and()
            .body(createCourier)
            .when()
            .post(COURIER_POST_CREATE);
  }

  @Step("Логин курьера в системе")
  public Response loginCourier(LoginCourier loginCourier) {
    return given()
            .spec(requestSpecification())
            .and()
            .body(loginCourier)
            .when()
            .post(COURIER_POST_LOGIN);
  }

  @Step("Получение id курьера")
  public Integer getCourierId(LoginCourier loginCourier) {
    return loginCourier(loginCourier)
            .body()
            .as(CreateCourier.class).getId();
  }

  @Step("Удаление курьера")
  public Response deleteCourier(LoginCourier loginCourier) {

    return given()
            .spec(requestSpecification())
            .when()
            .delete(COURIER_DELETE + (getCourierId(loginCourier)));
  }

  @Step("Получение рандомного несуществующего id курьера")
  public Response setRandomCourierId(String id) {
    return given()
            .spec(requestSpecification())
            .when()
            .post(COURIER_DELETE + RANDOM_COURIER_ID);
  }

}
