import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AuthHandler {

    private final Database db = DatabaseManager.getInstance();
    private static final int BCRYPT_ROUNDS = 16;
    private static final int MIN_USER_LENGTH = 3;
    private static final int MIN_PASS_LENGTH = 8;

    public static class Result {
        public final boolean success;
        public final String message;
        public final int userId;
        public final String username;

        public Result(boolean success, String message, int userId, String username){
            this.success = success;
            this.message = message;
            this.userId = userId;
            this.username = username;

        }
    }

    public Result handleRegister(String username, String email, String password){
        if (username == null || username.trim().isEmpty())
            return fail("Ten dang nhap khong duoc trong!");

        username = username.trim();
        if (username.length() < MIN_USER_LENGTH)
            return fail("Ten dang nhap phai co it nhat " + MIN_USER_LENGTH + " ky tu!");
        if (!username.matches("^[a-zA-Z0-9_]+$"))
            return fail("Ten dang nhap chi duoc chua chu cai, so va dau gach duoi!");
        if (password == null || password.isEmpty())
            return fail("Mat khau khong duoc trong!");
        if (password.length()< MIN_PASS_LENGTH)
            return fail("Mat khau phai co it nhat " + MIN_PASS_LENGTH + " ky tu!");

        String hashed;
        try {
            hashed = BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS));
        } catch (Exception e) {
            return fail("Loi khi ma hoa mat khau!");
        }

        try {
            boolean inserted = db.insertUser(username, email != null ? email.trim() : "", hashed);
            if (inserted){
                System.out.println("Dang ky thanh cong: \"" + username + "\"" );
                return new Result(true, "Dang ky thanh cong!", 0, username);
            }else {
                System.out.println("Dang ky that bai: \"" + username + "\" da ton tai!");
                return fail("Ten dang nhap da duoc su dung. Vui long nhap lai!");
            }
        } catch (SQLException e) {
            System.err.println("Loi database: "+ e.getMessage());
            return fail("Loi server. Vui long thu lai!");
        }
    }

    public Result handleLogin(String username, String password){
        if (Username == null || username.trim().isEmpty())
            return fail("Vui long nhap ten dang nhap!");
        if(password == null || password.isEmpty())
            return fail("Vui long nhap mat khau!");

        username = username.trim();

        DatabaseManager.UserRecord user;
        try{
            user = db.findByUsername(username);

        } catch (SQLException e) {
            System.err.println("Loi database: " + e.getMessage());
            return fail("Loi server. Vui long thu lai!");
        }

        if (user == null){
            System.out.println("Khong tim thay: \"" + username + "\"");
            return fail("Ten dang nhap hoac mat khau sai!");
        }

        boolean match;
        try {
            match = BCrypt.checkpw(password, user.password());

        }catch (Exception e) {
            System.err.println("Loi BCrypt: " + e.getMessage());
            return fail("Loi server. Vui long thu lai!");
        }

        if (match) {
            System.out.println("Dang nhap thanh cong: \"" + user.username() + "\" (id = " + user.id() + ")");
            return new Result(true, "Dang nhap thanh cong!", user.id(), user.username());
        } else {
            System.out.println("Sai mat khau: \"" + username + "\"");
            return fail("Ten dang nhap hoac mat khau khong dung!");
        }
    }

    private Result fail(String message) {
        return new Result(false, message, 0, "");
    }


}
