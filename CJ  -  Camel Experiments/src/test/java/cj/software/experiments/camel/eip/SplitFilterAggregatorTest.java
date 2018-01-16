package cj.software.experiments.camel.eip;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * demonstrates how to split a message, filter some of its content, and aggregate it. The message
 * with which this is tested contains several text lines. Each line begins with a letter <tt>y</tt>
 * and <tt>n</tt>, followed by a number. The filter allows only the rows that start with a
 * <tt>y</tt>. The aggregator sums up the numbers.
 */
public class SplitFilterAggregatorTest
		extends CamelTestSupport
{
	@Produce(uri = "direct:start")
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:calculated")
	private MockEndpoint resultMock;

	@Override
	protected RouteBuilder createRouteBuilder()
	{

		RouteBuilder lResult = new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("split-filter-aggr")
					.split(body().tokenize("\n"))
						.log("splitted: ${body}")
						.filter(PredicateBuilder.startsWith(body(), constant("y")))
							.aggregate(constant(true), new MyAggregator()).completionTimeout(1000000)
							.log("filtered: ${body}")
						.end()
						.log("after filter ${body}")
					.end()
					.log("all done ${body}")
					.to("mock:calculated")
				;
				//@formatter:on
			}
		};
		return lResult;
	}

	private class MyAggregator
			implements
			AggregationStrategy
	{
		@Override
		public Exchange aggregate(Exchange pOldExchange, Exchange pNewExchange)
		{
			Message lNewIn = pNewExchange.getIn();
			String lNewBody = lNewIn.getBody(String.class);
			String lSubString = lNewBody.substring(1);
			int lNewValue = Integer.parseInt(lSubString);
			Exchange lResult;
			if (pOldExchange == null)
			{
				Integer lSum = new Integer(lNewValue);
				lNewIn.setBody(lSum);
				lResult = pNewExchange;
			}
			else
			{
				Message lOldIn = pOldExchange.getIn();
				Integer lSum = lOldIn.getBody(Integer.class);
				lSum += lNewValue;
				lOldIn.setBody(lSum);
				lResult = pOldExchange;
			}
			Integer lStored = lResult.getIn().getBody(Integer.class);
			SplitFilterAggregatorTest.this.log.info("back " + lStored);
			return lResult;
		}
	}

	@Test
	public void splitFilterAggregate() throws InterruptedException
	{
		String lContent = "y13\ny25\nn62\ny-42\nn-23\ny14";
		this.resultMock.expectedBodiesReceived(new Integer(10));
		this.template.sendBody(lContent);
		this.resultMock.assertIsSatisfied();
	}
}
