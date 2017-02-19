
package es.salesianos.edu.webpages;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.fasterxml.jackson.annotation.JsonFormat.Value;

import es.salesianos.edu.model.Author;
import es.salesianos.edu.model.Book;
import es.salesianos.edu.service.AuthorService;
import es.salesianos.edu.service.BookService;

public class BookPage extends WebPage {

	@SpringBean
	BookService bookService;
	@SpringBean
	AuthorService authorService;

	public BookPage() {


		Form form = new Form("formInsertLogin", new CompoundPropertyModel(new Book())) {

			@Override
			protected void onSubmit() {
				super.onSubmit();
				boolean isInserted = bookService.insert((Book)getModelObject());
				FeedbackMessage message;
				if(isInserted){
					message = new FeedbackMessage(this, bookService.getFlagMessage(), FeedbackMessage.INFO);
				} else {
					message = new FeedbackMessage(this, bookService.getFlagMessage(), FeedbackMessage.INFO);
				}
				getFeedbackMessages().add(message);
			}
		};
		
		Button returnButton = new Button("returnbutton") {
			public void onSubmit() {
				//redirect to homepage
				getRequestCycle().setResponsePage(HomePage.class);
				
			}
		};
		
		form.add(new Label("isbnLabel", getString("book.isbn")));
		form.add(new Label("titleLabel", getString("book.title")));
		form.add(new Label("authorLabel", getString("book.author")));
		form.add(new RequiredTextField("isbn"));
		form.add(new RequiredTextField("title"));
		form.add(new DropDownChoice<Author>("author", authorService.searchAll(), new ChoiceRenderer<Author>("nameAuthor", "idAuthor")));
		returnButton.setDefaultFormProcessing(false);
		form.add(returnButton);
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		add(form);

	}

}
