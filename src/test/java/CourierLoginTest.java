import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static constants.RandomData.*;



public class CourierLoginTest {
  CourierSteps courierSteps;

  @Before
  public void setUp() {
    courierSteps = new CourierSteps();
    courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
  }

  @After
  public void afterTestDelete() {
    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
    courierSteps.checkAnswerThenValidDeleting(responseDelete);
  }

  @Test
  @DisplayName("Успешная авторизация курьера")
  @Description("Проверка, что при успешной регистрации курьера возвращается id курьера")
  public void loginCourierSuccess() {
    Response loginResponse = courierSteps.loginCourier(RANDOM_LOGIN, RANDOM_PASS);
    courierSteps.checkAnswerAndPresenceId(loginResponse);
  }

  @Test
  @DisplayName("Неуспешная авторизация с неподходящим логином курьера")
  @Description("Создание нового курьера, попытка авторизации с несуществующим логином и проверка неуспеха авторизации, код статуса ответа = 404")
  public void loginCourierWithIncorrectLoginFailed() {
    Response wrongLoginResponse = courierSteps.loginCourier("Ohhhh", RANDOM_PASS);
    courierSteps.checkAnswerWithWrongData(wrongLoginResponse);
    System.out.println(wrongLoginResponse.asString());
  }

  @Test
  @DisplayName("Неуспешная авторизация с неподходящим паролем курьера")
  @Description("Создание нового курьера,попытка авторизации с неправильным паролем и проверка неуспеха авторизации, код статуса ответа = 404")
  public void loginCourierWithIncorrectPassFailed() {
    Response wrongPassResponse = courierSteps.loginCourier(RANDOM_LOGIN, "9876");
    courierSteps.checkAnswerWithWrongData(wrongPassResponse);
    System.out.println(wrongPassResponse.asString());
  }


  @Test
  @DisplayName("Неуспешная авторизация без указания логина курьера")
  @Description("Создание нового курьера, попытка авторизации без логина и проверка неуспеха авторизации, код статуса ответа = 400")
  public void loginCourierWithoutLoginFailed() {
    Response withoutLoginResponse = courierSteps.loginCourier("", RANDOM_PASS);
    courierSteps.checkAnswerWithoutData(withoutLoginResponse);
    System.out.println(withoutLoginResponse.asString());
  }

  @Test
  @DisplayName("Неуспешная авторизация без указания пароля")
  @Description("Создание нового курьера, попытка авторизации без пароля и проверка неуспеха авторизации, код статуса ответа = 400")
  public void loginCourierWithoutPassFailed() {
    Response withoutPassResponse = courierSteps.loginCourier(RANDOM_LOGIN, "");
    courierSteps.checkAnswerWithoutData(withoutPassResponse);
    System.out.println(withoutPassResponse.asString());

  }

}