package cj.software.experiments.camel.jetty;

import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;

import cj.software.experiments.camel.jetty.entity.PersonsPostInput;
import cj.software.experiments.camel.jetty.entity.PersonsPostOutput;

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
		
		from("jetty://http://localhost:8765/persons?httpMethodRestrict=POST")
			.routeId("POST persons")
			.convertBodyTo(String.class)
			.log("${exchangeId}: POST persons")
			.convertBodyTo(PersonsPostInput.class)
			.setBody(simple("${body.person}"))
			.setBody(method(PersonDatastore.class, "savePerson"))
			.setBody(method(JettyRouteBuilder.class, "wrapInPersonPostOutput"))
		;
		//@formatter:on
	}

	public static PersonsPostOutput wrapInPersonPostOutput(UUID pUUID)
	{
		PersonsPostOutput lResult = new PersonsPostOutput(pUUID);
		return lResult;
	}
}
