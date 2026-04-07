package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class ConsultorioSignUpPage extends BasePage {

    @FindBy(css = "input[placeholder='Nombre']")
    private WebElement nameField;

    @FindBy(css = "input[placeholder='Email']")
    private WebElement emailField;

    @FindBy(css = "select[aria-label='Tipo de usuario']")
    private WebElement roleSelector;

    @FindBy(css = "input[type='password']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement registerButton;

    public ConsultorioSignUpPage(WebDriver driver) {
        super(driver);
    }

    public void openSignUpPage() {
        openPath("/signup");
        waitUntilVisible(nameField);
    }

    public void registerUser(String name, String email, String password, String roleValue) {
        type(nameField, name);
        type(emailField, email);
        selectRole(roleValue);
        type(passwordField, password);
        click(registerButton);
    }

    public void waitForRedirectToSignInPage() {
        waitUntilUrlContains("/signin");
    }

    private void selectRole(String roleValue) {
        waitUntilVisible(roleSelector);
        new Select(roleSelector).selectByValue(roleValue);
    }
}