package cj.software.experiments.camel.jetty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import cj.software.experiments.camel.jetty.entity.Person;

public class PersonDatastore
{
	private static Map<UUID, Person> persons = new HashMap<>();

	private static Logger logger = Logger.getLogger(PersonDatastore.class);

	public static UUID savePerson(Person pPerson)
	{
		UUID lUUID = UUID.randomUUID();
		Person lMine = new Person(lUUID, pPerson.getVorname(), pPerson.getNachname());
		persons.put(lUUID, lMine);
		logger.info(String.format("saved new Person at id %s", lUUID));
		return lUUID;
	}

	public static Collection<Person> listPersons()
	{
		return persons.values();
	}
}
