package tests;

import com.google.gson.Gson;
import models.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;


public class JsonTest {

    ClassLoader cl = FileTest.class.getClassLoader();
    Gson gson = new Gson();

    @Test
    void jsonTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files/testJson.json")) {
            String jsonAsString = new String(is.readAllBytes());
            User user = gson.fromJson(jsonAsString, User.class);
            assertThat(user.getId()).isEqualTo(1);
            assertThat(user.getName()).isEqualTo("John");
            assertThat(user.getSurname()).isEqualTo("Doe");
            assertThat(user.getGender()).isEqualTo("Male");
            assertThat(user.getCountry()).isEqualTo("China");
        }
    }
}
