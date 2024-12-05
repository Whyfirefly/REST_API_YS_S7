import groovyjarjarantlr4.v4.runtime.atn.SemanticContext;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static constants.RandomData.*;

public class CourierCreateTest {

  CourierSteps courierSteps;

  @Before
  public void setUp() {
        courierSteps = new CourierSteps();
  }

  @Test
  @DisplayName("Создание нового курьера")
  @Description("Создание нового курьера с корректными данными и проверка того, что он создался - получен код 201")
  public void creatingCourierPositive() {
    Response responseCreate = courierSteps.createCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerValidRegistration(responseCreate);

    Response responseDelete = courierSteps.deleteCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS);
    courierSteps.checkAnswerThenValidDeleting(responseDelete);
  }

  @Test
  @DisplayName("Повторное создание существующего курьера")
  @Description("Проверка ответа api (статус кода и тела) при попытке создания идентичного курьера существующему")
  public void creatingIdenticalCouriersConflict() {
    courierSteps.createCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);

    Response responseIdentical = courierSteps.createCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerReuseRegistrationData(responseIdentical);

    Response responseDelete = courierSteps.deleteCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS);
    courierSteps.checkAnswerThenValidDeleting(responseDelete);
  }


  @Test
  @DisplayName("Создание курьера без логина")
  @Description("Проверка тела ответа при создании курьера без логина")
  public void creatingCourierWithoutLoginBadRequest() {
    Response responseWithoutLogin = courierSteps.createCourier("", RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerWithNotEnoughRegData(responseWithoutLogin);

  }

  @Test
  @DisplayName("Создание курьера без пароля")
  @Description("Проверка тела ответа при создании курьера без пароля")
  public void creatingCourierWithoutPasswordBadRequest() {
    Response responseWithoutPass = courierSteps.createCourier("Matvei" + RANDOM_LOGIN, "", RANDOM_NAME);
    courierSteps.checkAnswerWithNotEnoughRegData(responseWithoutPass);

  }

  @Test
  @DisplayName("Создание курьера без указания имени")
  @Description("Проверка тела ответа при создании курьера без указания имени")
  public void creatingCourierWithoutNamePositive() {
    Response responseWithoutName = courierSteps.createCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS, "");
    courierSteps.checkAnswerValidRegistration(responseWithoutName);

    Response responseDelete = courierSteps.deleteCourier("Matvei" + RANDOM_LOGIN, RANDOM_PASS);
    courierSteps.checkAnswerThenValidDeleting(responseDelete);

  }

}
