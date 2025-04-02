#!/bin/bash

# Khởi động MySQL
service mysql start

# Khởi động Keycloak (giả sử đã được cài đặt)
/opt/keycloak/bin/kc.sh start-dev

# Giữ container chạy
tail -f /dev/null 