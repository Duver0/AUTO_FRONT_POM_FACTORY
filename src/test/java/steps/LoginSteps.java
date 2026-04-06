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

    @Given("the customer is on the sign in page")
    public void theCustomerIsOnTheSignInPage() {
        loginPage.openHomePage();
        loginPage.openSignInForm();
    }

    @When("the customer authenticates with email {string} and password {string}")
    public void theCustomerAuthenticatesWithEmailAndPassword(String email, String password) {
        loginPage.authenticateWith(email, password);
    }

    @Then("the customer should access the authenticated area")
    public void theCustomerShouldAccessTheAuthenticatedArea() {
        Assert.assertTrue("The authenticated area should be visible", homePage.isAuthenticatedAreaVisible());
    }

    @Then("the customer should see an authentication error message {string}")
    public void theCustomerShouldSeeAnAuthenticationErrorMessage(String expectedErrorMessage) {
        Assert.assertTrue("The error message should be displayed", homePage.isAuthenticationErrorDisplayed());
        Assert.assertEquals("The error message text does not match", expectedErrorMessage, homePage.getAuthenticationErrorMessage());
    }
}
