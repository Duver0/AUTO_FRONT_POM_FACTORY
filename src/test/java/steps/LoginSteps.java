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

    private static final String VALID_EMAIL = "duver@gmail.com";
    private static final String VALID_PASSWORD = "Duver123--";
    private static final String INVALID_EMAIL = "username@gmail.com";
    private static final String INVALID_PASSWORD = "12345678";
    private static final String EXPECTED_ERROR_MESSAGE = "User not found";

    private LoginPage loginPage;
    private HomePage homePage;

    @Before
    public void initPages() {
        loginPage = new LoginPage();
        homePage = new HomePage();
    }

    @Before("@positive_login")
    public void placePositiveFlowWindow() {
        loginPage.setWindowBounds(0, 0, 683, 768);
    }

    @Before("@negative_login")
    public void placeNegativeFlowWindow() {
        loginPage.setWindowBounds(683, 0, 683, 768);
    }

    @Given("the user opens the application in the home page")
    public void theUserOpensTheApplicationInTheHomePage() {
        loginPage.navigateTo();
    }

    @When("the user clicks the sign in button")
    public void theUserClicksTheSignInButton() {
        loginPage.clickSignInFromNavbar();
    }

    @When("the user enters invalid credentials")
    public void theUserEntersInvalidCredentials() {
        loginPage.enterCredentials(INVALID_EMAIL, INVALID_PASSWORD);
    }

    @When("the user enters valid credentials")
    public void theUserEntersValidCredentials() {
        loginPage.enterCredentials(VALID_EMAIL, VALID_PASSWORD);
    }

    @And("the user submits the login form")
    public void theUserSubmitsTheLoginForm() {
        loginPage.submitLoginForm();
    }

    @Then("the user should see the Registro button as authenticated user")
    public void theUserShouldSeeTheRegistroButtonAsAuthenticatedUser() {
        Assert.assertTrue("Registro button should be visible after successful login",
                homePage.isRegistroButtonVisibleForAuthenticatedUser());
    }

    @Then("the user should see the error message User not found")
    public void theUserShouldSeeTheErrorMessageUserNotFound() {
        Assert.assertTrue("Error message should be displayed after failed login",
                homePage.isLoginErrorDisplayed());
        Assert.assertEquals("Unexpected login error message",
                EXPECTED_ERROR_MESSAGE,
                homePage.getErrorMessageText());
    }
}
