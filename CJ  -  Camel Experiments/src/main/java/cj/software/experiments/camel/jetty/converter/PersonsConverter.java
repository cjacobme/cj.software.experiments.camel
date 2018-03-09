package cj.software.experiments.camel.jetty.converter;

import java.util.Collection;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

import cj.software.experiments.camel.jetty.entity.Person;
import cj.software.experiments.camel.jetty.entity.PersonsGetOutput;
import cj.software.experiments.camel.jetty.entity.PersonsPostInput;
import cj.software.experiments.camel.jetty.entity.PersonsPostOutput;

@Converter
public class PersonsConverter
{
	private static Logger logger = Logger.getLogger(PersonsConverter.class);

	@Converter
	public static Person toPerson(PersonsPostInput pInput, Exchange pExchange)
	{
		Person lResult = pInput.getPerson();
		logger.info(
				String.format(
						"%s: converted to %s",
						pExchange.getExchangeId(),
						lResult.toString()));
		return lResult;
	}

	@Converter
	public static PersonsPostOutput toPersonsPostOutput(UUID pUUID, Exchange pExchange)
	{
		PersonsPostOutput lResult = new PersonsPostOutput(pUUID);
		return lResult;
	}

	@Converter
	public static PersonsGetOutput toGetOutput(Collection<Person> pPersons, Exchange pExchange)
	{
		PersonsGetOutput lResult = PersonsGetOutput.builder().withPersons(pPersons).build();
		return lResult;
	}
}
