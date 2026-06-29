import org.example.com.company.DatabaseManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
public class GO_AUTH_02 {
    DatabaseManager db = DatabaseManager.getInstance();
    @Test
    void GO_AUTH_02_RegisterDuplicateUsername() throws Exception {

        db.insertUser("gia","a@gmail.com","123");

        boolean result =
                db.insertUser("bao","b@gmail.com","456");

        assertFalse(result);

    }
}
