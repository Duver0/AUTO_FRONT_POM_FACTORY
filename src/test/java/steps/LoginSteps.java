package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;

public class LoginSteps {

    @Managed
    private WebDriver driver;

    private LoginPage loginPage;
    private HomePage homePage;

    private static final String VALID_EMAIL = "duver@gmail.com";
    private static final String VALID_PASSWORD = "Duver123--";
    private static final String INVALID_EMAIL = "username@gmail.com";
    private static final String INVALID_PASSWORD = "12345678";
    private static final String EXPECTED_ERROR_MESSAGE = "User not found";

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

    @Then("the customer should access the authenticated area")
    public void theCustomerShouldAccessTheAuthenticatedArea() {
        Assert.assertTrue("The authenticated area should be visible", homePage.isAuthenticatedAreaVisible());
    }

    @When("the customer authenticates with invalid credentials")
    public void theCustomerAuthenticatesWithInvalidCredentials() {
        loginPage.enterUsername(INVALID_EMAIL);
        loginPage.enterPassword(INVALID_PASSWORD);
        loginPage.clickLoginButton();
    }

    @Then("the customer should see an authentication error message")
    public void theCustomerShouldSeeAnAuthenticationErrorMessage() {
        Assert.assertTrue("The error message should be displayed", homePage.isAuthenticationErrorDisplayed());
        Assert.assertEquals("The error message text does not match", EXPECTED_ERROR_MESSAGE, homePage.getAuthenticationErrorMessage());
    }
}
