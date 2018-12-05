CREATE TABLE "Author" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "Bar" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "Blog" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  "author" bigint(20) default NULL,
  PRIMARY KEY  ("id"),
  KEY "FK1FA3C24F2DD20D" ("author"),
  CONSTRAINT "FK1FA3C24F2DD20D" FOREIGN KEY ("author") REFERENCES "Author" ("id")
);
CREATE TABLE "Book" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "Foo" (
  "id" bigint(20) NOT NULL,
  "amount" decimal(19,2) default NULL,
  "date" datetime default NULL,
  "name" varchar(255) default NULL,
  "bar" bigint(20) default NULL,
  "F" varchar(255) default NULL,
  "I" char(1) default NULL,
  "L" varchar(255) default NULL,
  PRIMARY KEY  ("id"),
  KEY "FK114A61A27D6B" ("bar"),
  CONSTRAINT "FK114A61A27D6B" FOREIGN KEY ("bar") REFERENCES "Bar" ("id")
);
CREATE TABLE "FooInfo" (
  "foo" bigint(20) NOT NULL,
  "val" varchar(255) default NULL,
  "name" varchar(80) NOT NULL,
  PRIMARY KEY  ("foo","name"),
  KEY "FK3A9D2A741A29ED1" ("foo"),
  CONSTRAINT "FK3A9D2A741A29ED1" FOREIGN KEY ("foo") REFERENCES "Foo" ("id")
);
CREATE TABLE "MetricValue" (
  "F_ID" bigint(20) NOT NULL,
  "F_Frequence" varchar(255) default NULL,
  "F_Date" date default NULL,
  "F_TargetType" varchar(255) default NULL,
  "F_TargetId" varchar(255) default NULL,
  "F_MetricId" varchar(255) default NULL,
  "F_DimensionType" varchar(255) default NULL,
  "F_Dimension" varchar(255) default NULL,
  "F_QuotaValue" decimal(10,0) default NULL,
  "F_Type" varchar(255) default NULL,
  PRIMARY KEY  ("F_ID")
);
CREATE TABLE "OR_PROPERTIES" (
  "PROPERTY_ID" int(11) NOT NULL,
  "PROPERTY_KEY" varchar(255) NOT NULL,
  "PROPERTY_VALUE" varchar(255) default NULL,
  PRIMARY KEY  ("PROPERTY_ID"),
  UNIQUE KEY "PROPERTY_KEY" ("PROPERTY_KEY")
);
INSERT INTO "OR_PROPERTIES" ("PROPERTY_ID", "PROPERTY_KEY", "PROPERTY_VALUE") VALUES (1,'date.format','MM/dd/yyyy'),(2,'mail.auth.password',''),(3,'mail.auth.user',''),(4,'mail.smtp.auth','false'),(5,'mail.smtp.host','localhost'),(6,'role.admin','admin'),(7,'role.user','user'),(8,'temp.directory','/home/jxb/applications/jakarta-tomcat-5.0.28/webapps/openreports/reports/temp/'),(9,'queryreport.maxrows','1000'),(10,'report.generation.directory','/home/jxb/applications/jakarta-tomcat-5.0.28/webapps/openreports/reports/generated/'),(11,'base.directory','/home/jxb/applications/jakarta-tomcat-5.0.28/webapps/openreports/reports/');
CREATE TABLE "POINTACCOUNT" (
  "ID" bigint(20) NOT NULL,
  "CUSTOMERID" varchar(255) default NULL,
  "POINTTYPE" varchar(255) default NULL,
  "POINT" decimal(10,0) default NULL,
  PRIMARY KEY  ("ID")
);
INSERT INTO "POINTACCOUNT" ("ID", "CUSTOMERID", "POINTTYPE", "POINT") VALUES (1,'0000000001','POS','100'),(2,'0000000002','POS','300'),(3,'0000000003','POS','100');
CREATE TABLE "POINTCAL" (
  "ID" bigint(20) NOT NULL,
  "CUSTOMERID" varchar(255) default NULL,
  "TRANSTYPE" varchar(255) default NULL,
  "TRANSID" varchar(255) default NULL,
  "POINTTYPE" varchar(255) default NULL,
  "POINT" decimal(10,0) default NULL,
  "TRANSTIME" date default NULL,
  "MARK" int(11) default NULL,
  "PROMOTIONPLANID" varchar(255) default NULL,
  PRIMARY KEY  ("ID")
);
CREATE TABLE "POINTJOURNAL" (
  "ID" bigint(20) NOT NULL,
  "CUSTOMERID" varchar(255) default NULL,
  "TRANSTYPE" varchar(255) default NULL,
  "TRANSID" varchar(255) default NULL,
  "POINTTYPE" varchar(255) default NULL,
  "POINT" decimal(10,0) default NULL,
  "TRANSTIME" date default NULL,
  "PROMOTIONPLANID" varchar(255) default NULL,
  PRIMARY KEY  ("ID")
);
CREATE TABLE "POINTTYPE" (
  "ID" bigint(20) NOT NULL,
  "NAME" varchar(255) default NULL,
  "DESCRIPTION" varchar(255) default NULL,
  PRIMARY KEY  ("ID")
);
CREATE TABLE "PromotionRuleParams" (
  "PID" bigint(20) NOT NULL,
  "val" varchar(255) default NULL,
  "name" varchar(80) NOT NULL,
  PRIMARY KEY  ("PID","name"),
  KEY "FK8854C0852C537632" ("PID"),
  CONSTRAINT "FK8854C0852C537632" FOREIGN KEY ("PID") REFERENCES "TPromotionPlan" ("idno")
);
CREATE TABLE "QRTZ_BLOB_TRIGGERS" (
  "TRIGGER_NAME" varchar(80) NOT NULL,
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  "BLOB_DATA" blob,
  PRIMARY KEY  ("TRIGGER_NAME","TRIGGER_GROUP"),
  KEY "TRIGGER_NAME" ("TRIGGER_NAME","TRIGGER_GROUP"),
  CONSTRAINT "QRTZ_BLOB_TRIGGERS_ibfk_1" FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP") REFERENCES "QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP")
);
CREATE TABLE "QRTZ_CALENDARS" (
  "CALENDAR_NAME" varchar(80) NOT NULL,
  "CALENDAR" blob NOT NULL,
  PRIMARY KEY  ("CALENDAR_NAME")
);
CREATE TABLE "QRTZ_CRON_TRIGGERS" (
  "TRIGGER_NAME" varchar(80) NOT NULL,
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  "CRON_EXPRESSION" varchar(80) NOT NULL,
  "TIME_ZONE_ID" varchar(80) default NULL,
  PRIMARY KEY  ("TRIGGER_NAME","TRIGGER_GROUP"),
  KEY "TRIGGER_NAME" ("TRIGGER_NAME","TRIGGER_GROUP"),
  CONSTRAINT "QRTZ_CRON_TRIGGERS_ibfk_1" FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP") REFERENCES "QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP")
);
CREATE TABLE "QRTZ_FIRED_TRIGGERS" (
  "ENTRY_ID" varchar(95) NOT NULL,
  "TRIGGER_NAME" varchar(80) NOT NULL,
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  "IS_VOLATILE" varchar(1) NOT NULL,
  "INSTANCE_NAME" varchar(80) NOT NULL,
  "FIRED_TIME" bigint(13) NOT NULL,
  "STATE" varchar(16) NOT NULL,
  "JOB_NAME" varchar(80) default NULL,
  "JOB_GROUP" varchar(80) default NULL,
  "IS_STATEFUL" varchar(1) default NULL,
  "REQUESTS_RECOVERY" varchar(1) default NULL,
  PRIMARY KEY  ("ENTRY_ID")
);
CREATE TABLE "QRTZ_JOB_DETAILS" (
  "JOB_NAME" varchar(80) NOT NULL,
  "JOB_GROUP" varchar(80) NOT NULL,
  "DESCRIPTION" varchar(120) default NULL,
  "JOB_CLASS_NAME" varchar(128) NOT NULL,
  "IS_DURABLE" varchar(1) NOT NULL,
  "IS_VOLATILE" varchar(1) NOT NULL,
  "IS_STATEFUL" varchar(1) NOT NULL,
  "REQUESTS_RECOVERY" varchar(1) NOT NULL,
  "JOB_DATA" blob,
  PRIMARY KEY  ("JOB_NAME","JOB_GROUP")
);
CREATE TABLE "QRTZ_JOB_LISTENERS" (
  "JOB_NAME" varchar(80) NOT NULL,
  "JOB_GROUP" varchar(80) NOT NULL,
  "JOB_LISTENER" varchar(80) NOT NULL,
  PRIMARY KEY  ("JOB_NAME","JOB_GROUP","JOB_LISTENER"),
  KEY "JOB_NAME" ("JOB_NAME","JOB_GROUP"),
  CONSTRAINT "QRTZ_JOB_LISTENERS_ibfk_1" FOREIGN KEY ("JOB_NAME", "JOB_GROUP") REFERENCES "QRTZ_JOB_DETAILS" ("JOB_NAME", "JOB_GROUP")
);
CREATE TABLE "QRTZ_LOCKS" (
  "LOCK_NAME" varchar(40) NOT NULL,
  PRIMARY KEY  ("LOCK_NAME")
);
INSERT INTO "QRTZ_LOCKS" ("LOCK_NAME") VALUES ('CALENDAR_ACCESS'),('JOB_ACCESS'),('MISFIRE_ACCESS'),('STATE_ACCESS'),('TRIGGER_ACCESS');
CREATE TABLE "QRTZ_PAUSED_TRIGGER_GRPS" (
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  PRIMARY KEY  ("TRIGGER_GROUP")
);
CREATE TABLE "QRTZ_SCHEDULER_STATE" (
  "INSTANCE_NAME" varchar(80) NOT NULL,
  "LAST_CHECKIN_TIME" bigint(13) NOT NULL,
  "CHECKIN_INTERVAL" bigint(13) NOT NULL,
  "RECOVERER" varchar(80) default NULL,
  PRIMARY KEY  ("INSTANCE_NAME")
);
CREATE TABLE "QRTZ_SIMPLE_TRIGGERS" (
  "TRIGGER_NAME" varchar(80) NOT NULL,
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  "REPEAT_COUNT" bigint(7) NOT NULL,
  "REPEAT_INTERVAL" bigint(12) NOT NULL,
  "TIMES_TRIGGERED" bigint(7) NOT NULL,
  PRIMARY KEY  ("TRIGGER_NAME","TRIGGER_GROUP"),
  KEY "TRIGGER_NAME" ("TRIGGER_NAME","TRIGGER_GROUP"),
  CONSTRAINT "QRTZ_SIMPLE_TRIGGERS_ibfk_1" FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP") REFERENCES "QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP")
);
CREATE TABLE "QRTZ_TRIGGERS" (
  "TRIGGER_NAME" varchar(80) NOT NULL,
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  "JOB_NAME" varchar(80) NOT NULL,
  "JOB_GROUP" varchar(80) NOT NULL,
  "IS_VOLATILE" varchar(1) NOT NULL,
  "DESCRIPTION" varchar(120) default NULL,
  "NEXT_FIRE_TIME" bigint(13) default NULL,
  "PREV_FIRE_TIME" bigint(13) default NULL,
  "TRIGGER_STATE" varchar(16) NOT NULL,
  "TRIGGER_TYPE" varchar(8) NOT NULL,
  "START_TIME" bigint(13) NOT NULL,
  "END_TIME" bigint(13) default NULL,
  "CALENDAR_NAME" varchar(80) default NULL,
  "MISFIRE_INSTR" smallint(2) default NULL,
  "JOB_DATA" blob,
  PRIMARY KEY  ("TRIGGER_NAME","TRIGGER_GROUP"),
  KEY "JOB_NAME" ("JOB_NAME","JOB_GROUP"),
  CONSTRAINT "QRTZ_TRIGGERS_ibfk_1" FOREIGN KEY ("JOB_NAME", "JOB_GROUP") REFERENCES "QRTZ_JOB_DETAILS" ("JOB_NAME", "JOB_GROUP")
);
CREATE TABLE "QRTZ_TRIGGER_LISTENERS" (
  "TRIGGER_NAME" varchar(80) NOT NULL,
  "TRIGGER_GROUP" varchar(80) NOT NULL,
  "TRIGGER_LISTENER" varchar(80) NOT NULL,
  PRIMARY KEY  ("TRIGGER_NAME","TRIGGER_GROUP","TRIGGER_LISTENER"),
  KEY "TRIGGER_NAME" ("TRIGGER_NAME","TRIGGER_GROUP"),
  CONSTRAINT "QRTZ_TRIGGER_LISTENERS_ibfk_1" FOREIGN KEY ("TRIGGER_NAME", "TRIGGER_GROUP") REFERENCES "QRTZ_TRIGGERS" ("TRIGGER_NAME", "TRIGGER_GROUP")
);
CREATE TABLE "REPORT" (
  "REPORT_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "DESCRIPTION" varchar(255) NOT NULL,
  "REPORT_FILE" varchar(255) NOT NULL,
  "PDF_EXPORT" bit(1) NOT NULL,
  "CSV_EXPORT" bit(1) NOT NULL,
  "XLS_EXPORT" bit(1) NOT NULL,
  "HTML_EXPORT" bit(1) NOT NULL,
  "CHART_ID" int(11) default NULL,
  "REPORT_QUERY" text,
  "DATASOURCE_ID" int(11) default NULL,
  "FILL_VIRTUAL" bit(1) NOT NULL,
  "EXCEL_EXPORT" bit(1) NOT NULL,
  "TEXT_EXPORT" bit(1) NOT NULL,
  "RTF_EXPORT" bit(1) NOT NULL,
  "EXPORT_OPTION_ID" int(11) default NULL,
  "HIDDEN_REPORT" bit(1) NOT NULL,
  PRIMARY KEY  ("REPORT_ID"),
  UNIQUE KEY "NAME" ("NAME"),
  UNIQUE KEY "NAME_2" ("NAME"),
  KEY "FK8FDF49343A53434B" ("DATASOURCE_ID"),
  KEY "FK8FDF4934AD1AEEF4" ("EXPORT_OPTION_ID"),
  KEY "FK8FDF4934FBBB3EC9" ("CHART_ID")
);
INSERT INTO "REPORT" ("REPORT_ID", "NAME", "DESCRIPTION", "REPORT_FILE", "PDF_EXPORT", "CSV_EXPORT", "XLS_EXPORT", "HTML_EXPORT", "CHART_ID", "REPORT_QUERY", "DATASOURCE_ID", "FILL_VIRTUAL", "EXCEL_EXPORT", "TEXT_EXPORT", "RTF_EXPORT", "EXPORT_OPTION_ID", "HIDDEN_REPORT") VALUES (66,'客户信息查询','客户信息查询','','\0','\0','\0','',NULL,'select * from knc_cust c inner join knc_prsn p on c.custno=p.custno',NULL,'\0','\0','\0','\0',53,'\0'),(67,'客户积分查询','客户积分查询','','\0','\0','\0','',NULL,'select c.custno 客户号,  c.custna 客户名称, p.sextp 性别, a.POINT 积分\r\nfrom knc_cust c inner join knc_prsn p \r\non c.custno=p.custno inner join \r\n(select CUSTOMERID, sum(POINT)  POINT from POINTACCOUNT group by (CUSTOMERID)) a\r\non c.custno = a.CUSTOMERID',NULL,'\0','\0','\0','\0',54,'\0'),(68,'客户性别统计','客户性别统计','','\0','\0','\0','',13,'',NULL,'\0','\0','\0','\0',55,'\0');
CREATE TABLE "REPORT_ALERT" (
  "ALERT_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "DESCRIPTION" varchar(255) NOT NULL,
  "ALERT_QUERY" text NOT NULL,
  "DATASOURCE_ID" int(11) default NULL,
  PRIMARY KEY  ("ALERT_ID"),
  UNIQUE KEY "NAME" ("NAME"),
  KEY "FKF81C86713A53434B" ("DATASOURCE_ID"),
  CONSTRAINT "FKF81C86713A53434B" FOREIGN KEY ("DATASOURCE_ID") REFERENCES "REPORT_DATASOURCE" ("DATASOURCE_ID")
);
CREATE TABLE "REPORT_CHART" (
  "CHART_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "DESCRIPTION" varchar(255) NOT NULL,
  "CHART_QUERY" text NOT NULL,
  "CHART_TYPE" int(11) NOT NULL,
  "WIDTH" int(11) NOT NULL,
  "HEIGHT" int(11) NOT NULL,
  "X_AXIS_LABEL" varchar(255) default NULL,
  "Y_AXIS_LABEL" varchar(255) default NULL,
  "DATASOURCE_ID" int(11) default NULL,
  "SHOW_LEGEND" bit(1) NOT NULL,
  "SHOW_TITLE" bit(1) NOT NULL,
  "PLOT_ORIENTATION" int(11) default NULL,
  "REPORT_ID" int(11) default NULL,
  "SHOW_VALUES" bit(1) NOT NULL,
  PRIMARY KEY  ("CHART_ID"),
  UNIQUE KEY "NAME" ("NAME"),
  UNIQUE KEY "NAME_2" ("NAME"),
  KEY "FKF836D4F33A53434B" ("DATASOURCE_ID"),
  KEY "FKF836D4F3E08BB4B7" ("REPORT_ID")
);
INSERT INTO "REPORT_CHART" ("CHART_ID", "NAME", "DESCRIPTION", "CHART_QUERY", "CHART_TYPE", "WIDTH", "HEIGHT", "X_AXIS_LABEL", "Y_AXIS_LABEL", "DATASOURCE_ID", "SHOW_LEGEND", "SHOW_TITLE", "PLOT_ORIENTATION", "REPORT_ID", "SHOW_VALUES") VALUES (4,'Orders By Year and Month','XY Chart - Orders By Year and Month','select year_id, month_id,sum(quantityordered)\r\n from orderfact, time\r\nwhere orderfact.time_id = time.time_id\r\nand orderfact.orderdate > \'2002-12-31\'\r\nand orderfact.orderdate < \'2005-01-01\'\r\ngroup by year_id, month_id',2,600,400,'Month','Quantity Ordered',NULL,'','',2,NULL,'\0'),(5,'Orders By Date','Time Chart - Orders By Date','select \'Orders\', sum(QUANTITYORDERED),orderdate \r\n from orderfact\r\nwhere orderfact.orderdate > \'2003-11-03\' \r\nand orderfact.orderdate < \'2003-11-11\'\r\ngroup by orderdate\r\n\r\n',3,600,400,'Order Date','Order Quantity',NULL,'\0','\0',2,NULL,'\0'),(9,'Orders By Product Line','Pie Chart - Orders By Product Line','select count(*), productline from orderdetails, orders, products where \r\norders.ordernumber = orderdetails.ordernumber\r\nand \r\norderdetails.productcode = products.productcode and orders.customernumber = $P{CustomerNumber}\r\ngroup by productline',1,600,400,'','',NULL,'','',1,NULL,'\0'),(10,'Product Break Down By Line','Bar Chart - Product Break Down By Line','select count(*), \'\', productline\r\nfrom products \r\ngroup by productline\r\norder by productline',0,600,400,'Product Name','Product Count',NULL,'\0','\0',1,NULL,''),(11,'Product Line Inventory','Ring Chart - Product Line Inventory','select sum(quantityinstock), productline \r\nfrom products \r\ngroup by productline\r\norder by productline',4,600,400,'','',NULL,'\0','',1,NULL,''),(12,'指标类型分布饼图','指标类型分布饼图','select count(*), type from TMetric group by type',1,600,400,'类型','数量',NULL,'\0','\0',0,NULL,'\0'),(13,'客户性别分布图-饼图','客户性别分布图-饼图','select count(*), sextp from knc_prsn group by sextp',1,600,400,'性别','数量',NULL,'','',1,NULL,''),(14,'客户性别分布图-柱状图','客户性别分布图-柱状图','select count(*), \'\', sextp from knc_prsn group by sextp',0,600,400,'性别','数量',NULL,'','',2,NULL,''),(15,'客户性别分布图-Ring','客户性别分布图-Ring','select count(*), sextp from knc_prsn group by sextp',4,600,400,'性别','数量',NULL,'','',1,NULL,'');
CREATE TABLE "REPORT_DATASOURCE" (
  "DATASOURCE_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "DRIVER" varchar(255) default NULL,
  "URL" varchar(255) NOT NULL,
  "USERNAME" varchar(255) default NULL,
  "PASSWORD" varchar(255) default NULL,
  "MAX_IDLE" int(11) default NULL,
  "MAX_ACTIVE" int(11) default NULL,
  "MAX_WAIT" bigint(20) default NULL,
  "VALIDATION_QUERY" varchar(255) default NULL,
  "JNDI" bit(1) default NULL,
  PRIMARY KEY  ("DATASOURCE_ID"),
  UNIQUE KEY "NAME" ("NAME")
);
INSERT INTO "REPORT_DATASOURCE" ("DATASOURCE_ID", "NAME", "DRIVER", "URL", "USERNAME", "PASSWORD", "MAX_IDLE", "MAX_ACTIVE", "MAX_WAIT", "VALIDATION_QUERY", "JNDI") VALUES (2,'OpenReports Sample Data','org.hsqldb.jdbcDriver','jdbc:hsqldb:hsql://127.0.0.1:9001/sample','sa','',2,20,1800,NULL,'\0'),(3,'OpenReports','org.hsqldb.jdbcDriver','jdbc:hsqldb:hsql://127.0.0.1:9001/openreports','sa','',2,20,1800,NULL,'\0');
CREATE TABLE "REPORT_EXPORT_OPTIONS" (
  "EXPORT_OPTION_ID" int(11) NOT NULL,
  "XLS_REMOVE_EMPTY_SPACE" bit(1) NOT NULL,
  "XLS_ONE_PAGE_PER_SHEET" bit(1) NOT NULL,
  "XLS_AUTO_DETECT_CELL" bit(1) NOT NULL,
  "XLS_WHITE_BACKGROUND" bit(1) NOT NULL,
  "HTML_REMOVE_EMPTY_SPACE" bit(1) NOT NULL,
  "HTML_WHITE_BACKGROUND" bit(1) NOT NULL,
  "HTML_USE_IMAGES" bit(1) NOT NULL,
  "HTML_WRAP_BREAK" bit(1) NOT NULL,
  PRIMARY KEY  ("EXPORT_OPTION_ID")
);
INSERT INTO "REPORT_EXPORT_OPTIONS" ("EXPORT_OPTION_ID", "XLS_REMOVE_EMPTY_SPACE", "XLS_ONE_PAGE_PER_SHEET", "XLS_AUTO_DETECT_CELL", "XLS_WHITE_BACKGROUND", "HTML_REMOVE_EMPTY_SPACE", "HTML_WHITE_BACKGROUND", "HTML_USE_IMAGES", "HTML_WRAP_BREAK") VALUES (16,'\0','\0','\0','\0','\0','\0','\0','\0'),(17,'\0','\0','\0','\0','\0','\0','\0','\0'),(19,'\0','\0','\0','\0','\0','\0','\0','\0'),(23,'\0','\0','\0','\0','\0','\0','\0','\0'),(24,'\0','\0','\0','\0','\0','\0','\0','\0'),(25,'\0','\0','\0','\0','\0','\0','\0','\0'),(26,'\0','\0','\0','\0','\0','\0','\0','\0'),(27,'\0','\0','\0','\0','\0','\0','\0','\0'),(28,'\0','\0','\0','\0','\0','\0','\0','\0'),(32,'\0','\0','\0','\0','\0','\0','\0','\0'),(33,'\0','\0','\0','\0','\0','\0','\0','\0'),(34,'\0','\0','\0','\0','\0','\0','\0','\0'),(35,'\0','\0','\0','\0','\0','\0','\0','\0'),(36,'\0','\0','\0','\0','\0','\0','','\0'),(39,'\0','\0','\0','\0','\0','\0','\0','\0'),(40,'\0','\0','\0','\0','\0','\0','\0','\0'),(41,'\0','\0','\0','\0','\0','\0','\0','\0'),(42,'\0','\0','\0','\0','\0','\0','\0','\0'),(43,'\0','\0','\0','\0','\0','\0','\0','\0'),(44,'\0','\0','\0','\0','\0','\0','\0','\0'),(45,'\0','\0','\0','\0','\0','\0','\0','\0'),(46,'\0','\0','\0','\0','\0','\0','\0','\0'),(47,'\0','\0','\0','\0','\0','\0','\0','\0'),(49,'\0','\0','\0','\0','\0','\0','\0','\0'),(50,'\0','\0','\0','\0','\0','\0','\0','\0'),(51,'\0','\0','\0','\0','\0','\0','\0','\0'),(52,'\0','\0','\0','\0','\0','\0','\0','\0'),(53,'\0','\0','\0','\0','\0','\0','\0','\0'),(54,'\0','\0','\0','\0','\0','\0','\0','\0'),(55,'\0','\0','\0','\0','\0','\0','\0','\0');
CREATE TABLE "REPORT_GROUP" (
  "GROUP_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "DESCRIPTION" varchar(255) NOT NULL,
  PRIMARY KEY  ("GROUP_ID"),
  UNIQUE KEY "NAME" ("NAME")
);
CREATE TABLE "REPORT_GROUP_MAP" (
  "GROUP_ID" int(11) NOT NULL,
  "REPORT_ID" int(11) NOT NULL,
  "MAP_ID" int(11) NOT NULL,
  PRIMARY KEY  ("GROUP_ID","MAP_ID"),
  KEY "FKEF946211C487AF29" ("GROUP_ID"),
  CONSTRAINT "FKEF946211C487AF29" FOREIGN KEY ("GROUP_ID") REFERENCES "REPORT_GROUP" ("GROUP_ID")
);
CREATE TABLE "REPORT_LOG" (
  "LOG_ID" int(11) NOT NULL,
  "START_TIME" datetime default NULL,
  "END_TIME" datetime default NULL,
  "STATUS" varchar(255) default NULL,
  "MESSAGE" text,
  "REPORT_ID" int(11) default NULL,
  "USER_ID" int(11) default NULL,
  "ALERT_ID" int(11) default NULL,
  PRIMARY KEY  ("LOG_ID"),
  KEY "FK901BE599CB12A22B" ("USER_ID"),
  KEY "FK901BE59964AE609" ("ALERT_ID"),
  CONSTRAINT "FK901BE59964AE609" FOREIGN KEY ("ALERT_ID") REFERENCES "REPORT_ALERT" ("ALERT_ID"),
  CONSTRAINT "FK901BE599CB12A22B" FOREIGN KEY ("USER_ID") REFERENCES "REPORT_USER" ("REPORTUSER_ID")
);
INSERT INTO "REPORT_LOG" ("LOG_ID", "START_TIME", "END_TIME", "STATUS", "MESSAGE", "REPORT_ID", "USER_ID", "ALERT_ID") VALUES (187,'2007-05-23 11:49:55','2007-05-23 11:49:55','success',NULL,66,1,NULL),(188,'2007-05-23 11:56:26','2007-05-23 11:56:26','success',NULL,67,1,NULL),(189,'2007-05-23 11:58:58','2007-05-23 11:58:58','success',NULL,66,1,NULL),(190,'2007-05-23 11:59:03','2007-05-23 11:59:03','success',NULL,67,1,NULL),(191,'2007-05-23 12:08:30','2007-05-23 12:08:30','success',NULL,66,1,NULL),(192,'2007-05-23 12:08:41','2007-05-23 12:08:41','success',NULL,67,1,NULL),(193,'2007-05-23 12:17:04','2007-05-23 12:17:04','success',NULL,66,1,NULL),(194,'2007-05-23 12:18:26','2007-05-23 12:18:26','success',NULL,67,1,NULL),(195,'2007-05-23 17:22:32','2007-05-23 17:22:34','success',NULL,67,1,NULL),(196,'2007-05-23 17:34:13','2007-05-23 17:34:14','failure','Invalid Chart Query: com.mysql.jdbc.exceptions.MySQLSyntaxErrorException: Mixing of GROUP columns (MIN(),MAX(),COUNT(),...) with no GROUP columns is illegal if there is no GROUP BY clause',68,1,NULL),(197,'2007-05-23 17:42:17','2007-05-23 17:42:21','success',NULL,68,1,NULL),(198,'2007-05-23 17:45:28','2007-05-23 17:45:29','success',NULL,68,1,NULL),(199,'2007-05-23 17:46:42','2007-05-23 17:46:42','success',NULL,68,1,NULL),(200,'2007-05-23 17:47:58','2007-05-23 17:47:58','failure','Invalid Chart Query: java.sql.SQLException: Column Index out of range, 3 > 2. ',68,1,NULL),(201,'2007-05-23 17:57:43','2007-05-23 17:57:45','success',NULL,68,1,NULL);
CREATE TABLE "REPORT_PARAMETER" (
  "PARAMETER_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "TYPE" varchar(255) NOT NULL,
  "CLASSNAME" varchar(255) NOT NULL,
  "DATA" text,
  "DATASOURCE_ID" int(11) default NULL,
  "DESCRIPTION" varchar(255) default NULL,
  "REQUIRED" bit(1) default NULL,
  "MULTI_SELECT" bit(1) default NULL,
  PRIMARY KEY  ("PARAMETER_ID"),
  UNIQUE KEY "NAME" ("NAME"),
  KEY "FKBC64163E3A53434B" ("DATASOURCE_ID"),
  CONSTRAINT "FKBC64163E3A53434B" FOREIGN KEY ("DATASOURCE_ID") REFERENCES "REPORT_DATASOURCE" ("DATASOURCE_ID")
);
CREATE TABLE "REPORT_PARAMETER_MAP" (
  "REPORT_ID" int(11) NOT NULL,
  "PARAMETER_ID" int(11) default NULL,
  "REQUIRED" bit(1) default NULL,
  "SORT_ORDER" int(11) default NULL,
  "STEP" int(11) default NULL,
  "MAP_ID" int(11) NOT NULL,
  PRIMARY KEY  ("REPORT_ID","MAP_ID"),
  KEY "FK23FF1FBB5CC22769" ("PARAMETER_ID"),
  CONSTRAINT "FK23FF1FBB5CC22769" FOREIGN KEY ("PARAMETER_ID") REFERENCES "REPORT_PARAMETER" ("PARAMETER_ID")
);
CREATE TABLE "REPORT_USER" (
  "REPORTUSER_ID" int(11) NOT NULL,
  "NAME" varchar(255) NOT NULL,
  "PASSWORD" varchar(255) NOT NULL,
  "EXTERNAL_ID" varchar(255) default NULL,
  "EMAIL_ADDRESS" varchar(255) default NULL,
  "PDF_EXPORT_TYPE" int(11) NOT NULL,
  "DEFAULT_REPORT_ID" int(11) default NULL,
  PRIMARY KEY  ("REPORTUSER_ID"),
  UNIQUE KEY "NAME" ("NAME")
);
INSERT INTO "REPORT_USER" ("REPORTUSER_ID", "NAME", "PASSWORD", "EXTERNAL_ID", "EMAIL_ADDRESS", "PDF_EXPORT_TYPE", "DEFAULT_REPORT_ID") VALUES (1,'admin','admin','1','admin@testdomain.com',0,42),(5,'user','user','3','user@testdomain.com',0,NULL);
CREATE TABLE "RolePermissions" (
  "RoleId" bigint(20) NOT NULL,
  "Permission" varchar(255) default NULL,
  KEY "FK6CC0E0EB1E777EC" ("RoleId"),
  CONSTRAINT "FK6CC0E0EB1E777EC" FOREIGN KEY ("RoleId") REFERENCES "TRole" ("idno")
);
CREATE TABLE "SMSBONUS" (
  "ID" bigint(20) NOT NULL,
  "CUSTOMERID" varchar(255) default NULL,
  "CONTENT" varchar(255) default NULL,
  "PROMOTIONPLANID" varchar(255) default NULL,
  PRIMARY KEY  ("ID")
);
CREATE TABLE "SMSBONUSCAL" (
  "ID" bigint(20) NOT NULL,
  "CUSTOMERID" varchar(255) default NULL,
  "CONTENT" varchar(255) default NULL,
  "PROMOTIONPLANID" varchar(255) default NULL,
  PRIMARY KEY  ("ID")
);
CREATE TABLE "TASource" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  "atype" varchar(255) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "TBSource" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  "btype" varchar(255) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "TCSource" (
  "id" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  "ctype" varchar(255) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "TCustomerGroup" (
  "idno" bigint(20) NOT NULL,
  "creationDate" datetime default NULL,
  "name" varchar(255) default NULL,
  "description" varchar(255) default NULL,
  "rule" varchar(255) default NULL,
  "creator" varchar(255) default NULL,
  "evaluation" bigint(20) default NULL,
  "parent" bigint(20) default NULL,
  PRIMARY KEY  ("idno"),
  KEY "FKCB4D194D508DE1D" ("evaluation"),
  KEY "FKCB4D194D45469C26" ("parent"),
  CONSTRAINT "FKCB4D194D45469C26" FOREIGN KEY ("parent") REFERENCES "TCustomerGroup" ("idno"),
  CONSTRAINT "FKCB4D194D508DE1D" FOREIGN KEY ("evaluation") REFERENCES "TEvaluation" ("idno")
);
INSERT INTO "TCustomerGroup" ("idno", "creationDate", "name", "description", "rule", "creator", "evaluation", "parent") VALUES (1,'2007-05-23 00:00:00','女性客户','','客户.性别==\'女\'','2',1,3),(2,'2007-05-23 00:00:00','个人网银客户','','客户.网银开通标志==\'真\'','2',2,3),(3,'2007-05-23 00:00:00','个人客户','','客户.客户类型==\'个人\' ','2',1,NULL),(4,'2007-05-23 00:00:00','企业客户','','客户.客户类型==\'企业\' ','2',2,NULL),(5,'2007-05-23 00:00:00','企业网银客户','','客户.网银开通标志==\'真\'','2',2,4);
CREATE TABLE "TEvalMetrics" (
  "eid" bigint(20) NOT NULL,
  "mid" bigint(20) NOT NULL,
  KEY "FKDEE6EB93DCB4061D" ("mid"),
  KEY "FKDEE6EB93D1DE5F21" ("eid"),
  CONSTRAINT "FKDEE6EB93D1DE5F21" FOREIGN KEY ("eid") REFERENCES "TEvaluation" ("idno"),
  CONSTRAINT "FKDEE6EB93DCB4061D" FOREIGN KEY ("mid") REFERENCES "TMetric" ("idno")
);
INSERT INTO "TEvalMetrics" ("eid", "mid") VALUES (1,1),(1,2),(1,4),(1,5),(2,1),(2,4),(2,5);
CREATE TABLE "TEvaluation" (
  "idno" bigint(20) NOT NULL,
  "creationDate" datetime default NULL,
  "creator" varchar(255) default NULL,
  "description" varchar(255) default NULL,
  "formula" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  "type" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
INSERT INTO "TEvaluation" ("idno", "creationDate", "creator", "description", "formula", "name", "type") VALUES (1,'2007-05-23 00:00:00','2','','客户人数,POS消费额,POS消费笔数,网银交易金额,网银交易笔数','客户消费指标评价',NULL),(2,'2007-05-23 00:00:00','2','','客户人数,网银交易金额,网银交易笔数','客户网银交易指标评价',NULL);
CREATE TABLE "TGift" (
  "idno" bigint(20) NOT NULL,
  "availableQuantity" int(11) default NULL,
  "costPrice" decimal(19,2) default NULL,
  "description" varchar(255) default NULL,
  "exchangeRule" varchar(255) default NULL,
  "marketPrice" decimal(19,2) default NULL,
  "name" varchar(255) default NULL,
  "seqno" varchar(255) default NULL,
  "image" longblob,
  "imageFileName" varchar(255) default NULL,
  PRIMARY KEY  ("idno"),
  UNIQUE KEY "seqno" ("seqno")
);
CREATE TABLE "TLog" (
  "id" bigint(20) NOT NULL,
  "log" varchar(255) default NULL,
  "tablename" varchar(255) default NULL,
  "sourceid" bigint(20) default NULL,
  PRIMARY KEY  ("id")
);
CREATE TABLE "TMetric" (
  "idno" bigint(20) NOT NULL,
  "description" varchar(255) default NULL,
  "formula" varchar(255) default NULL,
  "type" varchar(255) default NULL,
  "frequence" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  "ratio" varchar(255) default NULL,
  "unit" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
INSERT INTO "TMetric" ("idno", "description", "formula", "type", "frequence", "name", "ratio", "unit") VALUES (1,'','count','','D','客户人数','',''),(2,'','sum(交易.金额) where 交易.交易类型==\'POS\'','','D','POS消费额','',''),(3,'','count where 交易.交易类型==\'POS\'','','D','POS交易笔数','',''),(4,'','sum(交易.金额) where 交易.交易类型==\'网银交易\'','','D','网银交易金额','',''),(5,'','count where 交易.交易类型==\'网银交易\'','','D','网银交易笔数','',''),(8,'','count where 客户.网银开通标志==\'真\'','','D','网银开户数','',''),(9,'','count where 客户.网银交易金额>0','','D','网银有效开户数','','');
CREATE TABLE "TPointType" (
  "idno" bigint(20) NOT NULL,
  "description" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
CREATE TABLE "TPromotionPlan" (
  "idno" bigint(20) NOT NULL,
  "creationDate" datetime default NULL,
  "creator" varchar(255) default NULL,
  "description" varchar(255) default NULL,
  "endDate" datetime default NULL,
  "evaluation" bigint(20) default NULL,
  "name" varchar(255) default NULL,
  "rule" bigint(20) default NULL,
  "startDate" datetime default NULL,
  "status" varchar(255) default NULL,
  "target" bigint(20) NOT NULL,
  PRIMARY KEY  ("idno"),
  KEY "FK77034A78508DE1D" ("evaluation"),
  KEY "FK77034A784C1A050D" ("target"),
  KEY "FK77034A7858B8A0A0" ("rule"),
  CONSTRAINT "FK77034A784C1A050D" FOREIGN KEY ("target") REFERENCES "TCustomerGroup" ("idno"),
  CONSTRAINT "FK77034A78508DE1D" FOREIGN KEY ("evaluation") REFERENCES "TEvaluation" ("idno"),
  CONSTRAINT "FK77034A7858B8A0A0" FOREIGN KEY ("rule") REFERENCES "TRules" ("idno")
);
INSERT INTO "TPromotionPlan" ("idno", "creationDate", "creator", "description", "endDate", "evaluation", "name", "rule", "startDate", "status", "target") VALUES (1,'2007-05-23 00:00:00','2','','2007-05-31 00:00:00',2,'网银常规营销计划',2,'2007-05-01 00:00:00','1',2);
CREATE TABLE "TReportDefinition" (
  "idno" bigint(20) NOT NULL,
  "dataSource" bigint(20) default NULL,
  "description" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  "query" varchar(255) default NULL,
  "type" varchar(255) default NULL,
  "template" bigint(20) default NULL,
  PRIMARY KEY  ("idno"),
  KEY "FK86F6921B2C71B5CC" ("template"),
  CONSTRAINT "FK86F6921B2C71B5CC" FOREIGN KEY ("template") REFERENCES "TReportTemplate" ("idno")
);
CREATE TABLE "TReportParameter" (
  "idno" bigint(20) NOT NULL,
  "value" varchar(255) default NULL,
  "cname" varchar(255) default NULL,
  "converter" varchar(255) default NULL,
  "len" int(11) default NULL,
  "maxlen" int(11) default NULL,
  "maxvalue" varchar(255) default NULL,
  "minlen" int(11) default NULL,
  "minvalue" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  "req" bit(1) default NULL,
  "type" varchar(255) default NULL,
  "action" varchar(255) default NULL,
  "jspattern" varchar(255) default NULL,
  "pattern" varchar(255) default NULL,
  "patternmsg" varchar(255) default NULL,
  "entityName" varchar(255) default NULL,
  "operator" varchar(255) default NULL,
  "is_key" bit(1) default NULL,
  "tagtype" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
CREATE TABLE "TReportParameters" (
  "reportId" bigint(20) NOT NULL,
  "paramId" bigint(20) NOT NULL,
  KEY "FKDEC51EB2E57D617A" ("reportId"),
  KEY "FKDEC51EB2A08FA98A" ("paramId"),
  CONSTRAINT "FKDEC51EB2A08FA98A" FOREIGN KEY ("paramId") REFERENCES "TReportParameter" ("idno"),
  CONSTRAINT "FKDEC51EB2E57D617A" FOREIGN KEY ("reportId") REFERENCES "TReportDefinition" ("idno")
);
CREATE TABLE "TReportTemplate" (
  "idno" bigint(20) NOT NULL,
  "content" longblob,
  "description" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
CREATE TABLE "TRole" (
  "idno" bigint(20) NOT NULL,
  "name" varchar(255) default NULL,
  "description" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
INSERT INTO "TRole" ("idno", "name", "description") VALUES (1,'default','common permissions for all users'),(2,'A部门管理员','common permissions for all users'),(3,'操作员','common permissions for all users');
CREATE TABLE "TRolePermissions" (
  "RoleId" bigint(20) NOT NULL,
  "Permission" varchar(255) default NULL,
  KEY "FKAA8DB3AB1E777EC" ("RoleId"),
  CONSTRAINT "FKAA8DB3AB1E777EC" FOREIGN KEY ("RoleId") REFERENCES "TRole" ("idno")
);
INSERT INTO "TRolePermissions" ("RoleId", "Permission") VALUES (2,'/system/main');
CREATE TABLE "TRules" (
  "idno" bigint(20) NOT NULL,
  "description" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  "rule" text,
  "type" varchar(255) default NULL,
  "mapping" text,
  PRIMARY KEY  ("idno")
);
INSERT INTO "TRules" ("idno", "description", "name", "rule", "type", "mapping") VALUES (1,NULL,'POS消费积分规则','adfafafsdaf','积分规则','sadfafsafa'),(2,NULL,'网银交易积分规则','if 交易.交易类型==\'网银交易\'\r\n   if 交易.操作类型==\'异地转帐\' or 交易.操作类型==\'跨行转帐\' or 交易.操作类型==\'信用卡还款\' or 交易.操作类型==\'在线支付\'\r\n      if 交易.交易金额/100>{积分1}\r\n         积分.网银积分+=50\r\n      esle\r\n         积分.网银积分+=交易.交易金额/{金额1}','积分规则','1、转帐类交易，进行非同城转帐和跨行转账，交易金额每满<金额1:100>元人民币，即可积1分；单笔交易最高积分不超过<积分1:50>分；\r\n2、使用信用卡还款功能通过借记卡完成对信用卡透支金额还款，交易金额每满<金额1:100>元人民币，即可积1分,单笔交易最高积分不超过<积分1:50>分；\r\n3、在线支付，每<金额:100>元人民币积1分；单笔在线支付最高积分不超过<积分1:50>分。');
CREATE TABLE "TUser" (
  "idno" bigint(20) NOT NULL,
  "creationDate" datetime default NULL,
  "disabled" bit(1) default NULL,
  "disabledDate" datetime default NULL,
  "ip" varchar(255) default NULL,
  "lastAccessDate" datetime default NULL,
  "name" varchar(255) default NULL,
  "password" varchar(255) default NULL,
  "superuser" bit(1) default NULL,
  PRIMARY KEY  ("idno")
);
INSERT INTO "TUser" ("idno", "creationDate", "disabled", "disabledDate", "ip", "lastAccessDate", "name", "password", "superuser") VALUES (1,NULL,'\0',NULL,NULL,NULL,'test','test12','\0'),(2,NULL,'\0',NULL,NULL,NULL,'admin','123456','');
CREATE TABLE "TUserInfo" (
  "userId" bigint(20) NOT NULL,
  "val" varchar(255) default NULL,
  "name" varchar(80) NOT NULL,
  PRIMARY KEY  ("userId","name"),
  KEY "FK9A67B78DB73CCD56" ("userId"),
  CONSTRAINT "FK9A67B78DB73CCD56" FOREIGN KEY ("userId") REFERENCES "TUser" ("idno")
);
CREATE TABLE "TUserRoles" (
  "userId" bigint(20) NOT NULL,
  "roleId" bigint(20) NOT NULL,
  PRIMARY KEY  ("userId","roleId"),
  KEY "FKB30E97BEB1E777EC" ("roleId"),
  KEY "FKB30E97BEB73CCD56" ("userId"),
  CONSTRAINT "FKB30E97BEB1E777EC" FOREIGN KEY ("roleId") REFERENCES "TRole" ("idno"),
  CONSTRAINT "FKB30E97BEB73CCD56" FOREIGN KEY ("userId") REFERENCES "TUser" ("idno")
);
CREATE TABLE "TestUser" (
  "idno" bigint(20) NOT NULL,
  "userid" bigint(20) default NULL,
  "amount" decimal(19,2) default NULL,
  "creationDate" datetime default NULL,
  "description" varchar(255) default NULL,
  "name" varchar(255) default NULL,
  "password" varchar(255) default NULL,
  PRIMARY KEY  ("idno")
);
CREATE TABLE "USER_ALERT_MAP" (
  "USER_ID" int(11) NOT NULL,
  "ALERT_ID" int(11) default NULL,
  "REPORT_ID" int(11) default NULL,
  "ALERT_LIMIT" int(11) default NULL,
  "ALERT_OPERATOR" varchar(255) default NULL,
  "MAP_ID" int(11) NOT NULL,
  PRIMARY KEY  ("USER_ID","MAP_ID"),
  KEY "FKD83C845CB12A22B" ("USER_ID"),
  KEY "FKD83C84564AE609" ("ALERT_ID"),
  CONSTRAINT "FKD83C84564AE609" FOREIGN KEY ("ALERT_ID") REFERENCES "REPORT_ALERT" ("ALERT_ID"),
  CONSTRAINT "FKD83C845CB12A22B" FOREIGN KEY ("USER_ID") REFERENCES "REPORT_USER" ("REPORTUSER_ID")
);
CREATE TABLE "USER_GROUP_MAP" (
  "USER_ID" int(11) NOT NULL,
  "GROUP_ID" int(11) NOT NULL,
  "MAP_ID" int(11) NOT NULL,
  PRIMARY KEY  ("USER_ID","MAP_ID"),
  KEY "FKC49EBE8CB12A22B" ("USER_ID"),
  KEY "FKC49EBE8C487AF29" ("GROUP_ID"),
  CONSTRAINT "FKC49EBE8C487AF29" FOREIGN KEY ("GROUP_ID") REFERENCES "REPORT_GROUP" ("GROUP_ID"),
  CONSTRAINT "FKC49EBE8CB12A22B" FOREIGN KEY ("USER_ID") REFERENCES "REPORT_USER" ("REPORTUSER_ID")
);
CREATE TABLE "USER_SECURITY" (
  "USER_ID" int(11) NOT NULL,
  "ROLE_NAME" varchar(255) default NULL,
  KEY "FK7DE1C934CB12A22B" ("USER_ID"),
  CONSTRAINT "FK7DE1C934CB12A22B" FOREIGN KEY ("USER_ID") REFERENCES "REPORT_USER" ("REPORTUSER_ID")
);
INSERT INTO "USER_SECURITY" ("USER_ID", "ROLE_NAME") VALUES (5,'LOG_VIEWER_ROLE'),(1,'CHART_ADMIN_ROLE'),(1,'REPORT_ADMIN_ROLE'),(1,'GROUP_ADMIN_ROLE'),(1,'LOG_VIEWER_ROLE'),(1,'PARAMETER_ADMIN_ROLE'),(1,'DATASOURCE_ADMIN_ROLE'),(1,'SCHEDULER_ROLE'),(1,'UPLOAD_ROLE'),(1,'ROOT_ADMIN_ROLE'),(5,'SCHEDULER_ROLE'),(5,'REPORT_ADMIN_ROLE'),(5,'UPLOAD_ROLE'),(1,'ADVANCED_SCHEDULER_ROLE');
CREATE TABLE "authors" (
  "id" bigint(20) NOT NULL,
  "elt" bigint(20) NOT NULL,
  KEY "FKD977104835B728BB" ("id"),
  KEY "FKD9771048A302392F" ("elt"),
  CONSTRAINT "FKD977104835B728BB" FOREIGN KEY ("id") REFERENCES "Book" ("id"),
  CONSTRAINT "FKD9771048A302392F" FOREIGN KEY ("elt") REFERENCES "Author" ("id")
);
CREATE TABLE "knc_cust" (
  "custno" varchar(255) NOT NULL default '',
  "brchno" varchar(255) default NULL,
  "custna" varchar(255) default NULL,
  "netbank" varchar(255) default NULL,
  PRIMARY KEY  ("custno")
);
CREATE TABLE "knc_prsn" (
  "custno" varchar(255) NOT NULL default '',
  "borndt" varchar(255) default NULL,
  "mobitl" varchar(255) default NULL,
  "sextp" varchar(255) default NULL,
  PRIMARY KEY  ("custno")
);
CREATE TABLE "kns_tran" (
  "transq" varchar(255) NOT NULL default '',
  "trandt" varchar(255) default NULL,
  "tranti" varchar(255) default NULL,
  "acctno" varchar(255) default NULL,
  "tranam" decimal(10,0) default NULL,
  "trantp" varchar(255) default NULL,
  "servtp" varchar(255) default NULL,
  "mrchtp" varchar(255) default NULL,
  "trancy" varchar(255) default NULL,
  "transt" varchar(255) default NULL,
  PRIMARY KEY  ("transq")
);
