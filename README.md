# Phân Công Công Việc – Dự Án Web Game Cờ Vây (Go)

Dự án được thực hiện bởi nhóm 6 thành viên. Mỗi người đảm nhận một mảng độc lập để đảm bảo tiến độ, tránh xung đột code và dễ dàng báo cáo khi bảo vệ.

## 1. FE 1 – UI/UX + Trang Auth
Phụ trách: Thiết kế và phát triển giao diện người dùng phần xác thực.

Nhiệm vụ chính:
Xây dựng trang Đăng nhập / Đăng ký bằng HTML/CSS/JS.
Kết nối API đăng nhập/đăng ký qua fetch.
Thiết kế giao diện cơ bản cho người dùng.
Hỗ trợ render giao diện danh sách người chơi.
---

## 2. FE 2 – Lobby + Room UI 
Phụ trách: Phát triển giao diện Lobby và phòng chơi.

Nhiệm vụ chính:
Thiết kế giao diện Lobby: danh sách phòng, nút tạo phòng, tham gia phòng.
Xây dựng khung giao diện phòng game (không xử lý logic).
Render chat UI.
Kết nối Socket.IO client để hiển thị danh sách phòng theo thời gian thực.

---

## 3. FE 3: Canvas Game + Ping Effect
Phụ trách: Hiển thị bàn cờ và hiệu ứng realtime phía frontend.

Nhiệm vụ chính:
Vẽ bàn cờ bằng HTML5 Canvas.
Vẽ quân đen / trắng, highlight vị trí, xử lý click của người chơi.
Render nước đi khi server gửi về.
Xử lý hiệu ứng Ping bằng Canvas.

---

## 4. FE/BE: WebRTC Ping + Socket Client
Phụ trách: Xử lý kết nối realtime peer-to-peer.

Nhiệm vụ chính:
Tạo kết nối WebRTC PeerConnection + DataChannel.
Gửi/nhận tín hiệu Ping qua DataChannel.
Hỗ trợ kết nối Socket.IO client với frontend.
Kiểm thử realtime communication.

---

## 5. BE 1: API Auth + Database + Static Server
Phụ trách: Xử lý backend đăng nhập và cơ sở dữ liệu.

Nhiệm vụ chính:
Tạo API đăng ký, đăng nhập bằng Express.js.
Hash mật khẩu (bcrypt).
Tạo và quản lý database SQLite.
Serve file frontend (HTML, CSS, JS).
Kiểm tra input, validate thông tin người dùng.
## 6. BE 2 + Full-stack Integration
Phụ trách: Xử lý realtime server và tích hợp toàn bộ hệ thống.

Nhiệm vụ chính:
Xây dựng server Socket.IO.
Quản lý lobby: tạo phòng, danh sách phòng, tham gia phòng.
Quản lý room: phân quyền người chơi, lượt chơi.
Tiếp nhận và broadcast nước đi.
Tiếp nhận và trả lời chat text.
Xử lý signaling WebRTC:
offer
answer
ICE candidates
Viết logic kiểm tra nước đi hợp lệ, đúng lượt.
Xác minh move từ client (chặn cheat).
Kết nối tất cả thành phần FE – BE.
Kiểm thử toàn bộ flow:
Login → Lobby → Tạo phòng → Chơi → Chat → Ping.
Sửa lỗi phát sinh giữa các phần.


---

## Lợi Ích Của Việc Chia Như Trên
- Không ai bị đụng code nhau.
- Nhiệm vụ rõ ràng, dễ chấm điểm cá nhân.
- Team làm việc khoa học, dễ quản lý tiến độ.
- Chia theo đúng kiến trúc thật của dự án real-time.

---

## Cấu Trúc Dự Án Đề Xuất
```
project/
│── backend/
│   ├── api/
│   ├── socket/
│   ├── database/
│   └── server.js
│
│── frontend/
│   ├── pages/
│   ├── styles/
│   ├── scripts/
│   └── canvas/
│
└── README.md
```

# Ngôn Ngữ & Công Nghệ Đề Xuất

Dự án nên được triển khai hoàn toàn bằng JavaScript/Node.js vì đây là lựa chọn tối ưu nhất cho real-time WebSocket, WebRTC và Canvas.

## Frontend
- **HTML5 + CSS3 + JavaScript**
- **Canvas API** để vẽ bàn cờ và hiển thị quân cờ.
- **WebRTC API** để tạo DataChannel cho chức năng Ping.
- **socket.io-client** để giao tiếp real-time với server.
- Có thể nâng cấp thêm:
  - **TypeScript** để hạn chế lỗi và code typed hơn.
  - **React** (không bắt buộc) nếu muốn UI reusable và chuyên nghiệp.

## Backend
- **Node.js**
- **Express.js** để cung cấp API và serve file tĩnh.
- **Socket.IO** để xử lý lobby, room và nước đi real-time.
- **SQLite** làm database nhẹ và phù hợp đồ án.
- **bcrypt** để hash mật khẩu.
- **WebRTC signaling** được implement trực tiếp qua Socket.IO.

## Vì sao chọn JavaScript + Node.js?
- FE bắt buộc dùng JS (Canvas + WebRTC API).
- Node.js đồng bộ ngon với WebSocket/WebRTC.
- Socket.IO mạnh nhất trên Node.js.
- Xử lý nhiều kết nối thời gian thực cực nhẹ nhờ event loop.
- FE & BE dùng chung 1 ngôn ngữ → dễ chia việc, dễ debug, dễ maintain.



---

# 🚀 HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY DỰ ÁN

## Yêu Cầu Hệ Thống

- **Node.js**: phiên bản 14.x trở lên
- **NPM**: phiên bản 6.x trở lên
- **Trình duyệt**: Chrome, Firefox, Edge (phiên bản mới nhất)
- **Hệ điều hành**: Windows, macOS, Linux

## Bước 1: Clone Dự Án

```bash
# Clone repository
git clone <repository-url>
cd Go-Socket
```

## Bước 2: Cài Đặt Dependencies

```bash
# Cài đặt các package cần thiết
npm install
```

### Các package được cài đặt:
- **express**: Web framework cho Node.js
- **socket.io**: Thư viện WebSocket cho real-time communication
- **sqlite3**: Database nhẹ
- **bcrypt**: Hash mật khẩu
- **jsonwebtoken**: Xác thực người dùng
- **cors**: Xử lý CORS
- **nodemon**: Auto-restart server khi dev (devDependencies)

## Bước 3: Khởi Động Server

### Development Mode (với auto-restart)
```bash
npm run dev
```

### Production Mode
```bash
npm start
```

Server sẽ chạy tại: **http://localhost:3000**

## Bước 4: Truy Cập Ứng Dụng

Mở trình duyệt và truy cập:

- **Trang đăng nhập**: http://localhost:3000
- **Trang đăng ký**: http://localhost:3000/register
- **Lobby**: http://localhost:3000/lobby (sau khi đăng nhập)
- **Game**: http://localhost:3000/game (tự động chuyển khi vào phòng)

## Bước 5: Kiểm Thử Hệ Thống

### Chạy test tự động
```bash
node backend/integration-test.js
```

### Kiểm thử thủ công
1. Mở 2 cửa sổ trình duyệt (hoặc 1 cửa sổ thường + 1 Incognito)
2. Đăng ký 2 tài khoản khác nhau
3. User 1: Tạo phòng
4. User 2: Tham gia phòng của User 1
5. Bắt đầu chơi!

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
- ✅ Mật khẩu được hash bằng **bcrypt** (10 rounds)
- ✅ Xác thực bằng **JWT token** (hết hạn sau 24h)
- ✅ Middleware kiểm tra token cho mọi kết nối Socket.IO

### Anti-Cheat (Chống gian lận)
- ✅ Kiểm tra người chơi có trong phòng không
- ✅ Kiểm tra đúng lượt chơi
- ✅ Validate tọa độ nước đi
- ✅ Kiểm tra nước đi theo luật cờ vây (Ko, Suicide, Capture)
- ✅ Ghi log mọi hành vi gian lận
- ✅ Server có quyền cuối cùng trong mọi quyết định

---

## 🐛 Troubleshooting

### Lỗi: "Cannot find module"
```bash
# Xóa node_modules và cài lại
rm -rf node_modules
npm install
```

### Lỗi: "Port 3000 is already in use"
```bash
# Đổi port trong backend/server.js
const PORT = process.env.PORT || 3001;
```

### Database bị lỗi
```bash
# Xóa database cũ và tạo lại
rm backend/database/database.db
npm start
```

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

#### 1. Game Logic (gameLogic.js)
- ✅ Thuật toán BFS tìm nhóm quân và đếm "khí"
- ✅ Validate nước đi (Ko, Suicide, Capture)
- ✅ Xử lý bắt quân
- ✅ Tính điểm lãnh thổ + Komi

#### 2. Anti-Cheat System (gameHandler.js)
- ✅ Server-side validation toàn diện
- ✅ Kiểm tra quyền và lượt chơi
- ✅ Ghi log hành vi gian lận
- ✅ Chặn mọi nước đi không hợp lệ

#### 3. Integration (Tích hợp FE-BE)
- ✅ Đồng bộ socket events
- ✅ Kết nối tất cả modules
- ✅ Fix bugs tích hợp
- ✅ Real-time sync

#### 4. Testing (Kiểm thử)
- ✅ Integration tests (backend/integration-test.js)
- ✅ Testing guide (TESTING_GUIDE.md)
- ✅ Test toàn bộ flow: Login → Lobby → Game
- ✅ Test anti-cheat và luật cờ

#### 5. Documentation
- ✅ README.md (hướng dẫn chạy dự án)
- ✅ TESTING_GUIDE.md (hướng dẫn kiểm thử)
- ✅ Code comments chi tiết
- ✅ Chuẩn bị nội dung báo cáo & demo

---

## 📞 Liên Hệ & Hỗ Trợ

Nếu gặp vấn đề:
1. Kiểm tra [TESTING_GUIDE.md](TESTING_GUIDE.md)
2. Xem console log (F12 → Console)
3. Kiểm tra server log
4. Liên hệ team

---

