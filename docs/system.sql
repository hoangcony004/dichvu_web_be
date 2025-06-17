-- 1. Bảng lưu User
CREATE TABLE sys_user (
                          id SERIAL PRIMARY KEY,
                          fullname VARCHAR(100) NOT NULL,
                          username VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE,
                          phone VARCHAR(20) UNIQUE,
                          gender SMALLINT DEFAULT 0, -- 0: chưa xác định, 1: nam, 2: nữ, 3: khác
                          address TEXT,
                          avatar_url TEXT,
                          status SMALLINT DEFAULT 0, -- 1: hoạt động, 0: khóa, 2: chờ kích hoạt
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- 2. Bảng lưu Role
CREATE TABLE sys_role (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bảng trung gian User - Role
CREATE TABLE sys_user_role (
                               id SERIAL PRIMARY KEY,
                               user_id INT NOT NULL,
                               role_id INT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
                               FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE cascade
);

-- 4. Bảng lưu Menu
CREATE TABLE sys_menu (
                          id SERIAL PRIMARY KEY,
                          label VARCHAR(100) NOT NULL,
                          link VARCHAR(255),
                          icon VARCHAR(100),
                          parent_id INT NULL,
                          order_no INT DEFAULT 0,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (parent_id) REFERENCES sys_menu(id) ON DELETE SET NULL
);

-- 5. Bảng trung gian Role - Menu
CREATE TABLE sys_role_menu (
                               id SERIAL PRIMARY KEY,
                               role_id INT NOT NULL,
                               menu_id INT NOT NULL,
                               permission_type VARCHAR(50) DEFAULT 'view',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE,
                               FOREIGN KEY (menu_id) REFERENCES sys_menu(id) ON DELETE CASCADE
);

CREATE TABLE sys_log (
                         id SERIAL PRIMARY KEY,
                         user_id INT,
                         action VARCHAR(255) NOT NULL,
                         method VARCHAR(10),
                         url TEXT,
                         ip_address VARCHAR(50),
                         user_agent TEXT,
                         status_code INT,
                         message TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Thêm dấu phẩy phía trước để không bị lỗi
                         FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE SET NULL
);

CREATE TABLE sys_token (
                           id SERIAL PRIMARY KEY,
                           user_id INT NOT NULL,
                           token TEXT NOT NULL,
                           type VARCHAR(20) DEFAULT 'access',
                           is_revoked BOOLEAN DEFAULT FALSE,
                           expired_at TIMESTAMP,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Thêm dấu phẩy phía trước để không bị lỗi
                           FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
);

