package cj.software.experiments.camel.jetty;

import org.apache.camel.builder.RouteBuilder;

public class JettyRouteBuilder
		extends RouteBuilder
{

	@Override
	public void configure() throws Exception
	{
		//@formatter:off
		from ("jetty://http://localhost:8765/camel/jetty01?httpMethodRestrict=POST")
			.routeId("jetty")
			.log("${exchangeId}: I was triggered")
			.log("${exchangeId}: ${body}")
			.setBody(simple("you triggered ${exchangeId}"))
		;
		//@formatter:on
	}

}
