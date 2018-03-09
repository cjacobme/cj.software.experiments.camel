package cj.software.experiments.camel.jetty;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;

import cj.software.experiments.camel.jetty.entity.Person;
import cj.software.experiments.camel.jetty.entity.PersonsGetOutput;
import cj.software.experiments.camel.jetty.entity.PersonsPostInput;
import cj.software.experiments.camel.jetty.entity.PersonsPostOutput;

public class JettyRouteBuilder
		extends RouteBuilder
{
	private static Logger logger = Logger.getLogger(JettyRouteBuilder.class);

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
			.setBody(method(JettyRouteBuilder.class, "listPersons"))
			.convertBodyTo(PersonsGetOutput.class)
		;
		
		from("jetty://http://localhost:8765/persons/{id}?httpMethodRestrict=DELETE")
			.routeId("DELETE persons")
			.setBody(method(JettyRouteBuilder.class, "delete"))
		;
		//@formatter:on
	}

	public static Object delete(Exchange pExchange)
	{
		Message lIn = pExchange.getIn();
		String lHttpPath = lIn.getHeader(Exchange.HTTP_PATH, String.class);
		String lUuidStr = lHttpPath.substring(lHttpPath.lastIndexOf('/') + 1);
		UUID lUUID = UUID.fromString(lUuidStr);
		boolean lSuccess = PersonDatastore.delete(lUUID);
		logger.info(
				String.format(
						"%s: deleted %s success %s",
						pExchange.getExchangeId(),
						lUUID,
						String.valueOf(lSuccess)));
		return null;
	}

	public static Collection<Person> listPersons(Exchange pExchange)
	{
		Message lIn = pExchange.getIn();
		Object lHeaderVorname = lIn.getHeader("vorname");
		Optional<Set<String>> lParam;
		if (lHeaderVorname != null)
		{
			Set<String> lChecked;
			if (lHeaderVorname instanceof String)
			{
				lChecked = new HashSet<>();
				lChecked.add((String) lHeaderVorname);
			}
			else if (lHeaderVorname instanceof Collection)
			{
				@SuppressWarnings(
				{ "unchecked", "rawtypes"
				})
				Collection<String> lCollection = (Collection) lHeaderVorname;
				lChecked = new HashSet<>(lCollection);
			}
			else
			{
				throw new IllegalArgumentException(
						"unknown: " + lHeaderVorname.getClass().getName());
			}
			lParam = Optional.of(lChecked);
		}
		else
		{
			lParam = Optional.empty();
		}
		Collection<Person> lResult = PersonDatastore.listPersons(lParam);
		return lResult;
	}
}
