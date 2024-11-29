import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static constants.RandomData.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierDeleteTest {
  CourierSteps courierSteps;

  @Before
  public void setUp() {
    courierSteps = new CourierSteps();
  }

  @Step("Проверка тела ответа и статус кода сервера на удаление курьера c id = null - 500")
  public void checkAnswerThenInValidDeletingWithoutId(Response response) {
    response
            .then()
            .statusCode(500)
            .and().assertThat().body("message", equalTo("invalid input syntax for type integer: \"null\""));
  }

  @Step("Проверка, что если на удаление передаём только логин, тогда id = null, выйдет ошибка 'invalid input syntax for type integer: \"null\"'")
  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutPass() {

    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, " ");
    checkAnswerThenInValidDeletingWithoutId(responseDelete);
    System.out.println(responseDelete.asString());
  }

  @Step("Проверка, что если на удаление передаём только пароль, тогда id = null, выйдет ошибка 'invalid input syntax for type integer: \"null\"'")
  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutLogin() {

    Response responseDelete = courierSteps.deleteCourier(" ", RANDOM_PASS);
    checkAnswerThenInValidDeletingWithoutId(responseDelete);
    System.out.println(responseDelete.asString());
  }


  @Step("Проверка тела ответа и статус кода сервера на удаление курьера c несуществующим id - 404 Not Found")
  public void checkAnswerThenInValidDeletingWithUnrealId(Response response) {
    response
            .then()
            .statusCode(404)
            .and().assertThat().body("message", equalTo("Not Found."));
  }

  @Step("Проверка, что если на удаление передаём несуществующий id, выйдет ошибка 'Not Found.'")
  @Test
  @DisplayName("Удаление курьера c несуществующим id")
  @Description("Проверка неуспешного удаления курьера c несуществующим id")
  public void deleteCourierNegativeWithUnrealId() {

    Response responseDelete = courierSteps.setRandomCourierId(RANDOM_ID);
    checkAnswerThenInValidDeletingWithUnrealId(responseDelete);
    System.out.println(responseDelete);
  }
}
