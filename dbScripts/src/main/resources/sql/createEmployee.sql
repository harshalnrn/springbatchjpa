DROP TABLE IF EXISTS EMPLOYEE CASCADE ;
CREATE TABLE IF NOT EXISTS EMPLOYEE (
   id INT PRIMARY KEY,
   password VARCHAR(50),
   email VARCHAR(50) ,
   sex VARCHAR(50) ,
   age VARCHAR(200),
   title VARCHAR(50) ,
   salary NUMBER(50)
);