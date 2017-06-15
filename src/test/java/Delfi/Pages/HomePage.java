package Delfi.Pages;


import Core.BaseFunctions;
import org.openqa.selenium.By;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by alexander.galash on 4/30/2017.
 */
public class HomePage {

    private static String articleName =  "РАПУО";
    private static String firstArticleFoundByArticleName = "[@class = 'top2012-title'][contains(text(), '" + articleName + "')]";  // top2012-title     headline-title
    private static By firstArticleFoundByArticleNameLocator = By.xpath("(//*" + firstArticleFoundByArticleName + ")[1]");
    private static By numberOfCommentsForArticleFound = By.xpath("(//*" + firstArticleFoundByArticleName + ")[1]/following-sibling::a[@class='comment-count']");

    private BaseFunctions basefunctions;


    public HomePage(BaseFunctions baseFunctions) {
        this.basefunctions = baseFunctions;
        LOGGER.info("Home Page is opened");
    }

    public CommentPage showArticleComments() {
        basefunctions.click(numberOfCommentsForArticleFound);
        return new CommentPage(basefunctions);

    }

    public int convertToIntegerArticleCommentsFromHomePage() {
        if (articleIsCommented()) {
            String countString = basefunctions.getText(numberOfCommentsForArticleFound);
            Integer countInt = Integer.parseInt(countString.substring(1, countString.length() - 1));
            LOGGER.info("Article comment count on home page " + countInt);
            return countInt;
        }
        LOGGER.info("No comments for the article ");
        return 0;                                                                           // wtf to do?

    }
    public void scrollForFirstArticleFoundByArticleNameLocator() throws InterruptedException {
        basefunctions.searchForElementWhileScrolling(firstArticleFoundByArticleNameLocator);

    }
    private boolean articleIsCommented() {
        return basefunctions.isElementPresent(numberOfCommentsForArticleFound);
    }

    }


