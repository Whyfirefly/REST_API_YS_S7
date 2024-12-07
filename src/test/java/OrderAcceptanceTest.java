import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierStepsApi;
import steps.CourierStepsChecks;
import steps.OrderSteps;

import static random_data.CourierGeneratorData.getRandomCourier;
import static random_data.RandomData.*;
import static steps.LoginCourierWithData.loginCourierWithData;


public class OrderAcceptanceTest {
  CourierStepsApi courierStepsApi;
  OrderSteps orderSteps;
  CourierStepsChecks courierStepsChecks;

  @Before
  public void setUp() {
    courierStepsApi = new CourierStepsApi();
    orderSteps = new OrderSteps();
    courierStepsChecks = new CourierStepsChecks();
  }

  //Удаление курьера
  @After
  public void afterTestDelete() {
    if (courierStepsApi.getCourierId(loginCourierWithData("Rony" + RANDOM_LOGIN, "pass" + RANDOM_PASS)) != null) {
      Response responseDelete = courierStepsApi.deleteCourier(loginCourierWithData("Rony" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
      courierStepsChecks.checkAnswerThenValidDeleting(responseDelete);
    }
  }

  @Test
  @DisplayName("Создание курьера и заказа, получение их id, проверка успешного принятия заказа")
  @Description("Проверка успешного принятия заказа - получен код 200")
  public void acceptanceOrderByValidOrderIdAndValidCourierId() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierStepsApi.createCourier(getRandomCourier("Rony", "pass", "Rony"));
    courierStepsChecks.checkAnswerValidRegistration(responseCreate);

    int courierID = courierStepsApi.getCourierId(loginCourierWithData("Rony" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
    System.out.println("Courier's id is " + courierID);

    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    orderStep.checkOrderTrackNotNullNew(createOrderResponse);

    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение по нужному мне ключу
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Получаем номер id заказа по треккинговому номеру
    int orderId = orderStep.getOrderId(trackNumber);
    System.out.println("New order's id is " + orderId);

    //Попытка принятия заказа по существующим id заказа и курьера
    orderStep.checkAcceptanceOrderByValidCourierIdAndOrderId(courierID, orderId);

  }

  @Test
  @DisplayName("Попытка повторного приема заказа с теми же данными (409)")
  @Description("При попытке повторного приема заказа ожидаем ошибку \"Этот заказ уже в работе\"")
  public void checkAcceptanceOrderAlreadyInWork() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierStepsApi.createCourier(getRandomCourier("Rony", "pass", "Rony"));
    courierStepsChecks.checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierStepsApi.getCourierId(loginCourierWithData("Rony" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
    System.out.println("Courier's id is " + courierID);

    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    orderStep.checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Получаем номер id заказа по треккинговому номеру
    int orderId = orderStep.getOrderId(trackNumber);
    System.out.println("New order's id is " + orderId);

    //Попытка принятия заказа по существующим id заказа и курьера
    orderStep.checkAcceptanceOrderByValidCourierIdAndOrderId(courierID, orderId);

    //Попытка повторного принятия заказа по тем же id заказа и курьера
    orderStep.checkRepeatAcceptanceOrder(courierID, orderId);

  }

  @Test
  @DisplayName("Попытка принять заказ без id заказа (400)")
  @Description("Попытка принять заказ без id заказа (код 400)")
  public void checkAcceptanceOrderWithoutOrderId() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierStepsApi.createCourier(getRandomCourier("Rony", "pass", "Rony"));
    courierStepsChecks.checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierStepsApi.getCourierId(loginCourierWithData("Rony" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
    System.out.println("Courier's id is " + courierID);

    //Нет id заказа
    OrderSteps orderStep = new OrderSteps();
    orderStep.checkAcceptanceOrderWithoutOrderId(courierID);

  }

  @Test
  @DisplayName("Попытка принять заказ без id курьера (400)")
  @Description("Попытка принять заказ без id курьера (код 400)")
  public void checkAcceptanceOrderWithoutCourierId() {
    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    orderStep.checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Получаем номер id заказа по треккинговому номеру
    int orderId = orderStep.getOrderId(trackNumber);
    System.out.println("New order's id is " + orderId);

    //Проверка принятия заказа без id курьера
    orderStep.checkAcceptanceOrderWithoutCourierId(orderId);

  }

  @Test
  @DisplayName("Попытка принять заказ c несуществующим номером id заказа (404)")
  @Description("Попытка принять заказ c несуществующим номером id заказа (код 404)")
  public void checkAcceptanceOrderWithInvalidOrderId() {
    //создаем курьера, получаем его id в системе
    Response responseCreate = courierStepsApi.createCourier(getRandomCourier("Rony", "pass", "Rony"));
    courierStepsChecks.checkAnswerValidRegistration(responseCreate);
    System.out.println(responseCreate.asString());
    int courierID = courierStepsApi.getCourierId(loginCourierWithData("Rony" + RANDOM_LOGIN, "pass" + RANDOM_PASS));
    System.out.println("Courier's id is " + courierID);

    //Проверка запроса с несуществующим номером заказа
    OrderSteps orderStep = new OrderSteps();
    orderStep.checkAcceptanceOrderWithInvalidOrderId(courierID);

  }

  @Test
  @DisplayName("Попытка принять заказ c несуществующим номером id курьера (404)")
  @Description("Попытка принять заказ c несуществующим номером id курьера (код 404)")
  public void checkAcceptanceOrderWithInvalidCourierId() {

    //создаём заказ, получаем его id в системе
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    orderStep.checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());
    //Response class has method path() using that, user can give the json path to get the particular value.
    //То есть я ответ API конвертирую в строку и из строки достаю значение нужного мне ключа
    String orderNumberTrack = createOrderResponse.path("track").toString();
    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Получаем номер id заказа по треккинговому номеру
    int orderId = orderStep.getOrderId(trackNumber);
    System.out.println("New order's id is " + orderId);

    //Запрос с несуществующим id курьера
    orderStep.checkAcceptanceOrderWithInvalidCourierId(orderId);
  }
}

