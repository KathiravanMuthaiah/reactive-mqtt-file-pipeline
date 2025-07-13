-- Create schema
CREATE SCHEMA IF NOT EXISTS fileproc;
SET search_path TO fileproc;
GRANT ALL ON SCHEMA fileproc TO logisticadmin;

-- 1️⃣ Table: line_data (written by Reader microservice using JPA)
DROP TABLE IF EXISTS line_data;
CREATE TABLE line_data (
    id SERIAL PRIMARY KEY,
    original_line TEXT NOT NULL,
    transformed_line TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- 2️⃣ Table: file_summary (written by Reader using raw JDBC after each file)
DROP TABLE IF EXISTS file_summary;
CREATE TABLE file_summary (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    total_lines INT NOT NULL,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3️⃣ Table: file_output (optional - written by Writer subscriber)
-- Only needed if you want to verify data was processed after MQTT receive
DROP TABLE IF EXISTS file_output;
CREATE TABLE file_output (
    id SERIAL PRIMARY KEY,
    mqtt_received_line TEXT NOT NULL,
    written_to_file TEXT,
    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

