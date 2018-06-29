package cj.software.experiments.camel.json;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class JsonSplitTest
		extends CamelTestSupport
{
	@Produce(uri = "direct:start")
	private ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:detail")
	private MockEndpoint mockDetail;

	@Override
	protected RoutesBuilder createRouteBuilder()
	{
		RoutesBuilder lResult = new RouteBuilder()
		{
			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("start")
					.split().jsonpath("$").streaming()
						.log("${exchangeId}: ${body}")
						.to("mock:detail")
					.end()
				;
				//@formatter:on
			}
		};
		return lResult;
	}

	@Test
	public void split() throws IOException, URISyntaxException, InterruptedException
	{
		String lJsonString = new String(
				Files.readAllBytes(
						Paths.get(JsonSplitTest.class.getResource("Persons.json").toURI())));
		this.mockDetail.expectedMessageCount(8);
		this.producerTemplate.sendBody(lJsonString);
		this.mockDetail.assertIsSatisfied();
	}
}
