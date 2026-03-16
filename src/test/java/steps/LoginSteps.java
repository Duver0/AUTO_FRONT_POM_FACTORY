package steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.HomePage;
import pages.LoginPage;

public class LoginSteps {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "password123";
    private static final String INVALID_USERNAME = "unknown_user";
    private static final String INVALID_PASSWORD = "wrong_password";

    private LoginPage loginPage;
    private HomePage homePage;

    @Before
    public void initPages() {
        loginPage = new LoginPage();
        homePage = new HomePage();
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginPage.navigateTo();
    }

    @When("the user enters a valid username and password")
    public void theUserEntersAValidUsernameAndPassword() {
        loginPage.enterCredentials(VALID_USERNAME, VALID_PASSWORD);
    }

    @When("the user enters an invalid username and password")
    public void theUserEntersAnInvalidUsernameAndPassword() {
        loginPage.enterCredentials(INVALID_USERNAME, INVALID_PASSWORD);
    }

    @And("the user submits the login form")
    public void theUserSubmitsTheLoginForm() {
        loginPage.submitLoginForm();
    }

    @Then("the user should be redirected to the dashboard")
    public void theUserShouldBeRedirectedToTheDashboard() {
        Assert.assertTrue("Dashboard should be visible after successful login",
                homePage.isDashboardVisible());
    }

    @Then("the user should see a login error message")
    public void theUserShouldSeeALoginErrorMessage() {
        Assert.assertTrue("Error message should be displayed after failed login",
                homePage.isLoginErrorDisplayed());
        Assert.assertFalse("Error message text should not be empty",
                homePage.getErrorMessageText().isEmpty());
    }
}
