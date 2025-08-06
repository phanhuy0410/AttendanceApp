CREATE DATABASE attendance_db;

CREATE TABLE attendance (
    mssv VARCHAR(10) NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    tenHP VARCHAR(50) NOT NULL,
    maHP VARCHAR(10) NOT NULL,
    nhom TINYINT NOT NULL,
    ngaygio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (mssv, maHP, ngaygio)
);

CREATE TABLE history (
    mssv VARCHAR(10) NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    tenHP VARCHAR(50) NOT NULL,
    maHP VARCHAR(10) NOT NULL,
    nhom TINYINT NOT NULL,
    ngaygio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TINYINT(1) DEFAULT 1,
    PRIMARY KEY (mssv, maHP, ngaygio)
);

CREATE TABLE login (
    maso VARCHAR(10) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE student (
    mssv VARCHAR(10) NOT NULL PRIMARY KEY,
    hoten VARCHAR(50) NOT NULL,
    lop VARCHAR(10) NOT NULL,
    email VARCHAR(50) NOT NULL
);

CREATE TABLE hocphan (
    maHP VARCHAR(10) PRIMARY KEY,       
    tenHP VARCHAR(50) NOT NULL,        
    namhoc VARCHAR(10) NOT NULL,      
    hocki TINYINT NOT NULL,            
    nhom TINYINT NOT NULL               
);

INSERT INTO student (mssv, hoten, lop, email) VALUES 
('B2204933', 'Phan Thanh Huy', 'DI22T9A1', 'huyb2204933@student.ctu.edu.vn'),
('B1234567', 'Nguyen Van An', 'DI22T9A1', 'anb1234567@student.ctu.edu.vn');

INSERT INTO login (maso, password) VALUES
('B2204933', '$2a$10$D5iT1eyqYPZJnG0ULlmhAuAAu7L3w/OSCZ5LO4k/mE6.vJ7h7LjSm'),
('B1234567', '$2a$10$ia087ALHGZX9oJ7.hs0oFumxnryJTolSz50DZ0fTx3eYIYlYHod4e'),
('000111', '$2a$10$P/y4QzfvkuWgJB0Ohru.UurjGhL2CxooJ9.QJhzHG.LxCbwLaU.SS');

INSERT INTO hocphan (maHP, tenHP, namhoc, hocki, nhom) VALUES 
('CT221', 'Lập trình mạng', '2024-2025', 1, 1),
('CT233', 'Điện toán đám mây', '2024-2025', 1, 1);

