package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class ConsultorioMedicalPanelPage extends BasePage {

    private static final By PANEL_TITLE_LOCATOR = By.xpath("//h1[normalize-space()='Panel de consultorio']");
    private static final By CONSULTORIO_SELECTOR_LOCATOR = By.id("consultorio-selector");
    private static final By NO_AVAILABLE_CONSULTORIO_LOCATOR =
            By.xpath("//p[contains(normalize-space(),'No hay consultorios libres por ahora.')]");
    private static final By COMMAND_ASSIGNMENT_MESSAGE_LOCATOR =
            By.xpath("//p[contains(normalize-space(),'Comando en proceso: asociar')]");
    private static final By CONSULTORIO_STATE_LABEL_LOCATOR =
            By.xpath("//p[normalize-space()='Estado del consultorio']/following-sibling::h2");

    @FindBy(xpath = "//h1[normalize-space()='Panel de consultorio']")
    private WebElement panelTitle;

    @FindBy(id = "consultorio-selector")
    private WebElement consultorioSelector;

    @FindBy(xpath = "//button[normalize-space()='Tomar consultorio']")
    private WebElement takeConsultorioButton;

    @FindBy(xpath = "//p[contains(normalize-space(),'Comando en proceso: asociar')]")
    private WebElement assignmentCommandMessage;

    @FindBy(xpath = "//p[normalize-space()='Estado del consultorio']/following-sibling::h2")
    private WebElement consultorioStateLabel;

    @FindBy(xpath = "//p[span[normalize-space()='Consultorio:']]")
    private WebElement consultorioLabel;

    @FindBy(xpath = "//p[contains(normalize-space(),'No hay consultorios libres por ahora.')]")
    private WebElement noAvailableConsultorioMessage;

    public ConsultorioMedicalPanelPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilPanelIsVisible() {
        waitUntilVisible(panelTitle);
    }

    public boolean isPanelVisible() {
        return isVisible(PANEL_TITLE_LOCATOR);
    }

    public boolean hasAtLeastOneAvailableConsultorio() {
        waitDriver().until(driver ->
                isVisible(CONSULTORIO_SELECTOR_LOCATOR) || isVisible(NO_AVAILABLE_CONSULTORIO_LOCATOR));

        if (isVisible(NO_AVAILABLE_CONSULTORIO_LOCATOR)) {
            return false;
        }

        Select select = new Select(consultorioSelector);
        return !select.getOptions().isEmpty();
    }

    public String selectFirstAvailableConsultorio() {
        waitUntilVisible(consultorioSelector);
        Select select = new Select(consultorioSelector);
        List<WebElement> options = select.getOptions();
        if (options.isEmpty()) {
            throw new IllegalStateException("No hay opciones de consultorio para seleccionar");
        }

        String consultorioId = options.get(0).getAttribute("value");
        select.selectByValue(consultorioId);
        return consultorioId;
    }

    public boolean isConsultorioOptionAvailable(String consultorioId) {
        waitUntilVisible(consultorioSelector);
        Select select = new Select(consultorioSelector);
        return select.getOptions().stream().anyMatch(option -> {
            String optionValue = option.getAttribute("value");
            return consultorioId.equalsIgnoreCase(optionValue);
        });
    }

    public void selectConsultorio(String consultorioId) {
        waitUntilVisible(consultorioSelector);
        new Select(consultorioSelector).selectByValue(consultorioId);
    }

    public void confirmConsultorioAssignment() {
        click(takeConsultorioButton);
    }

    public void waitForAssignmentCommandMessage() {
        waitUntilVisible(assignmentCommandMessage);
    }

    public String readAssignmentCommandMessage() {
        return readText(COMMAND_ASSIGNMENT_MESSAGE_LOCATOR);
    }

    public void waitUntilStateIsConMedicoDisponible() {
        waitDriver().until(driver -> "ConMedicoDisponible".equals(readDomainStateFromUi()));
    }

    public String readConsultorioStateLabel() {
        waitUntilVisible(consultorioStateLabel);
        return readText(CONSULTORIO_STATE_LABEL_LOCATOR);
    }

    public String readDomainStateFromUi() {
        String uiState = readConsultorioStateLabel();
        if ("Sin medico".equalsIgnoreCase(uiState)) {
            return "SinMedico";
        }
        if ("Disponible".equalsIgnoreCase(uiState)) {
            return "ConMedicoDisponible";
        }
        if ("En atencion".equalsIgnoreCase(uiState)) {
            return "EnAtencion";
        }
        if ("No disponible".equalsIgnoreCase(uiState)) {
            return "ConMedicoNoDisponible";
        }
        return uiState;
    }

    public boolean isConsultorioShownInUi(String consultorioId) {
        waitUntilVisible(consultorioLabel);
        String labelText = consultorioLabel.getText().replace("Consultorio:", "").trim();
        return labelText.equalsIgnoreCase(consultorioId);
    }

    public String readNoAvailableConsultorioMessage() {
        waitUntilVisible(noAvailableConsultorioMessage);
        return noAvailableConsultorioMessage.getText().trim();
    }
}