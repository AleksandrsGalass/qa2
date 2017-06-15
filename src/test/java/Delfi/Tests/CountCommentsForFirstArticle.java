package Delfi.Tests;

import Core.BaseFunctions;
import Delfi.Pages.CommentPage;
import Delfi.Pages.HomePage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.Scanner;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by alexander.galash on 6/2/2017.
 */
public class CountCommentsForFirstArticle {


    private static final String URL = "http://rus.delfi.lv";
    private BaseFunctions baseFunctions = new BaseFunctions();


    @Test
    public void compareCommentsFromHomePageWithArticleComments() throws InterruptedException {

        baseFunctions.goToUrl(URL);
        HomePage homePage = new HomePage(baseFunctions);
            homePage.scrollForFirstArticleFoundByArticleNameLocator();
            int homePageNumberOfComments = homePage.convertToIntegerArticleCommentsFromHomePage();
            CommentPage commentPage = homePage.showArticleComments();
            baseFunctions.closeAddIfPresent();
            int commentPageRegisteredCommentsCount = commentPage.getRegisteredCommentsCount();
            commentPage.selectUnRegisteredCommentsTab();
            int commentPageUnRegisteredCommentsCount = commentPage.getUnregisteredCommentsCount();
            Assert.assertEquals(homePageNumberOfComments, commentPageRegisteredCommentsCount + commentPageUnRegisteredCommentsCount);
            LOGGER.info("Main page comments (" + homePageNumberOfComments + ") = Registered(" + commentPageRegisteredCommentsCount + ") + (" + commentPageUnRegisteredCommentsCount + ") Unregistered");

    }

    @After
    public void closeDriver() {
        baseFunctions.stopDriver();
    }


}
