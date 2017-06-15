package Delfi.Pages;

import Core.BaseFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by alexander.galash on 6/8/2017.
 */
public class CommentPage {

    private static final By REGISTERED_COMMENTS_TAB = By.xpath("//a[@class = 'comment-thread-switcher-list-a comment-thread-switcher-list-a-reg']");
    private static final By UNREGISTERED_COMMENTS_TAB = By.xpath("//a[@class='comment-thread-switcher-list-a comment-thread-switcher-list-a-anon']");
    private static final By REGISTERED_COMMENTS_LIST_LOCATOR = By.xpath("//div[contains(@class,'comment-avatar-registered')]");
    private static final By UNREGISTERED_NOT_DELETED_COMMENTS_LIST_LOCATOR = By.xpath("//div[not(contains(text(),'Комментарий удален'))]/ancestor::div[contains(@class,'comment-avatar-anonymous')]");


    private BaseFunctions baseFunctions;

    CommentPage(BaseFunctions baseFunctions) {
        this.baseFunctions = baseFunctions;
        LOGGER.info("Comment Page is opened");
    }

    public void selectUnRegisteredCommentsTab() {
        baseFunctions.click(UNREGISTERED_COMMENTS_TAB);
        LOGGER.info("Unreg tab is opened");
    }


    public int getRegisteredCommentsCount() throws InterruptedException {
        boolean successState = false;
        int commentCount = 0;
        while (!successState) {
            baseFunctions.expandHiddenCommentsIfAny();
            commentCount += getListOfRegisteredComment().size();
            if (baseFunctions.pagingNeeded()) {
                baseFunctions.doPaging();
            } else {
                successState = true;
            }
        }
        return commentCount;
    }


    public int getUnregisteredCommentsCount() throws InterruptedException {
        boolean successState = false;
        int commentCount = 0;
        while (!successState) {
            baseFunctions.expandHiddenCommentsIfAny();
            commentCount += getListOfUnRegisteredComment().size();
            if (baseFunctions.pagingNeeded()) {
                Thread.sleep(20000);           //to check on UI if all hidden are expanded
                baseFunctions.doPaging();
            } else {
                successState = true;
            }
        }
        return commentCount;
    }

    private List<WebElement> getListOfRegisteredComment() {
        return baseFunctions.findElements(REGISTERED_COMMENTS_LIST_LOCATOR);
    }

    private List<WebElement> getListOfUnRegisteredComment() {
        return baseFunctions.findElements(UNREGISTERED_NOT_DELETED_COMMENTS_LIST_LOCATOR);
    }
}
