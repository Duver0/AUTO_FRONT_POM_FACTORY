package runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/negative_flow.feature",
        glue = "steps",
        plugin = { "pretty" }
)
public class NegativeCucumberTestRunner {
}
