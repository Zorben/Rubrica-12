
package es.salesianos.edu.webpages;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.salesianos.edu.model.Author;
import es.salesianos.edu.service.AuthorService;

public class AuthorPage extends WebPage {

	@SpringBean
	AuthorService authorService;

	public AuthorPage() {


		Form form = new Form("formInsertLogin", new CompoundPropertyModel(new Author())) {

			@Override
			protected void onSubmit() {
				super.onSubmit();
				boolean isInserted = authorService.insert((Author) getModelObject());
				FeedbackMessage message;
				if(isInserted){
					message = new FeedbackMessage(this, authorService.getFlagMessage(), FeedbackMessage.INFO);
				} else {
					message = new FeedbackMessage(this, authorService.getFlagMessage(), FeedbackMessage.INFO);
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
		
		
		
		form.add(new Label("nameAuthorLabel", getString("author.name")));
		form.add(new Label("dateOfBirthLabel", getString("date.of.birth")));
		form.add(new RequiredTextField("nameAuthor"));
		DateTextField datetimePicker = new DateTextField("dateOfBirth", "yyyy-MM-dd");
		form.add(datetimePicker);
		returnButton.setDefaultFormProcessing(false);
		form.add(returnButton);
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		add(form);

	}

}
