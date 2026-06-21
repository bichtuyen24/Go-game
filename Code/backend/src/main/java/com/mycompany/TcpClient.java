package main.java.com.mycompany;

public class TcpClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int    SERVER_PORT = "9000";
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void connect() throws IOException {
        socket = new Socket(SERVER_IP, SERVER_PORT);
        in = new BufferedReader (new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        System.out.println("[CLIENT] Da ket noi: " + SERVER_IP + ":" + SERVER_PORT);
    }

    public Packet send(Packet request) throws IOException {
        out.println(request.encode());
        String raw = in.readline();
        if (raw == null) throw new IOExeption("Server dong!");
        return Packet.decode(raw);
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        System.out.println("[CLIENT] Ngat ket noi!");
    }

    
}
