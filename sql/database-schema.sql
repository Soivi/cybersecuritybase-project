/* Do not change this schema */
DROP TABLE IF EXISTS Signup;
CREATE TABLE Signup (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(200),
    address varchar(200)
);
