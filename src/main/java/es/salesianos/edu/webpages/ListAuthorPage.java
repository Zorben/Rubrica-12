package es.salesianos.edu.webpages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private List<Author> listAuthor = new ArrayList<Author>();

	public ListAuthorPage(PageParameters parameters) {
		currentNameSearch = parameters.get("currentSearchTerm").toString();
		logger.debug("Cargando la pagina con el parametro " + currentNameSearch);
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
			/*
			 @Override
			protected void onSubmit() {
				super.onSubmit();
				listAuthor.clear();
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("currentSearchTerm", ((Author)
				getModelObject()).getNameAuthor());
				setResponsePage(ListAuthorPage.class, pageParameters);
			}*/
		};
		Button okButton = new Button("okbutton") {
			public void onSubmit() {
				listAuthor.clear();
				info("OK was pressed!");
				
				
			}
		};
		Button cancelButton = new Button("cancelbutton") {
			public void onSubmit() {
				listAuthor.clear();
				info("cancel was pressed!");
				
				
			}
		};
		Button returnButton = new Button("returnbutton") {
			public void onSubmit() {
				listAuthor.clear();
				info("return was pressed!");
				getRequestCycle().setResponsePage(HomePage.class);
				
			}
		};
		
		form.add(okButton);
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
		listAuthor = service.searchAll();
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


}
