import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import steps.OrderStepsApi;
import steps.OrderStepsChecks;


public class OrderGetListTest {
  OrderStepsApi orderStepsApi;
  OrderStepsChecks orderStepsChecks;

  @Before
  public void setUp() {
    orderStepsApi = new OrderStepsApi();
    orderStepsChecks = new OrderStepsChecks();
  }

  @Test
  @DisplayName("Получение списка заказов")
  @Description("Получение списка заказов, проверка наличия списка, что он не пустой")
  public void orderGetList() {
    OrderStepsApi orderStep = new OrderStepsApi();
    orderStepsChecks.checkGetListOrder();
  }

}