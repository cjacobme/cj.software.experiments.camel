package cj.software.experiments.camel.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

/**
 * verifiziert, ob der Postgres-JDBC-Treiber dieses Projekts mit Java-8 {@link OffsetDateTime}
 * umgehen kann. Dazu wird eine Tabelle mit dem Namen {@code Event} benutzt. Diese besteht aus den
 * Spalten {@code occurred} vom Type {@code timestamp with time zone} und {@code description} vom
 * Typ {@code character varying(255)}. Diese enthält zunächst mal einen Datensatz, der gelesen wird.
 */
public class OffsetDateTimeTest
		extends CamelTestSupport
{
	@Produce(uri = "direct:readThem")
	private ProducerTemplate templateReadThem;

	@EndpointInject(uri = "mock:dataRead")
	private MockEndpoint mockDataRead;

	@Override
	protected Context createJndiContext() throws Exception
	{
		Context lResult = super.createJndiContext();

		DataSource lDataSource = this.setupDataSource();
		lResult.bind("DataSource", lDataSource);

		return lResult;
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

	@Override
	protected RoutesBuilder createRouteBuilder()
	{
		RoutesBuilder lResult = new RouteBuilder()
		{
			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:readThem")
					.routeId("readThem")
					.log("${exchangeId}: read entries...")
					.to("sql:select * from event")
					.log("${exchangeId}: read ${body}")
					.to("mock:dataRead")
				;
				//@formatter:on
			}
		};
		return lResult;
	}

	@Test
	public void readRecords() throws InterruptedException, SQLException
	{
		this.mockDataRead.setMinimumExpectedMessageCount(1);
		this.templateReadThem.sendBody("los jetzt");
		this.mockDataRead.assertIsSatisfied();

		List<Exchange> lExchanges = this.mockDataRead.getExchanges();
		this.log.info(String.format("%d exchanges", lExchanges.size()));
		for (int bExchange = 0; bExchange < lExchanges.size(); bExchange++)
		{
			Exchange lExchange = lExchanges.get(bExchange);
			Message lIn = lExchange.getIn();
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> lList = lIn.getBody(List.class);
			for (Map<String, Object> bRow : lList)
			{
				Set<String> lKeys = bRow.keySet();
				for (String bKey : lKeys)
				{
					Object lObject = bRow.get(bKey);
					this.log.info(String.format("%s is a %s", bKey, lObject.getClass().getName()));
				}
			}

			this.readByJdbc(lExchange);
		}
	}

	private void readByJdbc(Exchange pExchange) throws SQLException
	{
		CamelContext lContext = pExchange.getContext();
		Registry lRegistry = lContext.getRegistry();
		BasicDataSource lDataSource = (BasicDataSource) lRegistry.lookupByNameAndType(
				"DataSource",
				DataSource.class);
		DateTimeFormatter lFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZZ");
		try (Connection lConnection = lDataSource.getConnection())
		{
			try (Statement lStmt = lConnection.createStatement())
			{
				try (ResultSet lRS = lStmt.executeQuery("select occurred, description from event"))
				{
					while (lRS.next())
					{
						OffsetDateTime lODT = lRS.getObject("occurred", OffsetDateTime.class);
						String lDescription = lRS.getString("description");
						this.log.info(
								String.format("%s: %s", lODT.format(lFormatter), lDescription));
					}
				}
			}
		}
	}
}
