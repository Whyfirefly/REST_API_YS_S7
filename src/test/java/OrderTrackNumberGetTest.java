
import constants.Urls;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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
    //код для десериализации ответа в объект response
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
    System.out.println(createOrderResponse.asString());

//мне нужно ковертировать JSON createOrderResponse и получить значение ключа track, но я не могу разобраться, как это сделать
    given().log().all()
           .baseUri(Urls.BASE_URL)
            .queryParam("?t", createOrderResponse)
            .get(ORDER_GET_BY_NUMBER)
            .then()
            .statusCode(200)
            .and()
            .assertThat().body("order", is(not(empty())));
    System.out.println(given().body("order"));

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
            .statusCode(404)
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
            .statusCode(400)
            .and()
            .assertThat().body("message", equalTo("Недостаточно данных для поиска"));
    System.out.println(given().body("message"));

  }
}