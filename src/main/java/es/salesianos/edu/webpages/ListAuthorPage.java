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
import es.salesianos.edu.service.AuthorService;

public class ListAuthorPage extends WebPage {

	private static final long serialVersionUID = -1935854748907274886L;

	@SpringBean
	AuthorService service;

	private static final Logger logger = LogManager.getLogger(ListAuthorPage.class.getName());

	private String currentNameSearch = null;
	private static List<Author> listAuthor = new ArrayList<Author>();

	public ListAuthorPage(PageParameters parameters) {
			
		checkParametersAndFillListBook(parameters);
		initComponents();
	}

	
	public ListAuthorPage() {
		initComponents();
	}

	private void initComponents() {
		addForm();
		addFeedBackPanel();
		addListAuthorView();
	}

	private void addForm() {
		Form form = new Form("formListAuthor", new CompoundPropertyModel(new Author())) {
			
			 @Override
			protected void onSubmit() {
				super.onSubmit();
				checkAuthorNameAndFillParameters(this);
			}

			
		};
		Button allButton = new Button("allbutton") {
			public void onSubmit() {
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("currentSearchType", "all");
				setResponsePage(ListAuthorPage.class, pageParameters);
			}
		};
		Button cancelButton = new Button("cancelbutton") {
			public void onSubmit() {
				listAuthor.clear();
				getRequestCycle().setResponsePage(ListAuthorPage.class);
				FeedbackMessage message;
				message = new FeedbackMessage(this, "Busqueda cancelada", FeedbackMessage.INFO);
				getFeedbackMessages().add(message);
				
			}
		};
		Button returnButton = new Button("returnbutton") {
			public void onSubmit() {
				listAuthor.clear();
				info("return was pressed!");
				//redirect to homepage
				getRequestCycle().setResponsePage(HomePage.class);
				
			}
		};
		
		form.add(allButton);
		form.add(cancelButton);
		form.add(returnButton);

		form.add(new TextField("nameAuthor"));
		add(form);
	}

	private void addFeedBackPanel() {
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
		add(feedbackPanel);
	}

	private void addListAuthorView() {
		//Author author = new Author();// service.newEntity()
		//author.setNameAuthor(currentNameSearch);
		Label mensaje = new Label("empty-list", "Ningun registro disponible");
		
		if (listAuthor.isEmpty())
			add(mensaje.setVisible(true));
		else
			add(mensaje.setVisible(false));
		
		ListView listview = new ListView("author-group", listAuthor) {
				@Override
				protected void populateItem(ListItem item) {
					Author author = (Author) item.getModelObject();
					item.add(new Label("authorName", author.getNameAuthor()));
					item.add(new Label("dateOfBirth", author.getDateOfBirth()));
				}	
		};
		add(listview);
		
	}
	
	
	
	/*  
	 * --------  FUNCIONES EXTRAIDAS/EMPAQUETADAS  ---------- 
	 *
	 */
	
	/**
	 * @param parameters Comprueba los parametros y carga la lista de autores con lo que corresponda
	 */
	private void checkParametersAndFillListBook(PageParameters parameters) {
		if (parameters.get("currentSearchType").toString().equals("all")){
			listAuthor= service.searchAll();
		}
		else{
			currentNameSearch = parameters.get("currentSearchTerm").toString();
			listAuthor= service.partialSearch(currentNameSearch);
		}
		currentNameSearch=null;
	}
	
	
	/**
	 *  Comprueba el nombre del autor a buscar por el usuario y recarga la pagina con los parametros, o muestra mensaje de error
	 */
	private void checkAuthorNameAndFillParameters(Form form) {
		if(((Author)form.getModelObject()).getNameAuthor()!=null){
			PageParameters pageParameters = new PageParameters();
			pageParameters.add("currentSearchType", "find");
			pageParameters.add("currentSearchTerm", ((Author)form.
			getModelObject()).getNameAuthor());
			setResponsePage(ListAuthorPage.class, pageParameters);
		}
		else
			info("No ha introducido ningún nombre");
	}


}
