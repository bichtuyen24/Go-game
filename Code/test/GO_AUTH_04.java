import org.example.com.company.DatabaseManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GO_AUTH_04 {
    DatabaseManager db = DatabaseManager.getInstance();

    @Test
    void GO_AUTH_04_WrongPassword() throws Exception {

        String username = "wrongpass";

        db.insertUser(username, "mail", "123456");

        boolean login = db.login(username, "999999");

        assertFalse(login);
    }
}
