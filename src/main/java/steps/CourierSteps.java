package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CreateCourier;
import pojo.LoginCourier;

import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

  @Step("Создание курьера")
  public Response createCourier(String login, String pass, String name) {
    CreateCourier courier = new CreateCourier(login, pass, name);
    return given()
            .header("Content-type", "application/json")
            .body(courier)
            .when()
            .post(COURIER_POST_CREATE);
  }

  @Step("Логин курьера в системе")
  public Response loginCourier(String login, String pass) {
    LoginCourier loginCourier = new LoginCourier(login, pass);
    return given()
            .header("Content-type", "application/json")
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
            .header("Content-type", "application/json")
            .when()
            .delete(COURIER_DELETE+(getCourierId(login, pass)));
  }
}
