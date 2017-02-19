package es.salesianos.edu.model;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {

	private int isbn;
	private String title;
	private Author author;
	
	public int getIsbn() {
		return isbn;
	}
	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	

	

}
