import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static random_data.RandomData.*;

public class CourierDeleteTest {
  CourierSteps courierSteps;

  @Before
  public void setUp() {
    courierSteps = new CourierSteps();
  }


  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutPass() {

    Response responseDeleteWithoutPass = courierSteps.deleteCourier(RANDOM_LOGIN, " ");
    courierSteps.checkAnswerThenInValidDeletingWithoutId(responseDeleteWithoutPass);
  }

  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutLogin() {

    Response responseDeleteWithoutLogin = courierSteps.deleteCourier(" ", RANDOM_PASS);
    courierSteps.checkAnswerThenInValidDeletingWithoutId(responseDeleteWithoutLogin);
  }

  @Test
  @DisplayName("Удаление курьера c несуществующим id")
  @Description("Проверка неуспешного удаления курьера c несуществующим id")
  public void deleteCourierNegativeWithUnrealId() {

    Response responseDeleteWithInvalidCourierId = courierSteps.setRandomCourierId(RANDOM_COURIER_ID);
    courierSteps.checkAnswerThenInValidDeletingWithUnrealId(responseDeleteWithInvalidCourierId);
  }
}
