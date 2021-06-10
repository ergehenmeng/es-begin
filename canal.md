```properties
[mysqld]
log-bin=mysql-bin # 开启binlog
binlog-format=ROW # 选择row模式
server_id=1 # 不要和canal的slaveId重复???
```

```sql
CREATE USER canal IDENTIFIED BY 'canal';  
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;
```