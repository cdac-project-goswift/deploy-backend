-- System Configuration
INSERT INTO system_config (config_id, service_tax_pct, booking_fee)
VALUES (1, 10.00, 20.00)
ON DUPLICATE KEY UPDATE service_tax_pct=10.00, booking_fee=20.00;

-- Cities
INSERT INTO cities (city_name, state, country)
VALUES ('Pune', 'Maharashtra', 'India')
ON DUPLICATE KEY UPDATE city_name='Pune';

INSERT INTO cities (city_name, state, country)
VALUES ('Mumbai', 'Maharashtra', 'India')
ON DUPLICATE KEY UPDATE city_name='Mumbai';

INSERT INTO cities (city_name, state, country)
VALUES ('Bangalore', 'Karnataka', 'India')
ON DUPLICATE KEY UPDATE city_name='Bangalore';

INSERT INTO cities (city_name, state, country)
VALUES ('Nashik', 'Maharashtra', 'India')
ON DUPLICATE KEY UPDATE city_name='Nashik';

INSERT INTO cities (city_name, state, country)
VALUES ('Nagpur', 'Maharashtra', 'India')
ON DUPLICATE KEY UPDATE city_name='Nagpur';

-- Super Admin User
INSERT INTO users (first_name, last_name, email, phone_number, password, role, status)
VALUES ('Super', 'Admin', 'admin@goswift.com', '9876543210', '$2a$10$HmDTORYpDRF26FXz/VA2.OvRhxtL22XGYYFwrO.JYF2lScuJV6/F.', 'ROLE_ADMIN', 'ACTIVE')
ON DUPLICATE KEY UPDATE first_name='Super';
