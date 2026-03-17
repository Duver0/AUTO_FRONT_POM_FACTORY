package pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class HomePage extends PageObject {

    @FindBy(xpath = "//a[normalize-space()='Registro']")
    private WebElement authenticatedAreaMarker;

    @FindBy(css = "p[role='alert']")
    private WebElement authenticationErrorMessage;

    public boolean isAuthenticatedAreaVisible() {
        setImplicitTimeout(10, java.time.temporal.ChronoUnit.SECONDS);
        boolean isVisible = element(authenticatedAreaMarker).waitUntilVisible().isDisplayed();
        resetImplicitTimeout();
        return isVisible;
    }

    public boolean isAuthenticationErrorDisplayed() {
        setImplicitTimeout(10, java.time.temporal.ChronoUnit.SECONDS);
        boolean isVisible = element(authenticationErrorMessage).waitUntilVisible().isDisplayed();
        resetImplicitTimeout();
        return isVisible;
    }

    public String getAuthenticationErrorMessage() {
        return authenticationErrorMessage.getText();
    }
}
