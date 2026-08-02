-- Create sequence for table employee
CREATE SEQUENCE employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Create table employee
CREATE TABLE employee (
    id INTEGER NOT NULL DEFAULT nextval('employee_id_seq') PRIMARY KEY,
    nome TEXT NOT NULL,
    dept TEXT NOT NULL
);

-- Create sequence for table ferias
CREATE SEQUENCE ferias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Create table ferias
CREATE TABLE ferias (
    id INTEGER NOT NULL DEFAULT nextval('ferias_id_seq') PRIMARY KEY,
    id_employee INTEGER NOT NULL REFERENCES employee(id),
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    CONSTRAINT chk_ferias_periodo CHECK (data_fim >= data_inicio)
);
