package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//a[normalize-space()='Iniciar sesión']")
    private WebElement signInEntryPoint;

    @FindBy(css = "input[placeholder='Email']")
    private WebElement emailField;

    @FindBy(css = "input[placeholder='Contraseña']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement authenticateButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void openHomePage() {
        driver.get("http://localhost:3001/");
    }

    public void openSignInForm() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(signInEntryPoint))
                .click();
    }

    public void enterUsername(String username) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(emailField));
        emailField.clear();
        emailField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(authenticateButton))
                .click();
    }
}
