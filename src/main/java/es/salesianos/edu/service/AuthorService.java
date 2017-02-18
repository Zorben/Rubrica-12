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
		logger.debug("realizando insercion");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		boolean isInserted;
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES WHERE NOMBREAUTOR = ?");
			prepareStatement.setString(1, author.getNameAuthor());
			resultSet = prepareStatement.executeQuery();
			if (resultSet.getRow()!=0){
				setFlagMessage("El autor ya existe");
				isInserted = false;
			}
			else{
				prepareStatement = conn.prepareStatement("INSERT INTO AUTORES(NOMBREAUTOR, FECHANACIMIENTO, IDLIBRO) VALUES(?,?,1)");
				prepareStatement.setString(1, author.getNameAuthor());
				prepareStatement.setObject(2, author.getDateOfBirth());
				prepareStatement.executeUpdate();
				setFlagMessage("El autor ha sido insertado");
				isInserted = true;	
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet);
			close(prepareStatement);	
		}
		manager.close(conn);
		return isInserted;
	}
	public List searchAll() {
		logger.debug("realizando seleccion");
		ResultSet resultSet = null;
		PreparedStatement prepareStatement = null;
		Connection conn = manager.open(jdbcUrl);
		List AuthorsFoundList = Collections.emptyList();
		try {
			prepareStatement = conn.prepareStatement("SELECT * FROM AUTORES");
			resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				Author foundAuthor = new Author();
				foundAuthor.setNameAuthor(resultSet.getString(2));
				foundAuthor.setDateOfBirth(resultSet.getDate(3));
				
				AuthorsFoundList.add(foundAuthor);
			}
			
			
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			close(resultSet);
			close(prepareStatement);	
		}
		manager.close(conn);
	
		return AuthorsFoundList;
	}
}
