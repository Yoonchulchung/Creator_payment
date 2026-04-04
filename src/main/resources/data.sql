-- 비밀번호 'password' BCrypt 인코딩 값
-- INSERT IGNORE: 중복 시 무시 (서버 재시작해도 안전)

-- ========== users ==========
INSERT IGNORE INTO users (id, username, password, role, created_at, updated_at, deleted_at) VALUES
(1,  'creator1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'CREATOR', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(2,  'creator2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'CREATOR', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(3,  'creator3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'CREATOR', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(4,  'student1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(5,  'student2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(6,  'student3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(7,  'student4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(8,  'student5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(9,  'student6', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(10, 'student7', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'STUDENT', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(11, 'admin',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S', 'ADMIN',   '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL);

-- ========== creator ==========
INSERT IGNORE INTO creator (id, user_id, name, created_at, updated_at, deleted_at) VALUES
(1, 1, '김강사', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(2, 2, '이강사', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(3, 3, '박강사', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL);

-- ========== student ==========
INSERT IGNORE INTO student (id, user_id, name, created_at, updated_at, deleted_at) VALUES
(1,  4,  '학생1', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(2,  5,  '학생2', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(3,  6,  '학생3', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(4,  7,  '학생4', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(5,  8,  '학생5', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(6,  9,  '학생6', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(7,  10, '학생7', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL);

-- ========== course ==========
INSERT IGNORE INTO course (id, title, created_at, updated_at, deleted_at) VALUES
(1, 'Spring Boot 입문', '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(2, 'JPA 실전',         '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(3, 'Kotlin 기초',      '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(4, 'MSA 설계',         '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL);

-- ========== creator_course ==========
INSERT IGNORE INTO creator_course (id, creator_id, course_id, created_at, updated_at, deleted_at) VALUES
(1, 1, 1, '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(2, 1, 2, '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(3, 2, 3, '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL),
(4, 3, 4, '2025-01-01 00:00:00', '2025-01-01 00:00:00', NULL);

-- ========== sale_record ==========
INSERT IGNORE INTO sale_record (id, sale_id, course_id, student_id, amount, paid_at, comment, created_at, updated_at, deleted_at) VALUES
(1, 'sale-1', 1, 1, 50000,  '2025-03-05 10:00:00', NULL, '2025-03-05 10:00:00', '2025-03-05 10:00:00', NULL),
(2, 'sale-2', 1, 2, 50000,  '2025-03-15 14:30:00', NULL, '2025-03-15 14:30:00', '2025-03-15 14:30:00', NULL),
(3, 'sale-3', 2, 3, 80000,  '2025-03-20 09:00:00', NULL, '2025-03-20 09:00:00', '2025-03-20 09:00:00', NULL),
(4, 'sale-4', 2, 4, 80000,  '2025-03-22 11:00:00', NULL, '2025-03-22 11:00:00', '2025-03-22 11:00:00', NULL),
(5, 'sale-5', 3, 5, 60000,  '2025-01-31 23:30:00', NULL, '2025-01-31 23:30:00', '2025-01-31 23:30:00', NULL),
(6, 'sale-6', 3, 6, 60000,  '2025-03-10 16:00:00', NULL, '2025-03-10 16:00:00', '2025-03-10 16:00:00', NULL),
(7, 'sale-7', 4, 7, 120000, '2025-02-14 10:00:00', NULL, '2025-02-14 10:00:00', '2025-02-14 10:00:00', NULL);
