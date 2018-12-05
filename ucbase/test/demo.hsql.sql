CREATE TABLE BRANCH (
       branch_id            VARCHAR(50),
       branch_desc          VARCHAR(50),
       feebyselfOpened      INTEGER,
       feeAgentPort         VARCHAR(20),
       feeAgentIP           VARCHAR(20),
       test_date			date
);

CREATE TABLE TEST (
       charField			CHAR(1),
       varcharField			VARCHAR(20),
       dateField			DATE,
       timestampField       TIMESTAMP,
       amountField          DECIMAL(15,2),
       intField				INTEGER,
       bigDataField			object,
       keyField				VARCHAR(20)
, PRIMARY KEY (keyField));


CREATE TABLE DUMMYTABLE (
       DUMMYKEY             CHAR(1)
);

