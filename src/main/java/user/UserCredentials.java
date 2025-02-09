package user;

import net.datafaker.Faker;


public class UserCredentials {
    private static final Faker faker = new Faker();
    static String incorrectData = faker.lorem().word();
    private String email;
    private String password;


    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(UserModel userModel) {
        return new UserCredentials(userModel.getEmail(), userModel.getPassword());
    }

    public static UserCredentials getCredentialsWithIncorrectEmail(UserModel userModel) {
        return new UserCredentials(incorrectData, userModel.getPassword());
    }

    public static UserCredentials getCredentialsWithIncorrectPassword(UserModel userModel) {
        return new UserCredentials(userModel.getEmail(), incorrectData);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
