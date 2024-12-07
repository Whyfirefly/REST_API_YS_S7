package pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreatedMain {
  //поле типа OrderFieldsGetByTrackNumber
  private OrderFieldsGetByTrackNumber order;

  //конструктор с параметрами
  public OrderCreatedMain(OrderFieldsGetByTrackNumber orderFieldsGetByTrackNumber) {
    this.order = orderFieldsGetByTrackNumber;
  }

}
