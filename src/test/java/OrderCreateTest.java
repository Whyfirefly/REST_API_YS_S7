import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import pojo.OrderCreate;
import steps.OrderSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static constants.Urls.BASE_URL;
import static org.hamcrest.Matchers.notNullValue;

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

  @Before
  public void setUp() {
    RestAssured.baseURI = BASE_URL;
  }

  @Step("Проверка тела ответа при создании заказа с \"track\"")
  public void checkOrderTrackNotNullNew(Response response) {
    response.then()
            .statusCode(201)
            .and()
            .assertThat().body("track", notNullValue());
  }

  @Test
  @DisplayName("Успешное создание заказа")
  @Description(value = "Проверка, что заказ создаётся с различными вариациями цвета самоката")
  public void creatingOrderSuccess() {
    OrderSteps orderStep = new OrderSteps();
    OrderCreate order = new OrderCreate("Василий", "Васильев", "Москва", "8", "+79224568299", 14, "2023-02-15", "Скорее!", colour);
    Response createOrderResponse = orderStep.createOrder(order);
    checkOrderTrackNotNullNew(createOrderResponse);
  }

}
