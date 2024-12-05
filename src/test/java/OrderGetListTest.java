import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;


public class OrderGetListTest {
  OrderSteps orderSteps;

  @Before
  public void setUp() {
    orderSteps = new OrderSteps();
  }

  @Test
  @DisplayName("Получение списка заказов")
  @Description("Получение списка заказов, проверка наличия списка, что он не пустой")
  public void orderGetList() {
    OrderSteps orderStep = new OrderSteps();
    orderStep.checkGetListOrder();
  }

}