
import org.example.com.company.DatabaseManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class GO_AUTH_01 {
    DatabaseManager db;

    @BeforeEach
    void setup() {
        db = DatabaseManager.getInstance();
    }

    @Test
    void GO_AUTH_01_RegisterSuccess() throws Exception {

        String username = "user_test_" + System.currentTimeMillis();

        boolean result = db.insertUser(
                username,
                "test@gmail.com",
                "123456"
        );

        assertTrue(result);
    }
}
