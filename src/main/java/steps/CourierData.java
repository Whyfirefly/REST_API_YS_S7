package steps;

import io.qameta.allure.Step;
import pojo.CreateCourier;

public class CourierData {

  @Step("Get courier data")
  public static CreateCourier getCourierData(String loginParam, String passwordParam,
                                             String firstNameParam) {
    return new CreateCourier(loginParam, passwordParam, firstNameParam);
  }
}
