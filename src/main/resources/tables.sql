CREATE SEQUENCE syncjob_job_id_seq;
create table syncjob_job(
  id INT PRIMARY KEY NOT NULL  DEFAULT nextval('syncjob_job_id_seq'),
  status           CHAR(50) NOT NULL,
  logFile           CHAR(50) NOT NULL,
  date         TIMESTAMP NOT NULL
);
ALTER SEQUENCE syncjob_job_id_seq OWNED BY syncjob_job.id;
create table syncjob_sickle_cell_patient(
  erp_id int NOT NULL UNIQUE,
  date         TIMESTAMP NOT NULL
);

create table syncjob_visit(
  erp_patient_id int NOT NULL,
  diagnoses           TEXT,
  visit_uuid           CHAR(50) NOT NULL UNIQUE,
  visit_startdate         TIMESTAMP NOT NULL,
  visit_stopdate         TIMESTAMP NOT NULL,
  visit_type CHAR(50) NOT NULL,
  visit_type_id SMALLINT NOT NULL ,
  date         TIMESTAMP NOT NULL
);

create table syncjob_patient_extn(
  erp_id int NOT NULL UNIQUE,
  gender           CHAR(1) NOT NULL,
  birthdate         TIMESTAMP NOT NULL
);

create table syncjob_chargetype_category_mapping (
  chargetype_name CHAR(50) NOT NULL,
  category_id int NOT NULL
);
insert into syncjob_chargetype_category_mapping
(chargetype_name,category_id)
SELECT 'Investigations',id from product_category where parent_id= (select id from product_category where name='Lab');
insert into syncjob_chargetype_category_mapping
(chargetype_name,category_id)
SELECT 'Medicines',id from product_category where parent_id= (select id from product_category where name='Drug');
insert into syncjob_chargetype_category_mapping
(chargetype_name,category_id)
  SELECT 'Procedure',id from product_category where name in ('Radiology','USG','Procedure','Dental');
-- 1=> SickleCell 2=>Bed Grant
create table claim_type(
  erp_patient_id int references res_partner(id),
  claim_type SMALLINT
);


