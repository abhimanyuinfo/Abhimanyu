CREATE TABLE accumulative_count (
  id int(11) NOT NULL AUTO_INCREMENT,
  ordering_currency varchar(32) DEFAULT NULL,
  count bigint(20) DEFAULT '0',
  PRIMARY KEY (id)
);

CREATE TABLE invalid_deals (
  id int(11) NOT NULL AUTO_INCREMENT,
  deal_id double DEFAULT NULL,
  from_currency_iso_code varchar(128) DEFAULT NULL,
  to_currency_iso_code varchar(128) DEFAULT NULL,
  date datetime DEFAULT NULL,
  deal_amount double DEFAULT '0',
  file_name varchar(256) DEFAULT NULL,
  reason varchar(1000) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE valid_deals (
  id int(11) NOT NULL AUTO_INCREMENT,
  deal_id double DEFAULT NULL,
  from_currency_iso_code varchar(128) DEFAULT NULL,
  to_currency_iso_code varchar(128) DEFAULT NULL,
  deal_amount double DEFAULT NULL,
  file_name varchar(256) DEFAULT NULL,
  date datetime DEFAULT NULL,
  PRIMARY KEY (id)
);
