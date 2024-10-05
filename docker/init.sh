#!/bin/bash

# Create directories
mkdir -p master slave1 slave2

# Create master/my.cnf
cat <<EOL > master/my.cnf
[mysqld]
server-id=1
log-bin=mysql-bin
binlog-do-db=mydb
secure-file-priv=/var/lib/mysql-files
EOL

# Create slave1/my.cnf
cat <<EOL > slave1/my.cnf
[mysqld]
server-id=2
relay-log=relay-log
log-bin=mysql-bin
binlog-do-db=mydb
secure-file-priv=/var/lib/mysql-files
EOL

# Create slave2/my.cnf
cat <<EOL > slave2/my.cnf
[mysqld]
server-id=3
relay-log=relay-log
log-bin=mysql-bin
binlog-do-db=mydb
secure-file-priv=/var/lib/mysql-files
EOL

# Create master/redis.conf
cat <<EOL > master/redis.conf
bind 0.0.0.0
port 6379
requirepass bronya
EOL

# Create slave1/redis.conf
cat <<EOL > slave1/redis.conf
bind 0.0.0.0
port 6379
requirepass bronya
EOL

# Create slave2/redis.conf
cat <<EOL > slave2/redis.conf
bind 0.0.0.0
port 6379
requirepass bronya
EOL

# Ensure the directory exists and set permissions
mkdir -p /var/lib/mysql-files
chown mysql:mysql /var/lib/mysql-files

echo "开始执行docker-compose"

docker-compose down
docker-compose up -d