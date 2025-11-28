# READ ME .Proyecto Final - Biblioteca (Java + JDBC + Swing + Patrones)

Materia: Paradigmas de Programación
Alumno: Santiago Tomas Leguizamon
Documento: 43525672

## Resumen
Aplicación de BIBLIOTECA para gestionar el CRUD de libros.

Implementa:
- Singleton (DatabaseConnection)
- DAO (LibroDAO, AutorDAO, GeneroDAO, EditorialDAO)
- Factory Method (CreadorLibro y variantes)
- Interfaz gráfica con Swing

## Estructura de paquetes
- conexion: DatabaseConnection
- modelo: Libro, Autor, Genero, Editorial
- dao: LibroDAO, AutorDAO, GeneroDAO, EditorialDAO
- factory: CreadorLibro, CreadorLibroNormal, CreadorLibroOferta, CreadorLibroPremium
- ui: VentanaBiblioteca
- Main.java

## Requisitos
- SQL Server con la base `Biblioteca` y las tablas: Autores, Generos, Editoriales, Libros.
- Driver JDBC de Microsoft (mssql-jdbc). Añadirlo al classpath del proyecto en VS Code.

## Creación de Tablas en SQLServer. (Adaptar tu cadena de conexión en databaseConnection.js para la correcta implementación)
   CREATE TABLE Autores 
( id_autor INT IDENTITY(1,1) PRIMARY KEY, 
autor VARCHAR(200) NOT NULL ); 

CREATE TABLE Generos 
( id_genero INT IDENTITY(1,1) PRIMARY KEY,
genero VARCHAR(100) NOT NULL ); 

CREATE TABLE Editoriales 
( id_editorial INT IDENTITY(1,1) PRIMARY KEY, 
editorial VARCHAR(200) NOT NULL ); 

CREATE TABLE Libros 
( id_libro INT IDENTITY(1,1) PRIMARY KEY, 
titulo VARCHAR(200) NOT NULL, 
id_autor INT NOT NULL, 
id_genero INT NOT NULL, 
id_editorial INT NOT NULL, 
precio DECIMAL(10,2) NOT NULL, 
isbn VARCHAR(20), 
tipo VARCHAR(20) NOT NULL DEFAULT 'NORMAL',

FOREIGN KEY (id_autor) REFERENCES Autores(id_autor), 
FOREIGN KEY (id_genero) REFERENCES Generos(id_genero), 
FOREIGN KEY (id_editorial) REFERENCES Editoriales(id_editorial) );
