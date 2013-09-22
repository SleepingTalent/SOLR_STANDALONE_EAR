/*Drop the code example database */
DROP DATABASE IF EXISTS codeExampleDb;

/*Create the code example Database */
CREATE DATABASE codeExampleDb;

/*Drop code example user */
DROP USER 'codeExample'@'localhost';

/* Create code example  user */
CREATE USER 'codeExample'@'localhost' IDENTIFIED BY 'codeExample';

grant all privileges on codeExampleDb.* to codeExample@localhost ;

