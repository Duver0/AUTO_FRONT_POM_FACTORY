package pages;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.time.temporal.ChronoUnit;
import utils.ImplicitTimeoutUtils;

public class LoginPage extends PageObject {

    private static final String SERENITY_CONFIG_FILE = "serenity.conf";
    private static final String BASE_URL_KEY = "webdriver.base.url";

    @FindBy(xpath = "//a[normalize-space()='Iniciar sesión']")
    private WebElement signInEntryPoint;

    @FindBy(css = "input[placeholder='Email']")
    private WebElement emailField;

    @FindBy(css = "input[placeholder='Contraseña']")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement authenticateButton;

    public void openHomePage() {
        openAt(baseUrlFromSerenityConfig());
    }

    private String baseUrlFromSerenityConfig() {
        Config config = ConfigFactory.parseFile(new File(SERENITY_CONFIG_FILE)).resolve();
        if (!config.hasPath(BASE_URL_KEY)) {
            throw new IllegalStateException("webdriver.base.url is not configured in serenity.conf");
        }

        String baseUrl = config.getString(BASE_URL_KEY);
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("webdriver.base.url is empty in serenity.conf");
        }

        return baseUrl;
    }

    public void openSignInForm() {
        ImplicitTimeoutUtils.executeWithDefaultTimeout(
                seconds -> setImplicitTimeout(seconds, ChronoUnit.SECONDS),
                () -> element(signInEntryPoint).click(),
                this::resetImplicitTimeout
        );
    }

    public void authenticateWith(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public void enterUsername(String username) {
        ImplicitTimeoutUtils.executeWithDefaultTimeout(
                seconds -> setImplicitTimeout(seconds, ChronoUnit.SECONDS),
                () -> {
                    element(emailField).waitUntilVisible().clear();
                    typeInto(emailField, username);
                },
                this::resetImplicitTimeout
        );
    }

    public void enterPassword(String password) {
        typeInto(passwordField, password);
    }

    public void clickLoginButton() {
        ImplicitTimeoutUtils.executeWithDefaultTimeout(
                seconds -> setImplicitTimeout(seconds, ChronoUnit.SECONDS),
                () -> element(authenticateButton).click(),
                this::resetImplicitTimeout
        );
    }
}
