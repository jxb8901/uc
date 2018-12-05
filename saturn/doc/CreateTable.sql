DROP TABLE knc_cust;
CREATE TABLE knc_cust
(
	custid INT,
	custno VARCHAR(20) NOT NULL PRIMARY KEY,
	brchno VARCHAR(20),
	custna VARCHAR(100),
	custtp VARCHAR(1),
	custst VARCHAR(1)
);

DROP TABLE knc_prsn;
CREATE TABLE knc_prsn
(
	custid INT,
	custno VARCHAR(20) NOT NULL PRIMARY KEY,
	borndt DATETIME,
	mobitl VARCHAR(100),
	sextp VARCHAR(1)
);

DROP TABLE kns_tran;
CREATE TABLE kns_tran
(
	trandt DATETIME,
	transq VARCHAR(50) NOT NULL PRIMARY KEY,
	tranus VARCHAR(20),
	acctno VARCHAR(50),
	tranbr VARCHAR(20),
	trantp VARCHAR(10),
	trancy VARCHAR(10),
	smrycd VARCHAR(10),
	dcmtno VARCHAR(50),
	dcmttp VARCHAR(10),
	prcscd VARCHAR(20),
	tranam DECIMAL,
	transt VARCHAR(1),
	servtp VARCHAR(10),
	acetcd VARCHAR(10)
);