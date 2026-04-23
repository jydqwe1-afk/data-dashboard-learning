CREATE TABLE IF NOT EXISTS departments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    real_name TEXT,
    department_id INTEGER,
    role TEXT DEFAULT 'user',
    status INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS folders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    parent_id INTEGER,
    created_by INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES folders(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    code TEXT,
    parent_id INTEGER,
    description TEXT
);

CREATE TABLE IF NOT EXISTS security_levels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    level_order INTEGER
);

CREATE TABLE IF NOT EXISTS archives (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    category_id INTEGER,
    security_level_id INTEGER,
    folder_id INTEGER,
    department_id INTEGER,
    created_by INTEGER,
    status INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (security_level_id) REFERENCES security_levels(id),
    FOREIGN KEY (folder_id) REFERENCES folders(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS archive_files (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    archive_id INTEGER NOT NULL,
    file_name TEXT NOT NULL,
    file_path TEXT NOT NULL,
    file_type TEXT,
    file_size INTEGER,
    uploaded_by INTEGER,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (archive_id) REFERENCES archives(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS permissions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    archive_id INTEGER NOT NULL,
    permission_type TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (archive_id) REFERENCES archives(id)
);

CREATE TABLE IF NOT EXISTS operation_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    archive_id INTEGER,
    operation_type TEXT,
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

CREATE TABLE IF NOT EXISTS access_applications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    applicant_id INTEGER NOT NULL,
    archive_id INTEGER NOT NULL,
    purpose TEXT,
    status INTEGER DEFAULT 0,
    reviewer_id INTEGER,
    review_comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    reviewed_at DATETIME,
    FOREIGN KEY (applicant_id) REFERENCES users(id),
    FOREIGN KEY (archive_id) REFERENCES archives(id),
    FOREIGN KEY (reviewer_id) REFERENCES users(id)
);

-- 初始数据
INSERT OR IGNORE INTO departments (id, name, description) VALUES (1, '综合处', '负责公司综合行政事务');
INSERT OR IGNORE INTO departments (id, name, description) VALUES (2, '人事处', '负责公司人力资源管理');
INSERT OR IGNORE INTO departments (id, name, description) VALUES (3, '财务处', '负责公司财务管理');
INSERT OR IGNORE INTO departments (id, name, description) VALUES (4, '工程处', '负责水务工程项目管理');

INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (1, '公开', 1);
INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (2, '内部', 2);
INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (3, '秘密', 3);
INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (4, '机密', 4);

INSERT OR IGNORE INTO categories (id, name, code) VALUES (1, '行政文件', 'XZ');
INSERT OR IGNORE INTO categories (id, name, code) VALUES (2, '人事档案', 'RS');
INSERT OR IGNORE INTO categories (id, name, code) VALUES (3, '财务资料', 'CW');
INSERT OR IGNORE INTO categories (id, name, code) VALUES (4, '工程项目', 'GC');

-- 初始管理员账号 (密码: admin123)
INSERT OR IGNORE INTO users (id, username, password_hash, real_name, department_id, role, status)
VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 1, 'admin', 1);
