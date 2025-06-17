# Backend API Project

Dự án này là một ứng dụng Spring Boot API của web với Swagger UI tích hợp để dễ dàng kiểm tra API.

## Các bước cài đặt và chạy ứng dụng

### Cài đặt các phụ thuộc

Đảm bảo rằng bạn đã cài đặt [Java](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html) và [Maven](https://maven.apache.org/install.html) trên máy của bạn.

Kiểm tra Java và Maven đã được cài đặt chưa bằng các lệnh sau:
```bash
java -version
mvn -version

Tải lại các gói phụ thuộc bằng Maven
mvn clean install

Nếu bạn không cài đặt Maven toàn cục trên máy tính, bạn có thể sử dụng Maven Wrapper
./mvnw clean install

Chạy ứng dụng
Chạy lệnh sau trong thư mục gốc của dự án:
./mvnw spring-boot:run

Sử dụng Maven (nếu đã cài đặt Maven)
mvn spring-boot:run

Truy cập: http://localhost:6868/public/api/hello
Swagger: http://localhost:6868/swagger-ui/index.html
Json: http://localhost:6868/v3/api-docs