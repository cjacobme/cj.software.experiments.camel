package cj.software.experiments.camel;

import org.apache.camel.builder.RouteBuilder;

public class FinishedRouteBuilder
		extends RouteBuilder
{

	@Override
	public void configure() throws Exception
	{
		// @formatter:off
		from("direct:finished")
			.routeId("finished")
			.log("processing finished")
		;
		//@formatter:on
	}

}
