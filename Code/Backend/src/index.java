import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class index {
    public static void main(String[] args){
        String serverHost="127.0.0.1";
        int serverPort=8000;

        System.out.println("Dang ket noi "+ serverHost + ":" +serverPort+"...");
        try (Socket socket = new Socket(serverHost, serverPort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Ket noi thanh cong!");
            String apiRequest = "Hello |Get_user_info|id=123";
            out.println(apiRequest);
            System.out.println("Da gui yeu cau: " + apiRequest);

            String apiResponse = in.readLine();
            System.out.println("--- Ket qua nhan duoc ---");
            System.out.println(apiResponse);
        } catch (Exception e) {
            System.err.println("Loi ket noi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}