import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import pojo.OrderCreate;
import steps.OrderStepsApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderStepsChecks;

import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreateTest {


  private final List<String> colour;

  public OrderCreateTest(List<String> color) {
    this.colour = color;
  }

  @Parameterized.Parameters(name = "Тестовые данные: {0}")
  public static Object[][] getTestData() {
    return new Object[][]{
            {List.of("")},
            {List.of("BLACK")},
            {List.of("GREY")},
            {List.of("BLACK", "GREY")},
    };
  }

  @Test
  @DisplayName("Успешное создание заказа")
  @Description(value = "Проверка, что заказ создаётся с различными вариациями цвета самоката")
  public void creatingOrderSuccessWthParam() {
    OrderStepsApi orderStep = new OrderStepsApi();
    OrderStepsChecks orderStepsChecks = new OrderStepsChecks();
    OrderCreate order = new OrderCreate("Виктор", "Пяточкин", "Воронеж", "8", "+79244568270", 3, "2024-11-29", "Пожалуйста, осторожнее.", colour);
    Response createOrderResponse = orderStep.createOrderWithLine(order);
    orderStepsChecks.checkOrderTrackNotNullNew(createOrderResponse);

    //Ответ API конвертируем в строку и из строки достаём значение по ключу track
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Отмена заказа
    orderStepsChecks.checkCancelOrderByValidTrackNumber(trackNumber);
  }

}
