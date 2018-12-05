delete from knc_cust;
delete from knc_prsn;
-- 女性客户
insert into knc_cust values('1000000001', '1', '张三', '1');
insert into knc_prsn values('1000000001', '1980-03-15', '13088888888', '1');
-- 男性客户
insert into knc_cust values('1000000002', '1', '李四', '1');
insert into knc_prsn values('1000000002', '1978-01-23', '13088888888', '0');
insert into knc_cust values('1000000003', '1', '王五', '0');
insert into knc_prsn values('1000000003', '1964-12-25', '13088888888', '0');
-- ID 日期 时间 
--帐号    金额    交易类型（现金CS／转帐TP）    交易渠道    商户类型    币种(01.CNY)    交易状态

delete from kns_tran;
insert into kns_tran values('1', '2007-02-23', '12:13:56', 
        '100000000100001', 100.00, 'POS', '1001', '01', '01', '1'); 
insert into kns_tran values('2', '2007-01-23', '12:13:56', 
        '100000000200001', 200.00, 'POS', '1002', '02', '01', '1'); 
insert into kns_tran values('3', '2007-03-23', '12:13:56', 
        '100000000100001', 400.00, 'ATM', '1003', '02', '01', '1'); 
insert into kns_tran values('4', '2007-03-23', '12:13:56', 
        '100000000300001', 200.00, 'NET', '1004', '99', '01', '1'); 
insert into kns_tran values('5', '2007-01-23', '12:13:56', 
        '100000000200001', 580.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('6', '2007-01-23', '12:13:56', 
        '100000000300001', 920.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('7', '2007-01-23', '12:13:56', 
        '100000000100001', 1356.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('8', '2007-08-31', '12:13:56', 
        '100000000200001', 680.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('9', '2007-08-31', '12:13:56', 
        '100000000300001', 289.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('10', '2007-08-31', '12:13:56', 
        '100000000100001', 785.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('11', '2007-08-31', '12:13:56', 
        '100000000300001', 229.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('12', '2007-08-31', '12:13:56', 
        '100000000100001', 95.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('13', '2007-08-31', '12:13:56', 
        '100000000100001', 95.00, 'POS', '1004', '99', '01', '1'); 
insert into kns_tran values('14', '2007-08-31', '12:13:56', 
        '100000000100001', 925.00, 'POS', '1004', '99', '01', '1'); 
 
 
delete from TPointType;
-- 初始化积分类型表
insert into TPointType (NAME, DESCRIPTION) values('POS积分','POS积分');
insert into TPointType (NAME, DESCRIPTION) values('网银积分','网银积分');
insert into TPointType (NAME, DESCRIPTION) values('消费积分','消费积分');

       
delete from POINTACCOUNT;        
-- 初始化积分帐户表
--ID    客户号    积分类型    积分额

insert into POINTACCOUNT (CUSTOMERID,POINTTYPE,POINT)( select custno , name , 0 from knc_prsn  cross join TPointType on 1=1 );


insert into TMetric(idno,name) values(1,'POS交易笔数');
insert into TMetric(idno,name) values(2,'交易笔数');






