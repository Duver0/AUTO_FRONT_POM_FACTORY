package runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/gestion_consultorio.feature",
        glue = "steps",
        plugin = { "pretty" },
        tags = "@gestion_consultorio"
)
public class GestionConsultorioRunner {
}