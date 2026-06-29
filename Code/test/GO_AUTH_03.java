import org.example.com.company.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GO_AUTH_03 {
    DatabaseManager db;

    @BeforeEach
    void init() {
        db = DatabaseManager.getInstance();
    }

    @Test
    void GO_AUTH_03_LoginSuccess() throws Exception {

        String username = "login_" + System.currentTimeMillis();

        db.insertUser(username,"mail","123456");

        boolean login =
                db.login(username,"123456");

        assertTrue(login);

    }
}
