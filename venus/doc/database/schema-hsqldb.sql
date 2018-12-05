alter table MetricValue drop constraint FKFB5361811D79FA59;
alter table PromotionRuleParams drop constraint FK8854C0852C537632;
alter table TCustomerGroup drop constraint FKCB4D194D508DE1D;
alter table TCustomerGroup drop constraint FKCB4D194D45469C26;
alter table TDispatchingGift drop constraint FK2281D784E93B81C5;
alter table TDispatchingGift drop constraint FK2281D784DBBD74A1;
alter table TEvalMetrics drop constraint FKDEE6EB93DCB4061D;
alter table TEvalMetrics drop constraint FKDEE6EB93D1DE5F21;
alter table TPromotionPlan drop constraint FK77034A78508DE1D;
alter table TPromotionPlan drop constraint FK77034A784C1A050D;
alter table TPromotionPlan drop constraint FK77034A785E55FE06;
alter table TRolePermissions drop constraint FKAA8DB3AB1E777EC;
alter table TStudentClassMates drop constraint FK6D19EE1DD4D4961;
alter table TStudentClassMates drop constraint FK6D19EE1DD4D2B59;
alter table TStudentForTest drop constraint FKDB028FB4D80147B;
alter table TStudentRoles drop constraint FK4AF1BFB6D4D5FE7;
alter table TUserInfo drop constraint FK9A67B78DB73CCD56;
alter table TUserRoles drop constraint FKB30E97BEB1E777EC;
alter table TUserRoles drop constraint FKB30E97BEB73CCD56;
alter table knc_prsn drop constraint FKD46A595C67328017;
drop table MetricValue;
drop table PromotionRuleParams;
drop table TCustomerGroup;
drop table TDispatchingGift;
drop table TEvalMetrics;
drop table TEvaluation;
drop table TGift;
drop table TMetric;
drop table TPointType;
drop table TPromotionPlan;
drop table TRole;
drop table TRolePermissions;
drop table TRules;
drop table TStudentClassMates;
drop table TStudentForTest;
drop table TStudentRoles;
drop table TUser;
drop table TUserInfo;
drop table TUserRoles;
drop table knc_cust;
drop table knc_prsn;
create table MetricValue (F_Id numeric(19,0) identity not null, F_DimensionType varchar(255) null, F_Dimension varchar(255) null, F_MetricId numeric(19,0) null, F_TargetType varchar(255) null, F_TargetId numeric(19,0) null, F_QuotaValue numeric(19,2) null, F_Frequence varchar(255) null, F_Date datetime null, primary key (F_Id));
create table PromotionRuleParams (PID numeric(19,0) not null, val varchar(255) null, name varchar(80) not null, primary key (PID, name));
create table TCustomerGroup (idno numeric(19,0) identity not null, creationDate datetime null, name varchar(255) null, description varchar(255) null, frule varchar(255) null, creator varchar(255) null, evaluation numeric(19,0) null, parent numeric(19,0) null, primary key (idno));
create table TDispatchingGift (idno numeric(19,0) identity not null, applyDate datetime null, customer varchar(255) null, gift numeric(19,0) null, quantity int null, status int null, primary key (idno));
create table TEvalMetrics (eid numeric(19,0) not null, mid numeric(19,0) not null);
create table TEvaluation (idno numeric(19,0) identity not null, creationDate datetime null, creator varchar(255) null, description varchar(255) null, formula varchar(255) null, name varchar(255) null, type varchar(255) null, primary key (idno));
create table TGift (idno numeric(19,0) identity not null, availableQuantity int null, costPrice numeric(19,2) null, description varchar(255) null, exchangeRule varchar(255) null, marketPrice numeric(19,2) null, name varchar(255) null, seqno varchar(255) null unique, image image null, imageFileName varchar(255) null, primary key (idno));
create table TMetric (idno numeric(19,0) identity not null, description varchar(255) null, formula varchar(255) null, type varchar(255) null, frequence varchar(255) null, name varchar(255) null, ratio varchar(255) null, unit varchar(255) null, primary key (idno));
create table TPointType (idno numeric(19,0) identity not null, description varchar(255) null, name varchar(255) null, primary key (idno));
create table TPromotionPlan (idno numeric(19,0) identity not null, creationDate datetime null, creator varchar(255) null, description varchar(255) null, endDate datetime null, evaluation numeric(19,0) null, name varchar(255) null, frule numeric(19,0) null, startDate datetime null, status varchar(255) null, target numeric(19,0) not null, primary key (idno));
create table TRole (idno numeric(19,0) identity not null, name varchar(255) null, description varchar(255) null, primary key (idno));
create table TRolePermissions (RoleId numeric(19,0) not null, Permission varchar(255) null);
create table TRules (idno numeric(19,0) identity not null, description varchar(255) null, name varchar(255) null, frule text null, type varchar(255) null, mapping text null, primary key (idno));
create table TStudentClassMates (eid numeric(19,0) not null, mid numeric(19,0) not null);
create table TStudentForTest (idno numeric(19,0) identity not null, name varchar(255) null, peer numeric(19,0) null, mainrole varchar(255) null, primary key (idno));
create table TStudentRoles (sid numeric(19,0) not null, role varchar(255) null);
create table TUser (idno numeric(19,0) identity not null, creationDate datetime null, disabled tinyint null, disabledDate datetime null, ip varchar(255) null, lastAccessDate datetime null, name varchar(255) null, password varchar(255) null, superuser tinyint null, primary key (idno));
create table TUserInfo (userId numeric(19,0) not null, val varchar(255) null, name varchar(80) not null, primary key (userId, name));
create table TUserRoles (userId numeric(19,0) not null, roleId numeric(19,0) not null, primary key (userId, roleId));
create table knc_cust (custno varchar(255) not null, custna varchar(255) null, primary key (custno));
create table knc_prsn (custno varchar(255) not null, mobitl varchar(255) null, primary key (custno));
alter table MetricValue add constraint FKFB5361811D79FA59 foreign key (F_MetricId) references TMetric;
alter table PromotionRuleParams add constraint FK8854C0852C537632 foreign key (PID) references TPromotionPlan;
alter table TCustomerGroup add constraint FKCB4D194D508DE1D foreign key (evaluation) references TEvaluation;
alter table TCustomerGroup add constraint FKCB4D194D45469C26 foreign key (parent) references TCustomerGroup;
alter table TDispatchingGift add constraint FK2281D784E93B81C5 foreign key (gift) references TGift;
alter table TDispatchingGift add constraint FK2281D784DBBD74A1 foreign key (customer) references knc_cust;
alter table TEvalMetrics add constraint FKDEE6EB93DCB4061D foreign key (mid) references TMetric;
alter table TEvalMetrics add constraint FKDEE6EB93D1DE5F21 foreign key (eid) references TEvaluation;
alter table TPromotionPlan add constraint FK77034A78508DE1D foreign key (evaluation) references TEvaluation;
alter table TPromotionPlan add constraint FK77034A784C1A050D foreign key (target) references TCustomerGroup;
alter table TPromotionPlan add constraint FK77034A785E55FE06 foreign key (frule) references TRules;
alter table TRolePermissions add constraint FKAA8DB3AB1E777EC foreign key (RoleId) references TRole;
alter table TStudentClassMates add constraint FK6D19EE1DD4D4961 foreign key (mid) references TStudentForTest;
alter table TStudentClassMates add constraint FK6D19EE1DD4D2B59 foreign key (eid) references TStudentForTest;
alter table TStudentForTest add constraint FKDB028FB4D80147B foreign key (peer) references TStudentForTest;
alter table TStudentRoles add constraint FK4AF1BFB6D4D5FE7 foreign key (sid) references TStudentForTest;
alter table TUserInfo add constraint FK9A67B78DB73CCD56 foreign key (userId) references TUser;
alter table TUserRoles add constraint FKB30E97BEB1E777EC foreign key (roleId) references TRole;
alter table TUserRoles add constraint FKB30E97BEB73CCD56 foreign key (userId) references TUser;
alter table knc_prsn add constraint FKD46A595C67328017 foreign key (custno) references knc_cust;