package pojo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginCourier {

  private String login;
  private String password;

  public LoginCourier(String login, String password) {
    this.login = login;
    this.password = password;
  }

  }