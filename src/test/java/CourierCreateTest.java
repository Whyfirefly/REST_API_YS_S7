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

  @After
  public void cleanUp() {
    if (courierSteps != null) {
      Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
      courierSteps.checkAnswerThenValidDeleting(responseDelete);
      System.out.println(responseDelete.asString());}
  }

  @Test
  @DisplayName("Создание нового курьера")
  @Description("Создание нового курьера с корректными данными и проверка того, что он создался - получен код 201")
  public void creatingCourierPositive() {
    Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerValidRegistration(responseCreate);
    //System.out.println(responseCreate.asString());

  }

  @Test
  @DisplayName("Повторное создание существующего курьера")
  @Description("Проверка ответа api (статус кода и тела) при попытке создания идентичного курьера существующему")
  public void creatingIdenticalCouriersConflict() {
    courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);

    Response responseIdentical = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerReuseRegistrationData(responseIdentical);
    System.out.println(responseIdentical.asString());
  }

  @Test
  @DisplayName("Создание курьера с уже существующим логином и паролем")
  @Description("Проверка кода и тела ответа при попытке создания курьера с уже существующим логином и паролем")
  public void creatingCourierWithExistingLoginConflict() {
    courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    Response responseExisting = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerReuseRegistrationData(responseExisting);
    System.out.println(responseExisting.asString());
  }

  @Test
  @DisplayName("Создание курьера без логина")
  @Description("Проверка тела ответа при создании курьера без логина")
  public void creatingCourierWithoutLoginBadRequest() {
    Response responseWithoutLogin = courierSteps.createCourier("", RANDOM_PASS, RANDOM_NAME);
    courierSteps.checkAnswerWithNotEnoughRegData(responseWithoutLogin);
    System.out.println(responseWithoutLogin.asString());

  }

  @Test
  @DisplayName("Создание курьера без пароля")
  @Description("Проверка тела ответа при создании курьера без пароля")
  public void creatingCourierWithoutPasswordBadRequest() {
    Response responseWithoutPass = courierSteps.createCourier(RANDOM_LOGIN, "", RANDOM_NAME);
    courierSteps.checkAnswerWithNotEnoughRegData(responseWithoutPass);
    System.out.println(responseWithoutPass.asString());

  }

  @Test
  @DisplayName("Создание курьера без указания имени")
  @Description("Проверка тела ответа при создании курьера без указания имени")
  public void creatingCourierWithoutNamePositive() {
    Response responseWithoutName = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, "");
    courierSteps.checkAnswerValidRegistration(responseWithoutName);
    System.out.println(responseWithoutName.asString());

  }

}
