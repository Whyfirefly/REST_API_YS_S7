import constants.Urls;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static io.restassured.RestAssured.given;
import static constants.Urls.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class OrderGetListTest {
  OrderSteps orderSteps;

  @Before
  public void setUp() {
    orderSteps = new OrderSteps();
  }

  @Step("Проверка тела ответа при создании заказа с \"track\"")
  public void checkOrderTrackNotNullNew(Response response) {
    response.then()
            .statusCode(201)
            .and()
            .assertThat().body("track", notNullValue());
  }

  @Step("Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов")
  @Test
  @DisplayName("Успешное создание заказа")
  @Description(value = "Проверка, что заказ создаётся из JSON файла успешно")
  public void creatingOrderSuccessWithJson() {
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println(createOrderResponse.asString());
  }

  @Step("Проверка наличия списка заказов")
  @Test
  @DisplayName("Получение списка заказов")
  @Description("Получение списка заказов, проверка наличия списка, что он не пустой")
  public void orderGetList() {
           given().log().all()
            .baseUri(Urls.BASE_URL)
            .get(ORDER_GET_LIST)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat().body("orders", is(not(empty())));
    System.out.println(given().body("orders"));
  }

}