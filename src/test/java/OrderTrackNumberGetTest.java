import constants.Urls;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static constants.RandomData.RANDOM_ORDER_TRACK;
import static constants.Urls.ORDER_GET_BY_NUMBER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class OrderTrackNumberGetTest {

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
    MatcherAssert.assertThat(response, notNullValue()) ;
  }

  @Step("Создаем заказ, затем проверяем, что заказ получен по существующему номеру заказа - статус код 200")
  @Test
  @DisplayName("Успешно получаем созданный заказ по его номеру")
  @Description(value = "Проверка, что заказ находится по существующему номеру заказа")
  public void getOrderByValidTrackNumber() {

    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int TrackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + TrackNumber);

    //Проверка ответа 200 и непустоты тела ответа при вводе существующего треккингового номера заказа
    given().log().all()
           .baseUri(Urls.BASE_URL)
            .queryParam("t", TrackNumber)
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .assertThat().body("order", is(not(empty())));
    //.body().as(OrderCreatedMain.class) - добавить в новый класс
  }

  @Step("Проверка получения заказа по несуществующему номеру id")
  @Test
  @DisplayName("Получаем ошибку при попытке поиска заказа по несуществующему номеру")
  @Description(value = "Проверка, что выходит ошибка \"Заказ не найден\" со статусом 404")
  public void getOrderByInValidTrackNumber() {

    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("t", RANDOM_ORDER_TRACK)
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and()
            .assertThat().body("message", equalTo("Заказ не найден"));
  }

  @Step("Проверка получения заказа без id")
  @Test
  @DisplayName("Получаем ошибку при попытке поиска заказа по несуществующему номеру")
  @Description(value = "Проверка, что выходит ошибка \"Недостаточно данных для поиска\" со статусом 400")
  public void getOrderWithEmptyTrackNumber() {

    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("t", "")
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .and()
            .assertThat().body("message", equalTo("Недостаточно данных для поиска"));
  }
}
