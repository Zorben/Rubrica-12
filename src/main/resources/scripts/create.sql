create table IF NOT EXISTS AUTORES(
	idAutor int IDENTITY(1,1) PRIMARY KEY,
	nombreAutor varchar(25),
	fechaNacimiento date,
);
create table IF NOT EXISTS LIBROS(
	isbn int PRIMARY KEY,
	titulo varchar(25),
	idAutor int,
);
