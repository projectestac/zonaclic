CREATE TABLE  "LOG_USERACTIONS" 
   (	"LOG_DATE" DATE NOT NULL ENABLE, 
	"LOG_TYPE" CHAR(5) NOT NULL ENABLE, 
	"LOG_USER" VARCHAR2(100), 
	"LOG_MSG" VARCHAR2(1024)
   )
/

CREATE INDEX  "LOG_USERACTIONS_LOG_DATE_IDX" ON  "LOG_USERACTIONS" ("LOG_DATE")
/

CREATE INDEX  "LOG_USERACTIONS_LOG_TYPE_IDX" ON  "LOG_USERACTIONS" ("LOG_TYPE")
/
