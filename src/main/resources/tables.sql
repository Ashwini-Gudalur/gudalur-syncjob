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
CREATE SEQUENCE syncjob_visit_id_seq;
create table syncjob_visit(
  id INT PRIMARY KEY NOT NULL  DEFAULT nextval('syncjob_visit_id_seq'),
  erp_patient_id int NOT NULL,
  diagnoses           TEXT,
  visit_uuid           CHAR(50) NOT NULL UNIQUE,
  visit_startdate         TIMESTAMP NOT NULL,
  visit_stopdate         TIMESTAMP NOT NULL,
  visit_type CHAR(50) NOT NULL,
  visit_type_id SMALLINT NOT NULL ,
  date         TIMESTAMP NOT NULL
);
ALTER SEQUENCE syncjob_visit_id_seq OWNED BY syncjob_visit.id;

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

#View for IPD billing
CREATE OR REPLACE VIEW visit_so_payment_rln AS SELECT * FROM
  (select sum(bp.total_paid) paid,
  so.id as order_id,
  amount_original,
  erp_patient_id,
  sv.visit_uuid,
  sv.id as visit_id,
  sv.visit_startdate,
  sv.visit_stopdate,
  so.date_confirm,
  so.create_date,
  so.state,
  so.name,so.amount_total,so.amount_tax,so.amount_untaxed,so.discount_amount,so.discount
   from (Select
  avl.name, move_line_id, amount_original,aml.ref,
  sum(CASE WHEN avl.type='cr' THEN 1*avl.amount
      ELSE -1*avl.amount
      END) as total_paid,
  bool_or(reconcile) as reconciled
from account_voucher_line avl
  INNER JOIN account_voucher av on avl.voucher_id = av.id
  INNER JOIN account_move_line aml on avl.move_line_id=aml.id
WHERE av.state != 'cancel'
group by avl.name, move_line_id, amount_original,aml.ref
) bp
  INNER JOIN sale_order so on so.name = bp.ref
  INNER JOIN syncjob_visit sv ON sv.erp_patient_id = so.partner_id
where so.create_date BETWEEN sv.visit_startdate  AND sv.visit_stopdate
GROUP BY order_id,erp_patient_id,
  sv.visit_uuid,
  visit_id,
sv.visit_startdate,
sv.visit_stopdate,
so.date_confirm,
so.create_date,
so.state,
  amount_original,
  so.name,so.amount_total,so.amount_tax,so.amount_untaxed,so.discount_amount,so.discount,
    so.name) vv
;



CREATE OR REPLACE VIEW visit_so_rln AS SELECT * FROM
  (
    SELECT
      so.id as order_id,
      so.amount_total,so.amount_tax,so.amount_untaxed,so.discount_amount,so.discount,
      erp_patient_id,
      sv.visit_uuid,
      sv.visit_type_id,
      sv.id as visit_id,
      sv.visit_startdate,
      sv.visit_stopdate,
      so.date_confirm,
      so.create_date,
      so.state
    FROM sale_order so
      INNER JOIN syncjob_visit sv ON sv.erp_patient_id = so.partner_id
    WHERE so.create_date BETWEEN sv.visit_startdate  AND sv.visit_stopdate

  ) as vv;