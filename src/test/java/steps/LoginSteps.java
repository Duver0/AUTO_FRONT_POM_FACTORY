package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.HomePage;
import pages.LoginPage;

public class LoginSteps {

    private static final String VALID_EMAIL = "duver@gmail.com";
    private static final String VALID_PASSWORD = "Duver123--";
    private static final String INVALID_EMAIL = "username@gmail.com";
    private static final String INVALID_PASSWORD = "12345678";
    private static final String EXPECTED_ERROR_MESSAGE = "User not found";
    private static final AtomicInteger WINDOW_SLOT = new AtomicInteger(0);

    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        int slot = resolveWindowSlot();
        java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        GraphicsDevice activeScreen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        for (GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            Rectangle bounds = screen.getDefaultConfiguration().getBounds();
            if (bounds.contains(mouseLocation)) {
                activeScreen = screen;
                break;
            }
        }

        Rectangle screenBounds = activeScreen.getDefaultConfiguration().getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(activeScreen.getDefaultConfiguration());

        int availableX = screenBounds.x + insets.left;
        int availableY = screenBounds.y + insets.top;
        int availableWidth = screenBounds.width - insets.left - insets.right;
        int availableHeight = screenBounds.height - insets.top - insets.bottom;

        int halfWidth = availableWidth / 2;
        int xPosition = availableX + (slot == 0 ? 0 : halfWidth);

        driver.manage().window().setSize(new Dimension(halfWidth, availableHeight));
        driver.manage().window().setPosition(new org.openqa.selenium.Point(xPosition, availableY));

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

    private int resolveWindowSlot() {
        String workerName = System.getProperty("org.gradle.test.worker");
        if (workerName != null && !workerName.isBlank()) {
            String workerNumber = workerName.replaceAll("\\D+", "");
            if (!workerNumber.isBlank()) {
                return Integer.parseInt(workerNumber) % 2;
            }
        }

        return WINDOW_SLOT.getAndIncrement() % 2;
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("the customer is on the sign in page")
    public void theCustomerIsOnTheSignInPage() {
        loginPage.openHomePage();
        loginPage.openSignInForm();
    }

    @When("the customer authenticates with valid credentials")
    public void theCustomerAuthenticatesWithValidCredentials() {
        loginPage.enterUsername(VALID_EMAIL);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();
    }

    @When("the customer authenticates with invalid credentials")
    public void theCustomerAuthenticatesWithInvalidCredentials() {
        loginPage.enterUsername(INVALID_EMAIL);
        loginPage.enterPassword(INVALID_PASSWORD);
        loginPage.clickLoginButton();
    }

    @Then("the customer should access the authenticated area")
    public void theCustomerShouldAccessTheAuthenticatedArea() {
        Assert.assertTrue("Registro button should be visible after successful login",
                homePage.isAuthenticatedAreaVisible());
    }

    @Then("the customer should see an authentication error message")
    public void theCustomerShouldSeeAnAuthenticationErrorMessage() {
        Assert.assertTrue("Error message should be displayed after failed login",
                homePage.isAuthenticationErrorDisplayed());
        Assert.assertEquals("Unexpected login error message",
                EXPECTED_ERROR_MESSAGE,
                homePage.getAuthenticationErrorMessage());
    }
}
