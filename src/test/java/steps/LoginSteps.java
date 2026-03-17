package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
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

    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
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
