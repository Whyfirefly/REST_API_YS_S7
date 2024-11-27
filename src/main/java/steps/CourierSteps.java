package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CreateCourier;
import pojo.LoginCourier;

import static constants.Urls.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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

  @Step("Создание логина курьера")
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
            .delete(courierDeletePreparingToString(getCourierId(login, pass)));
  }

  @Step("Подготовка к удалению курьера")
  public String courierDeletePreparingToString(Integer courierID) {
    return COURIER_DELETE + courierID;
  }

  @Step("Проверка body - (ok: true) и ответа сервера на первую корректную регистрацию - 201")
  public void checkAnswerValidRegistration(Response response) {
    response
            .then()
            .statusCode(201)
            .and().assertThat().body("ok", equalTo(true));
  }

  @Step("Проверка body - (ok: true) и ответа сервера на удаление курьера - 200")
  public void checkAnswerThenValidDeleting(Response response) {
    response
            .then()
            .statusCode(200)
            .and().assertThat().body("ok", equalTo(true));
  }

  @Step("Проверка body при попытке повторной регистрации под уже существующим логином - 409")
  public void checkAnswerReuseRegistrationData(Response response) {
    response.then()
            .statusCode(409)
            .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
  }

  @Step("Проверка body и ответа сервера при неполных данных при регистрации - 400")
  public void checkAnswerWithNotEnoughRegData(Response response) {
    response.then()
            .statusCode(400)
            .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
  }

  @Step("Проверка присутствия id курьера по его логину и паролю")
  public void checkAnswerAndPresenceId(Response response) {
    response.then()
            .statusCode(200).and().assertThat().body("id", notNullValue());
  }

  @Step("Проверка ошибки системы при попытка входа под несуществующими логином и паролем")
  public void checkAnswerWithWrongData(Response response) {
    response.then()
            .statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));
  }

  @Step("Проверка ошибки системы при попытка входа без указания логина или пароля")
  public void checkAnswerWithoutData(Response response) {
    response.then()
            .statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
  }
}
