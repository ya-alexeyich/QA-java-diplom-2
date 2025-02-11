package user;

import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static @NotNull UserModel getRandom() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();

        return new UserModel(email, password, name);
    }
}
