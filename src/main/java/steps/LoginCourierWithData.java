package steps;

import io.qameta.allure.Step;
import pojo.LoginCourier;

public class LoginCourierWithData {
  @Step("Login courier")
  public static LoginCourier loginCourierWithData(String loginParam, String passwordParam) {
    return new LoginCourier(loginParam, passwordParam);
  }
}
