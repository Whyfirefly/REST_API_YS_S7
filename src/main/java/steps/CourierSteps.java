package steps;

import constants.RandomData;
import constants.Urls;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.CreateCourier;
import pojo.LoginCourier;

import static constants.RandomData.RANDOM_ID;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

  //Base uri присваиваем через request specification в RestAssured
  public static RequestSpecification requestSpecification() {
    return given().log().all()
            .contentType(ContentType.JSON)
            .baseUri(Urls.BASE_URL);
  }

  @Step("Создание курьера")

  public Response createCourier(String login, String pass, String name) {
    CreateCourier courier = new CreateCourier(login, pass, name);
    return requestSpecification()
            .body(courier)
            .when()
            .post(COURIER_POST_CREATE);
  }

  @Step("Логин курьера в системе")
  public Response loginCourier(String login, String pass) {
    LoginCourier loginCourier = new LoginCourier(login, pass);
    return requestSpecification()
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

    return requestSpecification()
            .when()
            .delete(COURIER_DELETE+(getCourierId(login, pass)));
  }

  @Step("Получение рандомного id курьера")
  public Response setRandomCourierId(String id) {
    return requestSpecification()
            .when()
            .post(COURIER_DELETE+RANDOM_ID);
  }
}
