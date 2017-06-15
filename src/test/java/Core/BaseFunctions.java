package Core;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by alexander.galash on 5/13/2017.
 */
public class BaseFunctions {

    private WebDriver driver;
    private static final String CHROME_DRIVER_LOCATION = "C:\\Selenium\\chromedriver.exe";
    private static final By EXPAND_MORE_COMMENTS_BUTTON_LOCATOR = By.xpath("//div[not(contains(@style,'display: none;')) and contains(@class,'load-more-comments-btn')]");
    private static final By EXPAND_MORE_LOADING_LABEL_LOCATOR = By.xpath("//div[contains(@class, 'loading-more-comments-label')]");
    private static final By ADVERTISEMENT = By.xpath("//div[contains(@class, 'ado-close-cross')]");
    private static final By NEXT_PAGE_ARROW_LOCATOR = By.xpath("//*[contains(@class,'comments-pager-arrow-last')]");
    private static final Logger LOGGER = Logger.getLogger(BaseFunctions.class);


    public BaseFunctions() {

        LOGGER.info("Setting chromedriver location:" + CHROME_DRIVER_LOCATION);
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_LOCATION);

        LOGGER.info("Starting ChromeDriver");
        this.driver = new ChromeDriver();

        LOGGER.info("Maximize browser window size");
        driver.manage().window().maximize();

        LOGGER.info("Implicit wait = 5 sec ");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void goToUrl(String url) {
        LOGGER.info("User goes to: " + url);
        driver.get(url);
    }


    public void stopDriver() {
        LOGGER.info("Driver Stoped");
        driver.close();
    }

    public void click(By by) {
        waitForElement(by, 1);
        driver.findElement(by).click();
    }

    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    public boolean isElementPresent(By by) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            driver.findElement(by);
            return true;
        } catch (NotFoundException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }
    }

    private void waitForElement(By by, long sec) {
        WebDriverWait wait = new WebDriverWait(driver, sec);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException except) {
            System.out.println("Wait for element" + by + "timed out");
        }
    }

    public void searchForElementWhileScrolling(By by) throws InterruptedException {
        waitForElement(by, 1);
        if (!isElementPresent(by)) {
            for (int i = 1; i <= 6; i++) {
                scrollPageDown();
                if (isElementPresent(by)) {
                    LOGGER.info("Article found");
                    break;
                }
            }
        }
        LOGGER.info("Article not found on the page yet");

    }


    private void scrollPageToTheTop() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.HOME).perform();
        LOGGER.info("Top of the page");
    }

    private void scrollToTheElement(By by) {
        WebElement element = driver.findElement(by);
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
    }

    private void scrollPageDown() throws InterruptedException {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.PAGE_DOWN).perform();
        LOGGER.info("Page down key pressed ");
    }


    public String getText(By by) {
        waitForElement(by, 1);
        return driver.findElement(by).getText();
    }

    public void closeAddIfPresent() {
        waitForElement(ADVERTISEMENT, 5);
        if (isElementPresent(ADVERTISEMENT)) {
            try {
                click(ADVERTISEMENT);
                LOGGER.info("Add closed");
            } catch (TimeoutException e) {
                System.out.println("No add this time");
            }

        }
    }

    public void expandHiddenCommentsIfAny() throws InterruptedException {
        if (isElementPresent(EXPAND_MORE_COMMENTS_BUTTON_LOCATOR)) {
            WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
            List<WebElement> elements = driver.findElements(EXPAND_MORE_COMMENTS_BUTTON_LOCATOR);
            scrollToTheElement(EXPAND_MORE_COMMENTS_BUTTON_LOCATOR);
            try {
                LOGGER.info("Found " + elements.size() + " hidden buttons on the page ");
                click(EXPAND_MORE_COMMENTS_BUTTON_LOCATOR);
                webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(EXPAND_MORE_LOADING_LABEL_LOCATOR));
                LOGGER.info("Loading of hidden comments finished");
                expandHiddenCommentsIfAny();
            } catch (ElementNotVisibleException e) {
                System.out.println(" No hidden comments found on a page");
            }

        }
    }

    public void doPaging() {
        scrollPageToTheTop();
        click(NEXT_PAGE_ARROW_LOCATOR);
        LOGGER.info("Next page");
    }

    public boolean pagingNeeded() {
        return isElementPresent(NEXT_PAGE_ARROW_LOCATOR);
    }
}


