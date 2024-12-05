package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import pojo.CreateCourier;
import pojo.LoginCourier;

import static constants.RandomData.RANDOM_COURIER_ID;
import static constants.Urls.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps extends RestApi {


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
            .delete(COURIER_DELETE+(getCourierId(login, pass)));
  }

  @Step("Получение рандомного несуществующего id курьера")
  public Response setRandomCourierId(String id) {
    return given()
            .spec(requestSpecification())
            .when()
            .post(COURIER_DELETE + RANDOM_COURIER_ID);
  }

  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на удаление курьера - 200")
  public void checkAnswerThenValidDeleting(Response response) {
    response
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true))
            .log().all();
  }

  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на первую корректную регистрацию - 201")
  public void checkAnswerValidRegistration(Response response) {
    response
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true))
            .log().all();
  }

  @Step("Проверка тела ответа при попытке повторной регистрации под уже существующим логином - 409 Сonflict")
  public void checkAnswerReuseRegistrationData(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .and().assertThat().body("message", equalTo("Этот логин уже используется."));
  }

  @Step("Проверка тела ответа и статус кода сервера при неполных данных при регистрации - 400 Bad Request")
  public void checkAnswerWithNotEnoughRegData(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .and().assertThat().body("message", CoreMatchers.equalTo("Недостаточно данных для создания учетной записи"));
  }

  @Step("Проверка тела ответа и статус кода сервера на удаление курьера c id = null - 500")
  public void checkAnswerThenInValidDeletingWithoutId(Response response) {
    response
            .then()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .and().assertThat().body("message", equalTo("invalid input syntax for type integer: \"null\""))
            .log().all();
  }

  @Step("Проверка тела ответа и статус кода сервера на удаление курьера c несуществующим id - 404 Not Found")
  public void checkAnswerThenInValidDeletingWithUnrealId(Response response) {
    response
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and().assertThat().body("message", equalTo("Not Found."));
  }

  @Step("Проверка наличия id курьера по его логину и паролю - статус код 200")
  public void checkAnswerAndPresenceId(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_OK)
            .and().assertThat().body("id", notNullValue());
  }

  @Step("Проверка ошибки системы при попытке входа с несуществующей парой логин-пароль, статус-код 404 Not Found")
  public void checkAnswerWithWrongData(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .assertThat().body("message", CoreMatchers.equalTo("Учетная запись не найдена"));
  }

  @Step("Проверка ошибки системы при попытке входа без логина или пароля, статус-код 400 Bad Request ")
  public void checkAnswerWithoutData(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .assertThat().body("message", CoreMatchers.equalTo("Недостаточно данных для входа"));
  }


}
