import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CreateCourier;
import steps.CourierStepsApi;
import steps.CourierStepsChecks;

import static random_data.CourierGeneratorData.getRandomCourier;
import static random_data.RandomData.*;
import static steps.CourierData.getCourierData;
import static steps.LoginCourierWithData.loginCourierWithData;

public class CourierCreateTest {

  CourierStepsApi courierStepsApi;
  CourierStepsChecks courierStepsChecks;

  @Before
  public void setUp() {
    courierStepsApi = new CourierStepsApi();
    courierStepsChecks = new CourierStepsChecks();
  }

  @After
  public void afterTestDelete() {
    if (courierStepsApi.getCourierId(loginCourierWithData("Jora" + RANDOM_LOGIN, RANDOM_PASS)) != null) {
      Response responseDelete = courierStepsApi.deleteCourier(loginCourierWithData("Jora" + RANDOM_LOGIN, RANDOM_PASS));
      courierStepsChecks.checkAnswerThenValidDeleting(responseDelete);
    }
  }

  @Test
  @DisplayName("Создание нового курьера")
  @Description("Создание нового курьера с корректными данными и проверка того, что он создался - получен код 201")
  public void creatingCourierPositive() {
    CreateCourier createCourier = getRandomCourier("Jora", "pass", "Jora");
    Response responseCreate = courierStepsApi.createCourier(createCourier);
    courierStepsChecks.checkAnswerValidRegistration(responseCreate);

  }

  @Test
  @DisplayName("Повторное создание существующего курьера")
  @Description("Проверка ответа api (статус кода и тела) при попытке создания идентичного курьера существующему")
  public void creatingIdenticalCouriersConflict() {
    Response responseIdentical = courierStepsApi.createCourier(getRandomCourier("Jora", "pass", "Matvei"));
    courierStepsChecks.checkAnswerReuseRegistrationData(responseIdentical);

  }


  @Test
  @DisplayName("Создание курьера без логина")
  @Description("Проверка тела ответа при создании курьера без логина")
  public void creatingCourierWithoutLoginBadRequest() {
    CreateCourier createCourier = getCourierData("", "pass", "Jora");
    Response responseWithoutLogin = courierStepsApi.createCourier(createCourier);
    courierStepsChecks.checkAnswerWithNotEnoughRegData(responseWithoutLogin);

  }

  @Test
  @DisplayName("Создание курьера без пароля")
  @Description("Проверка тела ответа при создании курьера без пароля")
  public void creatingCourierWithoutPasswordBadRequest() {
    Response responseWithoutPass = courierStepsApi.createCourier(getCourierData("Jora", "", "Jora"));
    courierStepsChecks.checkAnswerWithNotEnoughRegData(responseWithoutPass);
  }

  @Test
  @DisplayName("Создание курьера без указания имени")
  @Description("Проверка тела ответа при создании курьера без указания имени")
  public void creatingCourierWithoutNamePositive() {
    Response responseWithoutName = courierStepsApi.createCourier(getCourierData("Jora" + RANDOM_LOGIN, "pass" + RANDOM_PASS, ""));
    courierStepsChecks.checkAnswerValidRegistration(responseWithoutName);
  }
}
