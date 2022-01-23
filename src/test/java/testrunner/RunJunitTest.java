package testrunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import utils.CommonActions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/features"},
		glue = {"stepdefinition"},monochrome = true		
		)

public class RunJunitTest {
	 @BeforeClass
	    public static void setup() {
	        CommonActions.initializeExtentReports();
	    }

	    @AfterClass
	    public static void teardown() {
	    	CommonActions.extent.flush();
	    }
}
