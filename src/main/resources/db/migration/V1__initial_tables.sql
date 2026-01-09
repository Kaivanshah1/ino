-- Create organisation table
CREATE TABLE organization (
    id VARCHAR(255) PRIMARY KEY,
    name TEXT,
    phone_number VARCHAR(20),
    email VARCHAR(100),
    status VARCHAR(20),
    created_at BIGINT,
    updated_at BIGINT
);

-- Create users table
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    name TEXT,
    phone_number VARCHAR(20),
    email VARCHAR(100),
    organization_id VARCHAR(255),
    status VARCHAR(20),
    role TEXT,
    created_at BIGINT,
    updated_at BIGINT
);

-- Create user_auth table
CREATE TABLE user_auth (
    id VARCHAR(255) PRIMARY KEY,
    user_id TEXT NOT NULL,
    username TEXT NOT NULL,
    hash_password TEXT,
    created_at BIGINT,
    updated_at BIGINT
);

INSERT INTO organization (
    id,
    name,
    phone_number,
    email,
    status,
    created_at,
    updated_at
) VALUES (
    '37624caa-be22-4ce8-aa87-51af1a2e5cc4',
    'Ino Travels',
    '9876543210',
    'inotravel@yopmail.com',
    'ACTIVE',
    '1767939612350',
    '1767939612350'
);
