package es.salesianos.edu.webpages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class HomePage extends WebPage {
	public HomePage() {
		BookmarkablePageLink bookmarkablePageLink1 = new BookmarkablePageLink("linkAuthorForm", AuthorPage.class);
		BookmarkablePageLink bookmarkablePageLink2 = new BookmarkablePageLink("linkListAuthor",
				ListAuthorPage.class);
		add(bookmarkablePageLink1);
		add(bookmarkablePageLink2);
		
		
		
		BookmarkablePageLink bookmarkablePageLink3 = new BookmarkablePageLink("linkBookForm", BookPage.class);
		/*BookmarkablePageLink bookmarkablePageLink4 = new BookmarkablePageLink("linkListBook",
				ListBookPage.class);*/
		add(bookmarkablePageLink3);
		//add(bookmarkablePageLink4);
	}
}
