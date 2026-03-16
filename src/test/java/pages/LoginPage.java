package pages;

import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    public void navigateTo() {
        initElements(getDriver());
        openUrl("http://localhost:3001/");
    }

    public void clickSignInFromNavbar() {
        initElements(getDriver());
        WebElementFacade signInNavButton = find(By.xpath("//a[normalize-space()='Iniciar sesión']"));
        signInNavButton.waitUntilClickable().click();
    }

    public void enterCredentials(String email, String password) {
        WebElementFacade emailField = find(By.cssSelector("input[placeholder='Email']"));
        WebElementFacade passwordField = find(By.cssSelector("input[placeholder='Contraseña']"));
        emailField.waitUntilVisible().type(email);
        passwordField.type(password);
    }

    public void submitLoginForm() {
        WebElementFacade signInSubmitButton = find(By.cssSelector("button[type='submit']"));
        signInSubmitButton.waitUntilClickable().click();
    }
}
