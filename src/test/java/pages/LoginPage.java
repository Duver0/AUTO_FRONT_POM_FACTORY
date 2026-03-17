package pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class LoginPage extends PageObject {

    @FindBy(xpath = "//a[normalize-space()='Iniciar sesión']")
    private WebElement signInEntryPoint;

    @FindBy(css = "input[placeholder='Email']")
    private WebElement emailField;

    @FindBy(css = "input[placeholder='Contraseña']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement authenticateButton;

    public void openHomePage() {
        openAt("http://localhost:3001/");
    }

    public void openSignInForm() {
        setImplicitTimeout(10, java.time.temporal.ChronoUnit.SECONDS);
        element(signInEntryPoint).click();
        resetImplicitTimeout();
    }

    public void enterUsername(String username) {
        setImplicitTimeout(10, java.time.temporal.ChronoUnit.SECONDS);
        element(emailField).waitUntilVisible().clear();
        typeInto(emailField, username);
        resetImplicitTimeout();
    }

    public void enterPassword(String password) {
        typeInto(passwordField, password);
    }

    public void clickLoginButton() {
        setImplicitTimeout(10, java.time.temporal.ChronoUnit.SECONDS);
        element(authenticateButton).click();
        resetImplicitTimeout();
    }
}
