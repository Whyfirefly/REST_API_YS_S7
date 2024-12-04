import constants.Urls;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderCreatedMain;
import steps.CourierSteps;
import steps.OrderSteps;

import static constants.RandomData.*;
import static constants.Urls.ORDER_GET_BY_NUMBER;
import static constants.Urls.ORDER_PUT_ACCEPT_ORDER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class OrderAcceptanceTest {
  CourierSteps courierSteps;
  OrderSteps orderSteps;

  @Before
  public void setUp() {
    courierSteps = new CourierSteps();
    orderSteps = new OrderSteps();
  }
  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на удаление курьера - 200")
  public void checkAnswerThenValidDeleting(Response response) {
    response
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));
  }

  @After
  public void cleanUp() {
    if (courierSteps != null) {
      Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
      checkAnswerThenValidDeleting(responseDelete);
      System.out.println(responseDelete.asString());}
  }

  //создаем курьера, получаем его id в системе
  @Step("Проверка тела ответа - (ok: true) и статус кода сервера на первую корректную регистрацию - 201")
  public void checkAnswerValidRegistration(Response response) {
    response
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));
  }

  @Step("Проверка тела ответа при создании заказа с \"track\"")
  public void checkOrderTrackNotNullNew(Response response) {
    response.then()
            .statusCode(HttpStatus.SC_CREATED)
            .and()
            .assertThat().body("track", notNullValue());
    MatcherAssert.assertThat(response, notNullValue()) ;
  }

  @Step("Успешный прием заказа (200) по существующим id номеру курьера и id номеру заказа")
  @Test
  @DisplayName("Создание курьера и заказа, получение их id, проверка успешного принятия заказа")
  @Description("Проверка успешного принятия заказа - получен код 200")
  public void checkPositiveAcceptanceOrder() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierSteps.getCourierId(RANDOM_LOGIN,RANDOM_PASS);
    System.out.println("Courier's id is "+ courierID);

    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Проверка ответа 200 и непустоты тела ответа при вводе существующего треккингового номера заказа
    OrderCreatedMain orderCreatedMain = given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            //десериализуем JSON
            .body().as(OrderCreatedMain.class);
    int orderId = orderCreatedMain.getOrder().getId();
    System.out.println("New order's id is " + orderId);

    //Попытка принятия заказа по существующим id заказа и курьера
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));
  }

  @Step("Пробуем принять заказ, который уже был в работе")
  @Test
  @DisplayName("Попытка повторного приема заказа с теми же данными (409)")
  @Description("Попытка повторного приема заказа с теми же данными (код 409)")
  public void checkAcceptanceOrderAlreadyInWork() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierSteps.getCourierId(RANDOM_LOGIN,RANDOM_PASS);
    System.out.println("Courier's id is "+ courierID);

    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Проверка ответа 200 и непустоты тела ответа при вводе существующего треккингового номера заказа
    OrderCreatedMain orderCreatedMain = given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            //десериализуем JSON
            .body().as(OrderCreatedMain.class);
    int orderId = orderCreatedMain.getOrder().getId();
    System.out.println("New order's id is " + orderId);

    //Попытка принятия заказа по существующим id заказа и курьера
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and().assertThat().body("ok", CoreMatchers.equalTo(true));

    //Попытка повторного принятия заказа по тем же id заказа и курьера
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_CONFLICT)
            .and().assertThat().body("message", equalTo("Этот заказ уже в работе"));
  }

  @Step("Пробуем принять заказ без id заказа")
  @Test
  @DisplayName("Попытка принять заказ без id заказа (400)")
  @Description("Попытка принять заказ без id заказа (код 400)")
  public void checkAcceptanceOrderWithoutOrderId() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierSteps.getCourierId(RANDOM_LOGIN,RANDOM_PASS);
    System.out.println("Courier's id is "+ courierID);

    //Нет id заказа
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", RANDOM_COURIER_ID)
            .put(ORDER_PUT_ACCEPT_ORDER)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .and().assertThat().body("message", equalTo("Недостаточно данных для поиска"));
  }

  @Step("Пробуем принять заказ без id курьера")
  @Test
  @DisplayName("Попытка принять заказ без id курьера (400)")
  @Description("Попытка принять заказ без id курьера (код 400)")
  public void checkAcceptanceOrderWithoutCourierId() {
    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Проверка ответа 200 и непустоты тела ответа при вводе существующего треккингового номера заказа
    OrderCreatedMain orderCreatedMain = given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            //десериализуем JSON
            .body().as(OrderCreatedMain.class);
    int orderId = orderCreatedMain.getOrder().getId();
    System.out.println("New order's id is " + orderId);

    //Нет id курьера
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", "")
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .and().assertThat().body("message", equalTo("Недостаточно данных для поиска"));

  }

  @Step("Пробуем принять заказ c несуществующим номером id заказа")
  @Test
  @DisplayName("Попытка принять заказ c несуществующим номером id заказа (404)")
  @Description("Попытка принять заказ c несуществующим номером id заказа (код 404)")
  public void checkAcceptanceOrderWithInvalidOrderId() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
    checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierSteps.getCourierId(RANDOM_LOGIN,RANDOM_PASS);
    System.out.println("Courier's id is "+ courierID);

    //Запрос с несуществующим номером заказа
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", courierID)
            .put(ORDER_PUT_ACCEPT_ORDER + RANDOM_ORDER_ID)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and().assertThat().body("message", equalTo("Заказа с таким id не существует"));

  }

  @Step("Пробуем принять заказ c несуществующим номером id курьера")
  @Test
  @DisplayName("Попытка принять заказ c несуществующим номером id курьера (404)")
  @Description("Попытка принять заказ c несуществующим номером id курьера (код 404)")
  public void checkAcceptanceOrderWithInvalidCourierId() {

    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Проверка ответа 200 и непустоты тела ответа при вводе существующего треккингового номера заказа
    OrderCreatedMain orderCreatedMain = given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("t", trackNumber)
            .get(ORDER_GET_BY_NUMBER)
            //десериализуем JSON
            .body().as(OrderCreatedMain.class);
    int orderId = orderCreatedMain.getOrder().getId();
    System.out.println("New order's id is " + orderId);

    //Запрос с несуществующим id курьера
    given().log().all()
            .baseUri(Urls.BASE_URL)
            .queryParam("courierId", RANDOM_COURIER_ID)
            .put(ORDER_PUT_ACCEPT_ORDER + orderId)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .and().assertThat().body("message", equalTo("Курьера с таким id не существует"));

  }
  }

