package pojo;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderCreate {
  private String firstName;
  private String lastName;
  private String address;
  private String metroStation;
  private String phone;
  private int rentTime;
  private String deliveryDate;
  private String comment;
  private List<String> colour;

  public OrderCreate(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> colour) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.metroStation = metroStation;
    this.phone = phone;
    this.rentTime = rentTime;
    this.deliveryDate = deliveryDate;
    this.comment = comment;
    this.colour = colour;
  }

}
