import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static constants.RandomData.*;

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

    Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, " ");
    courierSteps.checkAnswerThenInValidDeletingWithoutId(responseDelete);
    System.out.println(responseDelete.asString());
  }

  @Test
  @DisplayName("Удаление курьера без id")
  @Description("Проверка неуспешного удаления курьера без id")
  public void deleteCourierNegativeWithoutLogin() {

    Response responseDelete = courierSteps.deleteCourier(" ", RANDOM_PASS);
    courierSteps.checkAnswerThenInValidDeletingWithoutId(responseDelete);
    System.out.println(responseDelete.asString());
  }

  @Test
  @DisplayName("Удаление курьера c несуществующим id")
  @Description("Проверка неуспешного удаления курьера c несуществующим id")
  public void deleteCourierNegativeWithUnrealId() {

    Response responseDelete = courierSteps.setRandomCourierId(RANDOM_COURIER_ID);
    courierSteps.checkAnswerThenInValidDeletingWithUnrealId(responseDelete);
    System.out.println(responseDelete);
  }
}
