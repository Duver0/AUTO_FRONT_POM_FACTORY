package pages;

import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    public boolean isLoginErrorDisplayed() {
        initElements(getDriver());
        WebElementFacade loginErrorMessage = find(By.cssSelector("p[role='alert']"));
        return loginErrorMessage.waitUntilVisible().isVisible();
    }

    public String getErrorMessageText() {
        initElements(getDriver());
        WebElementFacade loginErrorMessage = find(By.cssSelector("p[role='alert']"));
        return loginErrorMessage.getText();
    }

    public boolean isRegistroButtonVisibleForAuthenticatedUser() {
        initElements(getDriver());
        WebElementFacade registroNavLink = find(By.xpath("//a[normalize-space()='Registro']"));
        return registroNavLink.waitUntilVisible().isVisible();
    }
}
