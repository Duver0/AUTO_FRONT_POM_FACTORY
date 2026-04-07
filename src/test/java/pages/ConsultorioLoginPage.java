package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConsultorioLoginPage extends BasePage {

    private static final By AUTHENTICATION_ERROR_LOCATOR = By.cssSelector("p[role='alert']");

    @FindBy(css = "input[placeholder='Email']")
    private WebElement emailField;

    @FindBy(css = "input[type='password']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement signInButton;

    @FindBy(css = "p[role='alert']")
    private WebElement authenticationError;

    public ConsultorioLoginPage(WebDriver driver) {
        super(driver);
    }

    public void openSignInPage() {
        openPath("/signin");
        waitUntilVisible(emailField);
    }

    public void authenticateWith(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(signInButton);
    }

    public void openMedicalPanelPage() {
        openPath("/medico");
    }

    public void waitForDashboardRedirection() {
        waitUntilUrlContains("/dashboard");
    }

    public void waitForMedicalPanelRedirection() {
        waitUntilUrlContains("/medico");
    }

    public void waitForRedirectOutsideMedicalPanel() {
        waitUntilUrlDoesNotContain("/medico");
    }

    public boolean isAuthenticationErrorVisible() {
        return isVisible(AUTHENTICATION_ERROR_LOCATOR);
    }

    public String readAuthenticationError() {
        waitUntilVisible(authenticationError);
        return authenticationError.getText().trim();
    }

    public boolean isInMedicalPanelPath() {
        return currentPath().contains("/medico");
    }
}