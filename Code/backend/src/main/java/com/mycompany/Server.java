package main.java.com.mycompany;

public class Server {
    private static final int PORT = 9000;
    private static final int MAX_THREADS = 50;

    public static void main(String[] args) {
        DatabaseManager.getInstance();
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n Dang dung server...");
            threadPool.shutdown();
            try{
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();

                } catch (InterruptedException e) {
                    threadPool.shutdownNow();
                    Thread.currentThread().interrupt();
                }

            DatabaseManager.getInstance().close();
            System.out.println("Server da dung hoan toan. ");

            }
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);

            System.out.println("=".repeat(56));
            System.out.println(" AUTH SERVER - RAW TCP - Da khoi dong");
            System.out.println("=".repeat(56));
            System.out.println(" Cong TCP: %d%n", PORT);
            System.out.println(" Thread Pool: toi da %d luong song song%n", MAX_THREADS);
            System.out.println(" Giao thuc: Ma hanh dong|{JSON}\\n%n");
            System.out.println(" Database: %s%n", new java.io.File("user.db".getAbsolutePath()));
            System.out.println(" CTRL+C de dung server.");
            System.out.println("=".repeat(56));

            while (true) {
                Socket clientSocket = serverSocket.accept();

                String addr = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                int active = ((ThreadPoolExecutor) threadPool).getActiveCount();
                System.out.println("Client ket noi: " + addr + " [Luong: " + active + "/" + MAX_THREADS + "]");

                threadPool.submit(new ClientHandler(clientSocket));
            }
        }catch (IOExeption e) {
            System.err.println("[Server] Loi: " + e.getMessage());
            System.err.println("[Server] Cong: " + PORT + " co the dang bi chiem.");
            System.err.println("[Server] Doi PORT sang so khac.");
        }
    }
}
