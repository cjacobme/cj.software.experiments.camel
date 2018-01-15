package cj.software.experiments.camel.db;

import javax.sql.DataSource;

import org.apache.camel.main.Main;
import org.apache.commons.dbcp2.BasicDataSource;

import cj.software.experiments.camel.FinishedRouteBuilder;

public class DBTriggerRouteMain
{
	private Main main;

	public static void main(String[] pArgs) throws Exception
	{
		DBTriggerRouteMain lInstance = new DBTriggerRouteMain();
		lInstance.runCamel();
	}

	private void runCamel() throws Exception
	{
		this.main = new Main();
		String lDatasourceName = "DBTrigger.dataSource";
		DataSource lDataSource = this.setupDataSource();
		this.main.bind(lDatasourceName, lDataSource);
		this.main.addRouteBuilder(
				new DBTriggerRouteBuilder(lDatasourceName, 2000, 5000, "direct:finished"));
		this.main.addRouteBuilder(new FinishedRouteBuilder());

		this.main.run();
	}

	private DataSource setupDataSource()
	{
		BasicDataSource lResult = new BasicDataSource();
		lResult.setDriverClassName("org.postgresql.Driver");
		lResult.setUrl("jdbc:postgresql://localhost/experiments");
		lResult.setUsername("experiments");
		lResult.setPassword("experiments");
		return lResult;
	}
}
