package cj.software.experiments.camel.jetty;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cj.software.experiments.camel.jetty.entity.Person;

public class PersonDatastore
{
	private static Map<UUID, Person> persons = new HashMap<>();

	public static UUID savePerson(Person pPerson)
	{
		UUID lUUID = UUID.randomUUID();
		Person lMine = new Person(lUUID, pPerson.getVorname(), pPerson.getNachname());
		persons.put(lUUID, lMine);
		return lUUID;
	}
}
