package stepdefinition;

import java.util.Properties;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.CommonActions;
import utils.ConfigReader;
import utils.ExcelReader;

public class Hooks {
	protected ConfigReader configReader;
	protected ExcelReader reader;
	protected CommonActions commonAction;
	Properties prop;
	

	@Before
	public void intializeObjectReference() {
				
	}

	@After
	public void flushInput() {
		//extent.flush();
	}

}
