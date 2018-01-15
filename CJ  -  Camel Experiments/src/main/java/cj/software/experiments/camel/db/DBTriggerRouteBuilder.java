package cj.software.experiments.camel.db;

import java.util.Objects;

import org.apache.camel.builder.RouteBuilder;

/**
 * a RouteBuilder which shows how a Trigger from a database table can be implemented. It assumes
 * that there is a database named <tt>cjtrigger</tt> with three columns:
 * <ol>
 * <li>a <tt>bigint</tt> column named <i>id</i></li>
 * <li>a <tt>varchar</tt> column named <i>name</i></li>
 * <li>a <tt>varchar</tt> column named <i>state</i></li>
 * </ol>
 * The Route selects entries in which the column <i>state</i> has the value <tt>created</tt>. It
 * picks up one entry after the other. If the further processing went flawlessly, the state is
 * changed to <tt>processed</tt>. If an error ocurred, the state is changed to <tt>failed</tt>.
 */
public class DBTriggerRouteBuilder
		extends RouteBuilder
{
	private long initialDelay;

	private long cycleDelay;

	private String datasourceName;

	private String nextEndpoint;

	private DBTriggerRouteBuilder()
	{
	}

	public DBTriggerRouteBuilder(
			String pDatasourceName,
			long pInitialDelay,
			long pCycleDelay,
			String pNextEndpoint)
	{
		this();
		this.datasourceName = Objects.requireNonNull(pDatasourceName);
		this.initialDelay = pInitialDelay;
		this.cycleDelay = pCycleDelay;
		this.nextEndpoint = Objects.requireNonNull(pNextEndpoint);
	}

	@Override
	public void configure() throws Exception
	{
		//@formatter:off
		String lURI = String.format("sql:select * from cjtrigger "
				+ "where state = 'created' order by id"
				+ "?dataSource=%s&consumer.delay=%d&consumer.initialDelay=%d&maxMessagesPerPoll=1"
				+ "&onConsume=update cjtrigger set state = 'processed' where id = :#id"
				+ "&onConsumeFailed=update cjtrigger set state = 'failed' where id = :#id",
				this.datasourceName, this.cycleDelay, this.initialDelay);
		from (lURI)
			.routeId("select")
			.log("triggered: ${body}")
			.process(new ProbablyExceptionThrower())
			.to(this.nextEndpoint)
		;
		//@formatter:on
	}

}
