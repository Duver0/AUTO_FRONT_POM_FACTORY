package pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElementFacade usernameField;

    @FindBy(id = "password")
    private WebElementFacade passwordField;

    @FindBy(id = "login-button")
    private WebElementFacade loginButton;

    public void navigateTo() {
        initElements(getDriver());
        openAt("/");
    }

    public void enterCredentials(String username, String password) {
        usernameField.type(username);
        passwordField.type(password);
    }

    public void submitLoginForm() {
        loginButton.click();
    }
}
