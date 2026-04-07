package pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.time.temporal.ChronoUnit;
import utils.ImplicitTimeoutUtils;

public class HomePage extends PageObject {

    @FindBy(xpath = "//a[normalize-space()='Registro']")
    private WebElement authenticatedAreaMarker;

    @FindBy(css = "p[role='alert']")
    private WebElement authenticationErrorMessage;

    public boolean isAuthenticatedAreaVisible() {
        return ImplicitTimeoutUtils.supplyWithDefaultTimeout(
                seconds -> setImplicitTimeout(seconds, ChronoUnit.SECONDS),
                () -> element(authenticatedAreaMarker).waitUntilVisible().isDisplayed(),
                this::resetImplicitTimeout
        );
    }

    public boolean isAuthenticationErrorDisplayed() {
        return ImplicitTimeoutUtils.supplyWithDefaultTimeout(
                seconds -> setImplicitTimeout(seconds, ChronoUnit.SECONDS),
                () -> element(authenticationErrorMessage).waitUntilVisible().isDisplayed(),
                this::resetImplicitTimeout
        );
    }

    public String getAuthenticationErrorMessage() {
        return authenticationErrorMessage.getText();
    }
}
