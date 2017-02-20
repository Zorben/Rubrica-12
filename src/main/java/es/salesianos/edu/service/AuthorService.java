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
import org.springframework.stereotype.Service;

import com.google.javascript.jscomp.Result;

import es.salesianos.edu.connection.*;
import es.salesianos.edu.model.Author;

@Service
public class AuthorService {
	
	// DEFINE LA RUTA A LA BASE DE DATOS DESDE EN EL PROYECTO
	private static final String jdbcUrl = "jdbc:h2:file:./src/main/resources/BibliotecaDB";
		
	static ConnectionManager manager = new ConnectionH2();

	private static final Logger logger = LogManager.getLogger(AuthorService.class.getName());

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
	
	public boolean insert(Author author) {
		
		boolean isInserted;
		
		if(search(author).isEmpty()){
			// No existe en la bd
			ResultSet resultSet = null;
			PreparedStatement prepareStatement = null;
			Connection conn = manager.open(jdbcUrl);
			
			try{
				prepareStatement = conn.prepareStatement("INSERT INTO AUTORES(NOMBREAUTOR, FECHANACIMIENTO) VALUES(?,?)");
				prepareStatement.setString(1, author.getNameAuthor());
				prepareStatement.setObject(2, author.getDateOfBirth());
				prepareStatement.executeUpdate();
				setFlagMessage("El autor ha sido insertado");
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
			setFlagMessage("El autor ya existe");
			isInserted = false;
		}
		return isInserted;
	}
	
	public List searchAll() {
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Author> AuthorsFoundList = new ArrayList<Author>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES");
			resultSet = prepareStatement.executeQuery();
			Author foundAuthor;
			while(resultSet.next()){
				foundAuthor = new Author();
				foundAuthor.setIdAuthor(resultSet.getInt(1));
				foundAuthor.setNameAuthor(resultSet.getString(2));
				foundAuthor.setDateOfBirth(resultSet.getDate(3));
				AuthorsFoundList.add(foundAuthor);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return AuthorsFoundList;
	}
	
	public List search(Author author) {
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Author> AuthorsFoundList = new ArrayList<Author>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES WHERE NOMBREAUTOR = ?");
			prepareStatement.setString(1, author.getNameAuthor());
			resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				Author foundAuthor = new Author();
				foundAuthor.setIdAuthor(resultSet.getInt(1));
				foundAuthor.setNameAuthor(resultSet.getString(2));
				foundAuthor.setDateOfBirth(resultSet.getDate(3));
				
				AuthorsFoundList.add(foundAuthor);
			}
			
			
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return AuthorsFoundList;
	}
	
	public List partialSearch(Author author) {
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Author> AuthorsFoundList = new ArrayList<Author>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES WHERE NOMBREAUTOR like %?%");
			prepareStatement.setString(1, author.getNameAuthor());
			resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				Author foundAuthor = new Author();
				foundAuthor.setIdAuthor(resultSet.getInt(1));
				foundAuthor.setNameAuthor(resultSet.getString(2));
				foundAuthor.setDateOfBirth(resultSet.getDate(3));
				
				AuthorsFoundList.add(foundAuthor);
			}
			
			
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return AuthorsFoundList;
	}
	
	public List partialSearch(String authorName) {
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List<Author> AuthorsFoundList = new ArrayList<Author>();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES WHERE NOMBREAUTOR like ?");
			prepareStatement.setString(1, "%"+authorName+"%");
			resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				Author foundAuthor = new Author();
				foundAuthor.setIdAuthor(resultSet.getInt(1));
				foundAuthor.setNameAuthor(resultSet.getString(2));
				foundAuthor.setDateOfBirth(resultSet.getDate(3));
				
				AuthorsFoundList.add(foundAuthor);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return AuthorsFoundList;
	}
	
	public Author SearchById(int id) {
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		Author foundAuthor = null;
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES WHERE IDAUTOR = ?");
			prepareStatement.setInt(1, id);
			resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				foundAuthor=new Author();
				foundAuthor.setIdAuthor(resultSet.getInt(1));
				foundAuthor.setNameAuthor(resultSet.getString(2));
				foundAuthor.setDateOfBirth(resultSet.getDate(3));
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet, prepareStatement, conn);
		}
		return foundAuthor;
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
