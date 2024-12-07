package pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderFieldsGetByTrackNumber {

  private int id;
  private String firstName;
  private String lastName;
  private String address;
  private String metroStation;
  private String phone;
  private int rentTime;
  private String deliveryDate;
  private int track;
  private int status;
  private List<String> colour;
  private String comment;
  private boolean cancelled;
  private boolean finished;
  private boolean inDelivery;
  private String courierFirstName;
  private String createdAt;
  private String updatedAt;

  public OrderFieldsGetByTrackNumber(int id, String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, int track, int status, List<String> colour, String comment, boolean cancelled, boolean finished, boolean inDelivery, String courierFirstName, String createdAt, String updatedAt) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.metroStation = metroStation;
    this.phone = phone;
    this.rentTime = rentTime;
    this.deliveryDate = deliveryDate;
    this.track = track;
    this.status = status;
    this.colour = colour;
    this.comment = comment;
    this.cancelled = cancelled;
    this.finished = finished;
    this.inDelivery = inDelivery;
    this.courierFirstName = courierFirstName;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }


}
