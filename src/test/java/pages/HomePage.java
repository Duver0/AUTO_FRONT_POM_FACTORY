package pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

public class HomePage extends BasePage {

    @FindBy(css = ".dashboard-title")
    private WebElementFacade dashboardTitle;

    @FindBy(css = ".error-message")
    private WebElementFacade errorMessage;

    public boolean isDashboardVisible() {
        initElements(getDriver());
        return dashboardTitle.isVisible();
    }

    public boolean isLoginErrorDisplayed() {
        initElements(getDriver());
        return errorMessage.isVisible();
    }

    public String getErrorMessageText() {
        return errorMessage.getText();
    }
}
