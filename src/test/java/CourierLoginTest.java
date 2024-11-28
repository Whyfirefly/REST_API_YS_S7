import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static constants.RandomData.*;
import static constants.Urls.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class CourierLoginTest {
  CourierSteps courierSteps;

  @Before
  public void setUp() {
    RestAssured.baseURI = BASE_URL;
    courierSteps = new CourierSteps();
    courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
  }

  @Step("Проверка наличия id курьера по его логину и паролю - статус код 200")
  public void checkAnswerAndPresenceId(Response response) {
    response.then()
            .statusCode(200).and().assertThat().body("id", notNullValue());
  }

  @Test
  @DisplayName("Успешная авторизация курьера")
  @Description("Проверка, что при успешной регистрации курьера возвращается id курьера")
  public void loginCourierSuccess() {
    Response loginResponse = courierSteps.loginCourier(RANDOM_LOGIN, RANDOM_PASS);
    checkAnswerAndPresenceId(loginResponse);

  }

  @Step("Проверка ошибки системы при попытке входа с несуществующей парой логин-пароль, статус-код 404 Not Found")
  public void checkAnswerWithWrongData(Response response) {
    response.then()
            .statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));
  }

  @Test
  @DisplayName("Неуспешная авторизация с неподходящим логином курьера")
  @Description("Создание нового курьера, попытка авторизации с несуществующим логином и проверка неуспеха авторизации, код статуса ответа = 404")
  public void loginCourierWithIncorrectLoginFailed() {
    Response wrongLoginResponse = courierSteps.loginCourier("Ohhhh", RANDOM_PASS);
    checkAnswerWithWrongData(wrongLoginResponse);

  }

  @Test
  @DisplayName("Неуспешная авторизация с неподходящим паролем курьера")
  @Description("Создание нового курьера,попытка авторизации с неправильным паролем и проверка неуспеха авторизации, код статуса ответа = 404")
  public void loginCourierWithIncorrectPassFailed() {
    Response wrongPassResponse = courierSteps.loginCourier(RANDOM_LOGIN, "9876");
    checkAnswerWithWrongData(wrongPassResponse);

  }

  @Step("Проверка ошибки системы при попытке входа без логина или пароля, статус-код 400 Bad Request ")
  public void checkAnswerWithoutData(Response response) {
    response.then()
            .statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
  }

  @Test
  @DisplayName("Неуспешная авторизация без указания логина курьера")
  @Description("Создание нового курьера, попытка авторизации без логина и проверка неуспеха авторизации, код статуса ответа = 400")
  public void loginCourierWithoutLoginFailed() {
    Response withoutLoginResponse = courierSteps.loginCourier("", RANDOM_PASS);
    checkAnswerWithoutData(withoutLoginResponse);

  }

  @Test
  @DisplayName("Неуспешная авторизация без указания пароля")
  @Description("Создание нового курьера, попытка авторизации без пароля и проверка неуспеха авторизации, код статуса ответа = 400")
  public void loginCourierWithoutPassFailed() {
    Response withoutPassResponse = courierSteps.loginCourier(RANDOM_LOGIN, "");
    checkAnswerWithoutData(withoutPassResponse);
  }

  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на удаление курьера - 200")
  public void checkAnswerThenValidDeleting(Response response) {
    response
            .then()
            .statusCode(200)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));
  }

  @After
  public void afterTestDelete() {
    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
    checkAnswerThenValidDeleting(responseDelete);
  }

}