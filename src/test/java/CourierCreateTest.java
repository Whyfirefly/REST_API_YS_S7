import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static constants.RandomData.*;
import static constants.Urls.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

  CourierSteps courierSteps;

  @Before
  public void setUp() {
    RestAssured.baseURI = BASE_URL;
    courierSteps = new CourierSteps();
  }

  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на первую корректную регистрацию - 201")
  public void checkAnswerValidRegistration(Response response) {
    response
            .then()
            .statusCode(201)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));
  }

  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на удаление курьера - 200")
  public void checkAnswerThenValidDeleting(Response response) {
    response
            .then()
            .statusCode(200)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));
  }


  @Test
  @DisplayName("Создание нового курьера")
  @Description("Создание нового курьера с корректными данными и проверка того, что он создался - получен код 201")
  public void creatingCourierPositive() {
    Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    checkAnswerValidRegistration(responseCreate);

    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
    checkAnswerThenValidDeleting(responseDelete);
  }

  @Step("Проверка тела ответа при попытке повторной регистрации под уже существующим логином - 409 Сonflict")
  public void checkAnswerReuseRegistrationData(Response response) {
    response.then()
            .statusCode(409)
            .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
  }

  @Test
  @DisplayName("Повторное создание существующего курьера")
  @Description("Проверка ответа api (статус кода и тела) при попытке создания идентичного курьера существующему")
  public void creatingIdenticalCouriersConflict() {
    courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);

    Response responseIdentical = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    checkAnswerReuseRegistrationData(responseIdentical);
  }

  @Test
  @DisplayName("Создание курьера с уже существующим логином и паролем")
  @Description("Проверка кода и тела ответа при попытке создания курьера с уже существующим логином и паролем")
  public void creatingCourierWithExistingLoginConflict() {
    courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    Response responseExisting = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
                checkAnswerReuseRegistrationData(responseExisting);
    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
    checkAnswerThenValidDeleting(responseDelete);
  }

  @Step("Проверка тела ответа и статус кода сервера при неполных данных при регистрации - 400 Bad Request")
  public void checkAnswerWithNotEnoughRegData(Response response) {
    response.then()
            .statusCode(400)
            .and().assertThat().body("message", CoreMatchers.equalTo("Недостаточно данных для создания учетной записи"));
  }

  @Test
  @DisplayName("Создание курьера без логина")
  @Description("Проверка тела ответа при создании курьера без логина")
  public void creatingCourierWithoutLoginBadRequest() {
    Response responseWithoutLogin = courierSteps.createCourier("", RANDOM_PASS, RANDOM_NAME);
    checkAnswerWithNotEnoughRegData(responseWithoutLogin);
  }

  @Test
  @DisplayName("Создание курьера без пароля")
  @Description("Проверка тела ответа при создании курьера без пароля")
  public void creatingCourierWithoutPasswordBadRequest() {
    Response responseWithoutPass = courierSteps.createCourier(RANDOM_LOGIN, "", RANDOM_NAME);
    checkAnswerWithNotEnoughRegData(responseWithoutPass);
  }

  @Test
  @DisplayName("Создание курьера без указания имени")
  @Description("Проверка тела ответа при создании курьера без указания имени")
  public void creatingCourierWithoutNamePositive() {
    Response responseWithoutName = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, "");
    checkAnswerValidRegistration(responseWithoutName);
    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
    checkAnswerThenValidDeleting(responseDelete);
  }

}
