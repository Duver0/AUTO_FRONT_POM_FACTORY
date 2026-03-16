package pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage extends PageObject {

    protected void initElements(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
