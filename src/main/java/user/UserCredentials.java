package user;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.datafaker.Faker;

@Data
@AllArgsConstructor
public class UserCredentials {
    private static final Faker faker = new Faker();
    static String incorrectData = faker.lorem().word();
    private String email;
    private String password;

    public static UserCredentials from(UserModel userModel) {
        return new UserCredentials(userModel.getEmail(), userModel.getPassword());
    }

    public static UserCredentials getCredentialsWithIncorrectEmail(UserModel userModel) {
        return new UserCredentials(incorrectData, userModel.getPassword());
    }

    public static UserCredentials getCredentialsWithIncorrectPassword(UserModel userModel) {
        return new UserCredentials(userModel.getEmail(), incorrectData);
    }
}
