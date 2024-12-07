package random_data;

import io.qameta.allure.Step;
import pojo.CreateCourier;

import static random_data.RandomData.*;

public class CourierGeneratorData {

  @Step("Generate random courier")
  public static CreateCourier getRandomCourier(String loginParam, String passwordParam,
                                               String firstNameParam) {
    String login = loginParam + RANDOM_LOGIN;
    String password = passwordParam + RANDOM_PASS;
    String firstName = firstNameParam + RANDOM_NAME;

    return new CreateCourier(login, password, firstName);
  }
}
