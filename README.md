# Phân Công Công Việc – Dự Án Ứng Dụng Game Cờ Vây (Go)

Dự án được thực hiện bởi nhóm 6 thành viên. Mỗi người đảm nhận một mảng độc lập để đảm bảo tiến độ, tránh xung đột code và dễ dàng báo cáo khi bảo vệ.

## 1. FE 1 – UI/UX + Auth View
Phụ trách: Thiết kế và phát triển giao diện người dùng phần xác thực trên Desktop.
Nhiệm vụ chính:
Xây dựng màn hình Đăng nhập / Đăng ký bằng Java (FXML + CSS).
Bắt sự kiện người dùng và gửi thông tin qua luồng TCP Socket (gọi hàm từ module mạng).
Thiết kế UI/UX cơ bản, bố cục cửa sổ ứng dụng (Window stage).
Hỗ trợ render giao diện danh sách người chơi đang online.
---

## 2. FE 2 – Lobby + Room UI 
Phụ trách: Phát triển giao diện Lobby và phòng chơi.

Nhiệm vụ chính:
Thiết kế giao diện Lobby: danh sách phòng, nút tạo phòng, tham gia phòng.
Xây dựng khung giao diện phòng game (không xử lý logic).
Render khung Chat UI.
Lắng nghe gói tin từ TCP Client để cập nhật danh sách phòng chơi theo thời gian thực lên giao diện.

---

## 3. FE 3: Canvas Game + Ping Effect
Phụ trách: Hiển thị bàn cờ và hiệu ứng realtime phía giao diện Client.
Nhiệm vụ chính:
Vẽ bàn cờ bằng Java Canvas (GraphicsContext).
Vẽ quân đen / trắng, highlight vị trí, bắt sự kiện click chuột của người chơi.
Render nước đi lên màn hình ngay khi nhận được gói tin (Packet) từ Server gửi về.
Xử lý hiệu ứng Ping (chỉ điểm) bằng Canvas Animation trên Java.

---

## 4. FE/BE: TCP Client Socket + Packet Handler
Phụ trách: Xử lý kết nối TCP xuyên suốt và phân giải gói tin (Serialization/Deserialization) phía Client.

Nhiệm vụ chính:
Khởi tạo và quản lý java.net.Socket kết nối liên tục tới Server.
Định nghĩa các class Đọc/Ghi luồng byte (DataInputStream, DataOutputStream).
Đóng gói/Mở gói tin (Parse JSON sang Java Object bằng Gson/Jackson và ngược lại).
Đóng gói và gửi tín hiệu Ping/Chat qua luồng TCP chung.
Phân luồng sự kiện mạng (Listener Thread) để gọi ngược lại (Callback) cập nhật UI trên luồng JavaFX Application Thread (Platform.runLater).

---

## 5. BE 1: TCP Auth Server + Database + Protocol Manager
Phụ trách: Xử lý luồng xác thực, cơ sở dữ liệu và định nghĩa giao thức mạng.

Nhiệm vụ chính:
Xây dựng ServerSocket riêng biệt hoặc luồng riêng để xử lý các kết nối Đăng ký / Đăng nhập từ Client.
Kết nối và quản lý cơ sở dữ liệu SQLite qua JDBC.
Mã hóa mật khẩu bằng thư viện BCrypt (Java) trước khi lưu vào DB.
Định nghĩa Giao thức cấu trúc gói tin (Custom Protocol Packet) chung cho cả nhóm (Ví dụ quy định: Mã_Hành_Động|Dữ_Liệu_JSON).
Kiểm tra tính hợp lệ của dữ liệu (Validate input) phía Server để tránh SQL Injection.
## 6. BE 2 + TCP Game Server + Room Manager + Full-stack Integration
Phụ trách: Xử lý logic thời gian thực trên Server, quản lý phòng và tích hợp hệ thống.

Nhiệm vụ chính:
Xây dựng Server chính quản lý kết nối đa luồng (Thread Pool / ExecutorService) để giữ kết nối đồng thời với tất cả Client.
Quản lý Logic Lobby/Room: Tạo phòng, quản lý danh sách phòng, điều hướng Client khi vào/ra phòng.
Tiếp nhận gói tin nước đi, gói tin chat từ một Client và Broadcast (phát tán) tới Client đối thủ trong cùng phòng.
Viết logic trọng tài (Go Rules Engine) trên Server: Thuật toán BFS tìm nhóm quân, tính "khí", bắt quân, kiểm tra nước đi hợp lệ (luật cướp Ko, luật Tự sát), tính điểm lãnh thổ tự động.
Anti-Cheat: Server toàn quyền kiểm tra lượt đi, không cho phép Client gửi nước đi sai luật hoặc sai lượt.
Tích hợp toàn bộ hệ thống và kiểm thử luồng chạy (Luồng Auth -> Luồng Lobby -> Luồng Game).

---

## Lợi Ích Của Việc Chia Như Trên
- Không ai bị đụng code nhau.
- Nhiệm vụ rõ ràng, dễ chấm điểm cá nhân.
- Team làm việc khoa học, dễ quản lý tiến độ.
- Thể hiện rõ kiến trúc Client-Server truyền thống, cực kỳ phù hợp cho môi trường học thuật và làm nền tảng socket programming.

---

## Cấu Trúc Dự Án Đề Xuất
```
project/
│── backend/
│   ├── auth/
│   ├── database/
│   ├── core/
│   └── logic/
│
│── frontend/
│   ├── views/
│   ├── styles/
│   ├── controllers/
│   └── canvas/
│   └── network/
│
└── README.md
```

# Ngôn Ngữ & Công Nghệ Đề Xuất

Dự án được triển khai hoàn toàn bằng Java cho cả 2 phía Client và Server. Việc đồng nhất ngôn ngữ giúp tái sử dụng mã nguồn (ví dụ: các class Packet, models) và đơn giản hóa hệ thống.

## Frontend
- Hiển thị giao diện Đăng nhập và Đăng ký tài khoản.
- Hiển thị Lobby bao gồm danh sách phòng chơi và chức năng tạo/tham gia phòng.
- Hiển thị bàn cờ vây 19x19 bằng Java Canvas.
- Hiển thị quân cờ, lượt chơi hiện tại và trạng thái trận đấu.
- Hỗ trợ Chat trực tiếp giữa hai người chơi.
- Hiển thị kết quả trận đấu và điểm số cuối ván.

## Backend
- Java SE (Java 11 trở lên): Ngôn ngữ cốt lõi để xây dựng hệ thống Server.
- java.net.ServerSocket: Để mở cổng, lắng nghe và thiết lập kết nối TCP thuần với các Client.
- Thread Pool (ExecutorService): Quản lý, phân phối luồng (Multi-threading) để xử lý đồng thời hàng trăm kết nối TCP từ Client một cách tối ưu, tránh nghẽn mạng.
- SQLite + JDBC Driver: Hệ quản trị cơ sở dữ liệu nhẹ, không cần cài đặt phức tạp, tương tác qua JDBC của Java.
- jBCrypt: Thư viện Java chuyên dụng để mã hóa và kiểm tra mật khẩu an toàn.
- Gson / Jackson: Thư viện chuyển đổi dữ liệu (Object <-> JSON) để đóng gói và truyền tải qua luồng byte của TCP Socket.

## Vì sao chọn Java?
- Không phụ thuộc vào trình duyệt, tạo ra một ứng dụng game độc lập hoàn chỉnh (.jar / .exe).
- Học được cách thức mạng máy tính hoạt động ở tầng sâu (Transport Layer) qua việc thao tác trực tiếp với luồng byte thay vì dùng API HTTP hay thư viện bọc sẵn như Socket.IO.
- Dùng chung 1 ngôn ngữ cho cả Client và Server → Dễ chia việc, chia sẻ chung thư viện gói tin (Shared Packets), dễ debug.
- Java xử lý đa luồng (Multi-threading) cực kỳ mạnh mẽ và chuẩn mực, đáp ứng hoàn hảo cho cơ chế xử lý Game Server.


---

# 🚀 HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY DỰ ÁN

## Yêu Cầu Hệ Thống

- JDK: Java Development Kit phiên bản 11 trở lên (Khuyên dùng JDK 17).
- Công cụ Build: Maven hoặc Gradle.
- IDE: IntelliJ IDEA, Eclipse, hoặc NetBeans.
- Hệ điều hành: Windows, macOS, Linux (Java Write Once, Run Anywhere).

## Bước 1: Clone Dự Án

```bash
# Clone repository
git clone <repository-url>
cd Go-Socket
```

## Bước 2: Build Cài Đặt Dependencies (Maven)

```bash
# Tải các dependencies và build project
mvn clean install
```

## Các thư viện chính trong pom.xml:
- javafx-controls, javafx-fxml: Thư viện UI UI.
- sqlite-jdbc: Driver SQLite.
- jBCrypt: Mã hóa mật khẩu.
- gson: Parse JSON.

## Bước 3: Khởi Động Server

Mở class ServerMain.java trong module server trên IDE và nhấn Run, hoặc chạy lệnh sau qua Terminal:
```bash
java -cp target/server.jar com.go.server.ServerMain
```

Server sẽ báo log: Server started on TCP port 8080...

## Bước 4: Truy Cập Ứng Dụng

Mở class ClientMain.java trong module client trên IDE và nhấn Run (để mô phỏng nhiều người chơi, bạn chỉ cần Run class này nhiều lần để mở nhiều cửa sổ ứng dụng).

```bash
java -module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/client.jar
```

## Bước 5: Kiểm Thử Hệ Thống

1. Chạy Server.
2. Khởi chạy 2 cửa sổ Client Desktop.
3. Đăng ký 2 tài khoản khác nhau trên 2 cửa sổ.
4. User 1: Tạo phòng tại Lobby.
5. User 2: Nhấn làm mới Lobby và Tham gia phòng của User 1.
6. Bắt đầu chơi!

Xem chi tiết: [TESTING_GUIDE.md](TESTING_GUIDE.md)

---

## 🎮 Hướng Dẫn Chơi

### 1. Đăng Ký / Đăng Nhập
- Tạo tài khoản mới tại trang **Đăng Ký**
- Username phải có ít nhất 3 ký tự
- Password phải có ít nhất 6 ký tự

### 2. Tạo Phòng
- Tại **Lobby**, click nút **Tạo Phòng Mới**
- Nhập tên phòng và chọn kích thước bàn cờ (9x9, 13x13, 19x19)
- Chờ người chơi thứ 2 tham gia

### 3. Tham Gia Phòng
- Xem danh sách phòng đang chờ
- Click **Tham Gia** vào phòng bạn muốn

### 4. Chơi Game
- **Quân Đen** đi trước (người tạo phòng)
- Click vào bàn cờ để đánh
- Tuân thủ luật cờ vây:
  - Không được đánh vào ô đã có quân
  - Không được tự sát (đánh vào ô không có "khí")
  - Không được vi phạm luật Ko (đánh lại ngay lập tức)
- Bắt quân đối phương bằng cách bao vây hết "khí"
- Click **Bỏ Lượt** nếu không muốn đánh
- Click **Xin Thua** để đầu hàng

### 5. Kết Thúc Game
- Game kết thúc khi cả 2 người chơi **Bỏ Lượt** liên tiếp
- Hệ thống tự động tính điểm:
  - Lãnh thổ (Territory)
  - Tù binh (Captures)
  - Komi (6.5 điểm cho Quân Trắng)
- Người có điểm cao hơn thắng

---

## 🔒 Tính Năng Bảo Mật & Anti-Cheat

### Authentication
- ✅ Mật khẩu được hash bằng BCrypt phía Server.
- ✅ Quản lý phiên đăng nhập bằng Session Token cấp phát riêng qua TCP cho mỗi Client.
- ✅ Mọi gói tin TCP sau khi đăng nhập đều phải đính kèm Session Token để Server xác thực.

### Anti-Cheat (Chống gian lận)
- ✅ Server chặn mọi gói tin giả mạo tọa độ từ Client đã bị chỉnh sửa code.
- ✅ Kiểm tra đúng lượt chơi mới xử lý.
- ✅ Server nắm toàn quyền giữ trạng thái bàn cờ hiện tại, kiểm tra luật Ko, Suicide trước khi đồng ý nước đi.
- ✅ Bắt lỗi đứt kết nối (TCP Disconnect): Xử lý tự động xử thua người ngắt kết nối giữa trận.

---

## 🐛 Troubleshooting

## Lỗi: "Address already in use: JVM_Bind" hoặc "Port 8080 is in use"
Có một tiến trình Server khác đang chạy ngầm. Mở Task Manager tắt mọi tiến trình java.exe và chạy lại Server.

## Lỗi: NullPointerException khi mở giao diện Client
Kiểm tra lại đường dẫn file .fxml hoặc .css trong phương thức FXMLLoader.load(). Đảm bảo file nằm đúng trong thư mục resources.

## Database không lưu hoặc báo lỗi SQLite
Đảm bảo thư mục chứa file database.db đã được cấp quyền Ghi (Write), hoặc thử xóa file database.db đi để hệ thống tự động tạo lại (nếu đã code logic auto-create tables).

---

## 📊 Thông Tin Kỹ Thuật

### Luật Cờ Vây Được Implement
1. ✅ **Basic Rules**: Đánh lần lượt, không đè quân
2. ✅ **Liberty (Khí)**: Đếm số "khí" của quân/nhóm quân
3. ✅ **Capture (Bắt Quân)**: Bắt quân hết "khí"
4. ✅ **Ko Rule**: Không được lặp lại tình huống cướp
5. ✅ **Suicide Rule**: Không được tự sát (trừ khi bắt quân)
6. ✅ **Territory Scoring**: Tính điểm theo lãnh thổ
7. ✅ **Komi**: Đền bù 6.5 điểm cho Quân Trắng

|

#### 1. Game Logic (gameLogic.java)
- ✅ Thuật toán BFS tìm nhóm quân và đếm "khí"
- ✅ Validate nước đi (Ko, Suicide, Capture)
- ✅ Xử lý bắt quân
- ✅ Tính điểm tự động ở cuối trận

#### 2. Protocol Manager (PacketHandler.java)
- ✅ Giao thức Serialize/Deserialize bằng JSON.
- ✅ Phân luồng các Action_Code (vd: LOGIN_REQ, MOVE_RES, CHAT_PACKET).

#### 3. Tích hợp & Xử lý sự kiện mạng
- ✅ Đồng bộ luồng UI JavaFX (Platform.runLater()) để tránh lỗi Not on FX application thread.
- ✅ Xử lý ngắt kết nối an toàn (socket.close()).

#### 4. Testing (Kiểm thử)
- ✅ Viết testcase JUnit cho GameLogic.java.
- ✅ Test cường độ cao bằng cách mở hàng chục Client kết nối cùng lúc xem Server có bị sập hay không.

#### 5. Documentation
- ✅ Code comments bằng Javadoc chuẩn.
- ✅ README chuẩn bị cho báo cáo và demo.



