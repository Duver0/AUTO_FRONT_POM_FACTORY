package pages;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    private static final String SERENITY_CONFIG_FILE = "serenity.conf";
    private static final String BASE_URL_KEY = "webdriver.base.url";
    private static final String DEFAULT_BASE_URL = "http://localhost:3001/";
    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(20);

    protected final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        this.baseUrl = resolveBaseUrl();
        PageFactory.initElements(driver, this);
    }

    protected void openPath(String path) {
        driver.get(buildUrl(path));
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void type(WebElement element, String value) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        visibleElement.clear();
        visibleElement.sendKeys(value);
    }

    protected void waitUntilVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitUntilVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitUntilUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected void waitUntilUrlDoesNotContain(String fragment) {
        wait.until(webDriver -> !webDriver.getCurrentUrl().contains(fragment));
    }

    protected boolean isVisible(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return elements.stream().anyMatch(WebElement::isDisplayed);
    }

    protected String readText(By locator) {
        waitUntilVisible(locator);
        return driver.findElement(locator).getText().trim();
    }

    protected String currentPath() {
        try {
            return new URI(driver.getCurrentUrl()).getPath();
        } catch (URISyntaxException exception) {
            return driver.getCurrentUrl();
        }
    }

    protected WebDriverWait waitDriver() {
        return wait;
    }

    public void clearBrowserSession() {
        driver.manage().deleteAllCookies();
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
        }
    }

    private String buildUrl(String path) {
        if (path == null || path.isBlank() || "/".equals(path)) {
            return baseUrl;
        }

        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }

        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        return baseUrl + normalizedPath;
    }

    private String resolveBaseUrl() {
        String fromConfig = baseUrlFromSerenityConfig();
        if (fromConfig != null && !fromConfig.isBlank()) {
            return normalizeBaseUrl(fromConfig);
        }

        String fromSystemProperty = System.getProperty(BASE_URL_KEY);
        if (fromSystemProperty != null && !fromSystemProperty.isBlank()) {
            return normalizeBaseUrl(fromSystemProperty);
        }

        String fromEnvironment = System.getenv("WEBDRIVER_BASE_URL");
        if (fromEnvironment != null && !fromEnvironment.isBlank()) {
            return normalizeBaseUrl(fromEnvironment);
        }

        return DEFAULT_BASE_URL;
    }

    private String baseUrlFromSerenityConfig() {
        try {
            Config config = ConfigFactory.parseFile(new File(SERENITY_CONFIG_FILE)).resolve();
            if (config.hasPath(BASE_URL_KEY)) {
                return config.getString(BASE_URL_KEY);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String normalizeBaseUrl(String rawBaseUrl) {
        return rawBaseUrl.endsWith("/") ? rawBaseUrl : rawBaseUrl + "/";
    }
}