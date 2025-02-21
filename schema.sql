-- Users Table
CREATE TABLE users (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) CHECK (role IN ('EMPLOYEE', 'IT_SUPPORT')) NOT NULL
);

-- Tickets Table
CREATE TABLE tickets (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR2(255) NOT NULL,
    description CLOB NOT NULL,
    priority VARCHAR2(10) CHECK (priority IN ('Low', 'Medium', 'High')) NOT NULL,
    category VARCHAR2(20) CHECK (category IN ('Network', 'Hardware', 'Software', 'Other')) NOT NULL,
    status VARCHAR2(20) CHECK (status IN ('New', 'In Progress', 'Resolved')) DEFAULT 'New' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id NUMBER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Audit Log Table
CREATE TABLE audit_logs (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id NUMBER NOT NULL,
    action VARCHAR2(50) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

-- Insert Default Admin User
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'IT_SUPPORT');
