import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierStepsApi;
import steps.CourierStepsChecks;

import static random_data.CourierGeneratorData.getRandomCourier;
import static random_data.RandomData.*;
import static steps.LoginCourierWithData.loginCourierWithData;


public class CourierLoginTest {
  CourierStepsApi courierStepsApi;
  CourierStepsChecks courierStepsChecks;

  @Before
  public void setUp() {
    courierStepsApi = new CourierStepsApi();
    courierStepsApi.createCourier(getRandomCourier("Maks", "pass", "Maks"));
    courierStepsChecks = new CourierStepsChecks();
  }

  @After
  public void afterTestDelete() {
    Response responseDelete = courierStepsApi.deleteCourier(loginCourierWithData("Maks" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
    courierStepsChecks.checkAnswerThenValidDeleting(responseDelete);
  }

  @Test
  @DisplayName("Успешная авторизация курьера")
  @Description("Проверка, что при успешной регистрации курьера возвращается id курьера")
  public void loginCourierSuccess() {
    Response loginResponse = courierStepsApi.loginCourier(loginCourierWithData("Maks" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
    courierStepsChecks.checkAnswerAndPresenceId(loginResponse);
  }

  @Test
  @DisplayName("Неуспешная авторизация с неподходящим логином курьера")
  @Description("Создание нового курьера, попытка авторизации с несуществующим логином и проверка неуспеха авторизации, код статуса ответа = 404")
  public void loginCourierWithIncorrectLoginFailed() {
    Response wrongLoginResponse = courierStepsApi.loginCourier(loginCourierWithData("Ohhhh", "pass" + RANDOM_PASS));
    courierStepsChecks.checkAnswerWithWrongData(wrongLoginResponse);
  }

  @Test
  @DisplayName("Неуспешная авторизация с неподходящим паролем курьера")
  @Description("Создание нового курьера,попытка авторизации с неправильным паролем и проверка неуспеха авторизации, код статуса ответа = 404")
  public void loginCourierWithIncorrectPassFailed() {
    Response wrongPassResponse = courierStepsApi.loginCourier(loginCourierWithData("Maks" + RANDOM_LOGIN, "65890g"));
    courierStepsChecks.checkAnswerWithWrongData(wrongPassResponse);
  }


  @Test
  @DisplayName("Неуспешная авторизация без указания логина курьера")
  @Description("Создание нового курьера, попытка авторизации без логина и проверка неуспеха авторизации, код статуса ответа = 400")
  public void loginCourierWithoutLoginFailed() {
    Response withoutLoginResponse = courierStepsApi.loginCourier(loginCourierWithData("", "pass" + RANDOM_PASS));
    courierStepsChecks.checkAnswerWithoutData(withoutLoginResponse);
  }

  @Test
  @DisplayName("Неуспешная авторизация без указания пароля")
  @Description("Создание нового курьера, попытка авторизации без пароля и проверка неуспеха авторизации, код статуса ответа = 400")
  public void loginCourierWithoutPassFailed() {
    Response withoutPassResponse = courierStepsApi.loginCourier(loginCourierWithData("Maks" + RANDOM_LOGIN, ""));
    courierStepsChecks.checkAnswerWithoutData(withoutPassResponse);

  }

}