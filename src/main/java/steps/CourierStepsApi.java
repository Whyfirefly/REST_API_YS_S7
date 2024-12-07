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
  public Response createCourier(String login, String pass, String name) {
    CreateCourier courier = new CreateCourier(login, pass, name);
    return given()
            .spec(requestSpecification())
            .and()
            .body(courier)
            .when()
            .post(COURIER_POST_CREATE);
  }

  @Step("Логин курьера в системе")
  public Response loginCourier(String login, String pass) {
    LoginCourier loginCourier = new LoginCourier(login, pass);
    return given()
            .spec(requestSpecification())
            .and()
            .body(loginCourier)
            .when()
            .post(COURIER_POST_LOGIN);
  }

  @Step("Получение id курьера")
  public Integer getCourierId(String login, String pass) {
    return loginCourier(login, pass)
            .body()
            .as(CreateCourier.class).getId();
  }

  @Step("Удаление курьера")
  public Response deleteCourier(String login, String pass) {

    return given()
            .spec(requestSpecification())
            .when()
            .delete(COURIER_DELETE + (getCourierId(login, pass)));
  }

  @Step("Получение рандомного несуществующего id курьера")
  public Response setRandomCourierId(String id) {
    return given()
            .spec(requestSpecification())
            .when()
            .post(COURIER_DELETE + RANDOM_COURIER_ID);
  }

}
