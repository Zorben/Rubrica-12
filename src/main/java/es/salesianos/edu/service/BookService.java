package es.salesianos.edu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.javascript.jscomp.Result;

import es.salesianos.edu.connection.*;
import es.salesianos.edu.model.Author;
import es.salesianos.edu.model.Book;



@Service
public class BookService {
	
	@Autowired
	AuthorService authorService;
	
	// DEFINE LA RUTA A LA BASE DE DATOS DESDE EN EL PROYECTO
	private static final String jdbcUrl = "jdbc:h2:file:./src/main/resources/BibliotecaDB";
		
	static ConnectionManager manager = new ConnectionH2();

	private static final Logger logger = LogManager.getLogger(BookService.class.getName());

	public String flagMessage="";
	
	public String getFlagMessage() {
		return flagMessage;
	}
	public void setFlagMessage(String flagMessage) {
		this.flagMessage = flagMessage;
	}
	
	
	
	// FUNCION QUE CIERRA LA CONSULTA SQL
	private static void close(PreparedStatement prepareStatement) {
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
	// FUNCION QUE CIERRA LA TABLA QUE RECIBE EL RESULTADO DE LA CONSULTA SQL
		private static void close(ResultSet resultSet) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	
	public boolean insert(Book book) {
		
		boolean isInserted;
		
		if(search(book).isEmpty()){
			// No existe en la bd
			logger.debug("realizando insercion");
			ResultSet resultSet = null;
			PreparedStatement prepareStatement = null;
			Connection conn = manager.open(jdbcUrl);
			
			try{
				prepareStatement = conn.prepareStatement("INSERT INTO LIBROS(ISBN, TITULO, IDAUTOR) VALUES(?,?,?)");
				prepareStatement.setInt(1, book.getIsbn());
				prepareStatement.setString(2, book.getTitle());
				prepareStatement.setInt(3, book.getAuthor().getIdAuthor());
				prepareStatement.executeUpdate();
				setFlagMessage("El libro ha sido insertado");
				isInserted = true;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally {
				close(prepareStatement);
				manager.close(conn);	
			}
		}
		
		else{
			setFlagMessage("El libro ya existe, introduzca otro");
			isInserted = false;
		}
		return isInserted;
	}
	
	public List searchAll() {
		logger.debug("realizando seleccion total");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Book> booksFoundList = new ArrayList<Book>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM LIBROS");
			resultSet = prepareStatement.executeQuery();
			Book foundBook;
			Author authorOfBook;
			while(resultSet.next()){
				foundBook = new Book();
				foundBook.setIsbn(resultSet.getInt(1));
				foundBook.setTitle(resultSet.getString(2));
				authorOfBook = authorService.SearchById(resultSet.getInt(3));
				foundBook.setAuthor(authorOfBook);
				booksFoundList.add(foundBook);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return booksFoundList;
	}
	
	public List search(Book book) {
		logger.debug("realizando seleccion");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Book> bookFoundList = new ArrayList<Book>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM LIBROS WHERE isbn = ?");
			prepareStatement.setInt(1, book.getIsbn());
			resultSet = prepareStatement.executeQuery();
			Book foundBook;
			while(resultSet.next()){
				foundBook= new Book();
				foundBook.setIsbn(resultSet.getInt(1));
				foundBook.setTitle(resultSet.getString(2));
				foundBook.setAuthor(authorService.SearchById(resultSet.getInt(3)));
				bookFoundList.add(foundBook);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return bookFoundList;
	}
	
	public List partialSearch(Book book) {
		logger.debug("realizando seleccion");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Book> bookFoundList = new ArrayList<Book>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM LIBROS WHERE TITULO like ?");
			prepareStatement.setString(1, "%"+book.getTitle()+"%");
			resultSet = prepareStatement.executeQuery();
			Book foundBook = null;
			while(resultSet.next()){
				foundBook= new Book();
				foundBook.setIsbn(resultSet.getInt(1));
				foundBook.setTitle(resultSet.getString(2));
				foundBook.setAuthor(authorService.SearchById(resultSet.getInt(3)));
				bookFoundList.add(foundBook);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return bookFoundList;
	}
	
	public List partialSearch(String title) {
		logger.debug("realizando seleccion");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Book> bookFoundList = new ArrayList<Book>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM LIBROS WHERE TITULO like ?");
			prepareStatement.setString(1, "%"+title+"%");
			resultSet = prepareStatement.executeQuery();
			Book foundBook = null;
			while(resultSet.next()){
				foundBook= new Book();
				foundBook.setIsbn(resultSet.getInt(1));
				foundBook.setTitle(resultSet.getString(2));
				foundBook.setAuthor(authorService.SearchById(resultSet.getInt(3)));
				bookFoundList.add(foundBook);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return bookFoundList;
	}
	
	public List partialSearch(Author author) {
		logger.debug("realizando seleccion");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Book> bookFoundList = new ArrayList<Book>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM LIBROS WHERE idAutor = ?");
			prepareStatement.setInt(1, author.getIdAuthor());
			resultSet = prepareStatement.executeQuery();
			Book foundBook = null;
			while(resultSet.next()){
				foundBook= new Book();
				foundBook.setIsbn(resultSet.getInt(1));
				foundBook.setTitle(resultSet.getString(2));
				foundBook.setAuthor(authorService.SearchById(resultSet.getInt(3)));
				bookFoundList.add(foundBook);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return bookFoundList;
	}
	
	public Book SearchByIsnb(int isbn) {
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		Book foundBook = null;
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM LIBROS WHERE ISBN = ?");
			prepareStatement.setInt(1, isbn);
			resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				foundBook=new Book();
				foundBook.setIsbn(resultSet.getInt(1));
				foundBook.setTitle(resultSet.getString(2));
				foundBook.setAuthor(authorService.SearchById(resultSet.getInt(3)));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return foundBook;
	}
	
	
	
	/**
	 * @param resultSet
	 * @param prepareStatement
	 * @param conn
	 */
	private void close(ResultSet resultSet, PreparedStatement prepareStatement, Connection conn) {
		close(resultSet);
		close(prepareStatement);
		manager.close(conn);
	}
}
