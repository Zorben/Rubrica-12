package es.salesianos.edu.webpages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.salesianos.edu.model.Author;
import es.salesianos.edu.model.Book;
import es.salesianos.edu.service.AuthorService;
import es.salesianos.edu.service.BookService;

public class ListBookPage extends WebPage {

	private static final long serialVersionUID = -1935854748907274886L;

	@SpringBean
	AuthorService authorService;
	@SpringBean
	BookService bookService;

	private static final Logger logger = LogManager.getLogger(ListBookPage.class.getName());

	private String currentNameSearch = null;
	private static List<Book> listBook = new ArrayList<Book>();

	public ListBookPage(PageParameters parameters) {
			
		checkParametersAndFillListAuthor(parameters);
		initComponents();
	}

	
	public ListBookPage() {
		initComponents();
	}

	private void initComponents() {
		addForm();
		addFeedBackPanel();
		addListBookView();
	}

	private void addForm() {
		Form form = new Form("formListBook", new CompoundPropertyModel(new Book())) {
			
			 @Override
			protected void onSubmit() {
				super.onSubmit();
				checkBookTitleAndFillParameters(this);
			}

			
		};
		Button allButton = new Button("allbutton") {
			public void onSubmit() {
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("currentSearchType", "all");
				setResponsePage(ListBookPage.class, pageParameters);
			}
		};
		Button cancelButton = new Button("cancelbutton") {
			public void onSubmit() {
				listBook.clear();
				getRequestCycle().setResponsePage(ListBookPage.class);
				FeedbackMessage message;
				message = new FeedbackMessage(this, "Busqueda cancelada", FeedbackMessage.INFO);
				getFeedbackMessages().add(message);
				
			}
		};
		Button returnButton = new Button("returnbutton") {
			public void onSubmit() {
				listBook.clear();
				//redirect to homepage
				getRequestCycle().setResponsePage(HomePage.class);
				
			}
		};
		
		form.add(allButton);
		form.add(cancelButton);
		form.add(returnButton);

		form.add(new TextField("title"));
		add(form);
	}

	private void addFeedBackPanel() {
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
		add(feedbackPanel);
	}

	private void addListBookView() {
		
		Label mensaje = new Label("empty-list", "Ningun registro disponible");
		
		if (listBook.isEmpty())
			add(mensaje.setVisible(true));
		else
			add(mensaje.setVisible(false));
		
		ListView listview = new ListView("book-group", listBook) {
				@Override
				protected void populateItem(ListItem item) {
					Book book = (Book) item.getModelObject();
					item.add(new Label("isbn", book.getIsbn()));
					item.add(new Label("title", book.getTitle()));
					item.add(new Label("authorname", book.getAuthor().getNameAuthor()));
				}	
		};
		add(listview);
		
	}
	
	
	
	/*  
	 * --------  FUNCIONES EXTRAIDAS/EMPAQUETADAS  ---------- 
	 *
	 */
	
	/**
	 * @param parameters Comprueba los parametros y carga la lista de libros con lo que corresponda
	 */
	private void checkParametersAndFillListAuthor(PageParameters parameters) {
		if (parameters.get("currentSearchType").toString().equals("all")){
			listBook= bookService.searchAll();
		}
		else{
			currentNameSearch = parameters.get("currentSearchTerm").toString();
			listBook= bookService.partialSearch(currentNameSearch);
		}
		currentNameSearch=null;
	}
	
	
	/**
	 *  Comprueba el nombre del libro a buscar por el usuario y recarga la pagina con los parametros, o muestra mensaje de error
	 */
	private void checkBookTitleAndFillParameters(Form form) {
		if(((Book)form.getModelObject()).getTitle()!=null){
			PageParameters pageParameters = new PageParameters();
			pageParameters.add("currentSearchType", "find");
			pageParameters.add("currentSearchTerm", ((Book)form.
			getModelObject()).getTitle());
			setResponsePage(ListBookPage.class, pageParameters);
		}
		else
			info("No ha introducido ningún título");
	}


}
