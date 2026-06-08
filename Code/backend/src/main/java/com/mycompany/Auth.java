import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class Auth {
    
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


    private static final int BCRYPT_ROUNDS = 16;
    private static final int MIN_USER_LENGTH = 3;
    private static final int MIN_PASS_LENGTH = 8;

    private final Database db = Database.getInstance();

    public Result Register(String username, String email, String password) {
        if (username == null || username.trim().isEmpty()){
            return fail("Ten dang nhap khong duoc trong.");
        }
        username = username.trim();
        if (username.length() < MIN_USER_LENGTH)
            return fail("Ten dang nhap phai co it nhat " + MIN_USER_LENGTH + " ky tu.");
        if (password.length() < MIN_PASS_LENGTH)
            return fail("Mat khau phai co it nhat " + MIN_PASS_LENGTH + " ky tu.");

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS));

        try {
            boolean good = db.insertUser(username, email == null ? "" : email, hashed);
            if (good) {
                System.out.println("[AUTH} Dang ky thanh cong: \"" + username + "\"");
                return new Result(true, "Dang ky tai khoan thanh cong!", 0, username);

            }else {
                return fail ("Ten dang nhap da ton tai.");
            }
        } catch (SQLException e) {
            System.err.println("[AUTH] DB error: " + e.getMessage());
            return fail("Loi server!");
        }
    }
    puclic Result Login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()){
            return fail("Vui long nhap du thong tin.");
        }
        username = username.trim();

        Database.UserRecord user;
        try {
            user = db.findByUsername(username);
        
        } catch (SQLException e) {
            System.err.println("[AUTH} DB error: "+ e/.getMessage());
            return fail("Loi server!");
        }
        if (user == null){
            return fail("Ten dang nhap khong ton tai.");
        }
        boolean match = BCrypt.checkpw(password, user.passwordHash);
        if (match) {
            System.out.println("[AUTH] Dang nhap thanh cong: \"" + user.username() + "\" id =" + user.id());
            return new Result(true, "Dang nhap thanh cong!", user.id(), user.username());
        }else {
            System.out.println("[AUTH] Sai mat khau: \"" + username + "\"");
            return fail("Ten dang nhap hoac mat khau khong dung.");
        }
    }
}
