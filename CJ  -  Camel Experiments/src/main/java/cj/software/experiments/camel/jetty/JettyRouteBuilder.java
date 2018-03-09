package cj.software.experiments.camel.jetty;

import org.apache.camel.builder.RouteBuilder;

import cj.software.experiments.camel.jetty.entity.Person;
import cj.software.experiments.camel.jetty.entity.PersonsGetOutput;
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
			.convertBodyTo(Person.class)
			.setBody(method(PersonDatastore.class, "savePerson"))
			.convertBodyTo(PersonsPostOutput.class)
			.log("${exchangeId}: respond ${body.id}")
		;
		
		from("jetty://http://localhost:8765/persons?httpMethodRestrict=GET")
			.routeId("GET persons")
			.setBody(method(PersonDatastore.class, "listPersons"))
			.convertBodyTo(PersonsGetOutput.class)
		;
		//@formatter:on
	}
}
