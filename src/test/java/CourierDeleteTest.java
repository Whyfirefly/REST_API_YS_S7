import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.CourierStepsApi;
import steps.CourierStepsChecks;

import static random_data.RandomData.*;

public class CourierDeleteTest {
  CourierStepsApi courierStepsApi;
  CourierStepsChecks courierStepsChecks;

  @Before
  public void setUp() {
    courierStepsApi = new CourierStepsApi();
    courierStepsChecks = new CourierStepsChecks();
  }

  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutPass() {

    Response responseDeleteWithoutPass = courierStepsApi.deleteCourier(RANDOM_LOGIN, " ");
    courierStepsChecks.checkAnswerThenInValidDeletingWithoutId(responseDeleteWithoutPass);
  }

  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutLogin() {

    Response responseDeleteWithoutLogin = courierStepsApi.deleteCourier(" ", RANDOM_PASS);
    courierStepsChecks.checkAnswerThenInValidDeletingWithoutId(responseDeleteWithoutLogin);
  }

  @Test
  @DisplayName("Удаление курьера c несуществующим id")
  @Description("Проверка неуспешного удаления курьера c несуществующим id")
  public void deleteCourierNegativeWithUnrealId() {

    Response responseDeleteWithInvalidCourierId = courierStepsApi.setRandomCourierId(RANDOM_COURIER_ID);
    courierStepsChecks.checkAnswerThenInValidDeletingWithUnrealId(responseDeleteWithInvalidCourierId);
  }
}
