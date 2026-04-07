package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.ConsultorioLoginPage;
import pages.ConsultorioMedicalPanelPage;
import pages.ConsultorioPreconditionsHelper;
import pages.ConsultorioSignUpPage;
import utils.TestIdentityUtils;

public class GestionConsultorioSteps {

    @Managed
    private WebDriver driver;

    private ConsultorioSignUpPage signUpPage;
    private ConsultorioLoginPage loginPage;
    private ConsultorioMedicalPanelPage medicalPanelPage;
    private ConsultorioPreconditionsHelper preconditionsHelper;

    private String medicoEmail;
    private String medicoPassword;
    private String nonMedicoEmail;
    private String nonMedicoPassword;
    private String availableConsultorioId;

    @Before
    public void setUp() {
        signUpPage = new ConsultorioSignUpPage(driver);
        loginPage = new ConsultorioLoginPage(driver);
        medicalPanelPage = new ConsultorioMedicalPanelPage(driver);
        preconditionsHelper = new ConsultorioPreconditionsHelper();
    }

    @After("@gestion_consultorio")
    public void tearDown() {
        try {
            if (preconditionsHelper != null) {
                preconditionsHelper.releaseTrackedTestmailConsultorios();
            }
        } finally {
            if (loginPage != null) {
                loginPage.clearBrowserSession();
            }
        }
    }

    @Given("existe un medico registrado con rol {string} y credenciales validas")
    public void existeUnMedicoRegistradoConRolYCredencialesValidas(String role) {
        Assert.assertEquals("El rol para esta precondicion debe ser medico", "medico", role);

        medicoEmail = TestIdentityUtils.buildUniqueEmail("medico");
        medicoPassword = preconditionsHelper.buildStrongPassword();
        String medicoName = TestIdentityUtils.buildUniqueName("medico");

        signUpPage.openSignUpPage();
        signUpPage.registerUser(medicoName, medicoEmail, medicoPassword, role);
        signUpPage.waitForRedirectToSignInPage();
        preconditionsHelper.registerTestMedicoCredentials(medicoEmail, medicoPassword);
    }

    @Given("existe al menos un consultorio en estado SinMedico disponible para ser tomado")
    public void existeAlMenosUnConsultorioEnEstadoSinMedicoDisponibleParaSerTomado() {
        availableConsultorioId = preconditionsHelper.findAvailableConsultorioId(medicoEmail, medicoPassword);
        Assert.assertNotNull("Debe existir un consultorio SinMedico para ejecutar el escenario", availableConsultorioId);
        Assert.assertFalse("El consultorio disponible no puede ser vacio", availableConsultorioId.isBlank());
    }

    @When("el medico navega a la pantalla de login")
    public void elMedicoNavegaALaPantallaDeLogin() {
        loginPage.openSignInPage();
    }

    @When("el medico ingresa sus credenciales validas y envia el formulario")
    public void elMedicoIngresaSusCredencialesValidasYEnviaElFormulario() {
        loginPage.authenticateWith(medicoEmail, medicoPassword);
        loginPage.waitForMedicalPanelRedirection();
    }

    @When("el medico selecciona un consultorio disponible y confirma la vinculacion")
    public void elMedicoSeleccionaUnConsultorioDisponibleYConfirmaLaVinculacion() {
        medicalPanelPage.waitUntilPanelIsVisible();

        boolean hasConsultorioAvailable = medicalPanelPage.hasAtLeastOneAvailableConsultorio();
        Assert.assertTrue("La precondicion de consultorio disponible no se cumple", hasConsultorioAvailable);

        if (availableConsultorioId == null
                || availableConsultorioId.isBlank()
                || !medicalPanelPage.isConsultorioOptionAvailable(availableConsultorioId)) {
            availableConsultorioId = medicalPanelPage.selectFirstAvailableConsultorio();
        } else {
            medicalPanelPage.selectConsultorio(availableConsultorioId);
        }

        medicalPanelPage.confirmConsultorioAssignment();
    }

    @Then("el consultorio cambia a ConMedicoDisponible y se visualiza correctamente en la UI")
    public void elConsultorioCambiaAConMedicoDisponibleYSeVisualizaCorrectamenteEnLaUI() {
        medicalPanelPage.waitForAssignmentCommandMessage();
        String assignmentMessage = medicalPanelPage.readAssignmentCommandMessage();

        Assert.assertTrue(
                "El sistema debe confirmar el comando de asociacion",
                assignmentMessage.contains("Comando en proceso: asociar"));

        medicalPanelPage.waitUntilStateIsConMedicoDisponible();
        Assert.assertEquals(
                "El estado de dominio esperado es ConMedicoDisponible",
                "ConMedicoDisponible",
                medicalPanelPage.readDomainStateFromUi());

        Assert.assertTrue(
                "El consultorio seleccionado debe mostrarse en la UI",
                medicalPanelPage.isConsultorioShownInUi(availableConsultorioId));
    }

    @Given("existe un usuario registrado con rol {string} y credenciales validas")
    public void existeUnUsuarioRegistradoConRolYCredencialesValidas(String role) {
        Assert.assertNotEquals("El rol para este escenario debe ser distinto a medico", "medico", role);

        nonMedicoEmail = TestIdentityUtils.buildUniqueEmail(role);
        nonMedicoPassword = preconditionsHelper.buildStrongPassword();
        String nonMedicoName = TestIdentityUtils.buildUniqueName(role);

        signUpPage.openSignUpPage();
        signUpPage.registerUser(nonMedicoName, nonMedicoEmail, nonMedicoPassword, role);
        signUpPage.waitForRedirectToSignInPage();
    }

    @When("el usuario navega a la pantalla de login")
    public void elUsuarioNavegaALaPantallaDeLogin() {
        loginPage.openSignInPage();
    }

    @When("el usuario ingresa sus credenciales y envia el formulario")
    public void elUsuarioIngresaSusCredencialesYEnviaElFormulario() {
        loginPage.authenticateWith(nonMedicoEmail, nonMedicoPassword);
        loginPage.waitForDashboardRedirection();
    }

    @Then("el sistema muestra el mensaje de acceso denegado {string} y no permite ingresar al panel medico")
    public void elSistemaMuestraElMensajeDeAccesoDenegadoYNoPermiteIngresarAlPanelMedico(String expectedAccessDeniedMessage) {
        loginPage.openMedicalPanelPage();
        loginPage.waitForRedirectOutsideMedicalPanel();

        String authToken = preconditionsHelper.readAuthTokenFromCookie(driver);
        String deniedMessage = preconditionsHelper.requestAccessDeniedMessage(authToken, "C1");

        Assert.assertEquals(
                "El backend debe responder con mensaje de acceso denegado para rol no medico",
                expectedAccessDeniedMessage,
                deniedMessage);

        Assert.assertFalse(
                "El usuario sin rol medico no debe quedarse en el panel medico",
                loginPage.isInMedicalPanelPath());

        Assert.assertFalse(
                "El panel medico no debe renderizarse para usuario sin rol medico",
                medicalPanelPage.isPanelVisible());
    }
}