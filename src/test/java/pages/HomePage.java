package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    @FindBy(xpath = "//a[normalize-space()='Registro']")
    private WebElement authenticatedAreaMarker;

    @FindBy(css = "p[role='alert']")
    private WebElement authenticationErrorMessage;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isAuthenticatedAreaVisible() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(authenticatedAreaMarker))
                .isDisplayed();
    }

    public boolean isAuthenticationErrorDisplayed() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(authenticationErrorMessage))
                .isDisplayed();
    }

    public String getAuthenticationErrorMessage() {
        return authenticationErrorMessage.getText();
    }
}
