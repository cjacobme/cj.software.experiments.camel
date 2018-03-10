package cj.software.experiments.camel.jetty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import cj.software.experiments.camel.jetty.entity.Person;

public class PersonDatastore
{
	private static Map<UUID, Person> persons = new HashMap<>();

	private static Logger logger = Logger.getLogger(PersonDatastore.class);

	static
	{
		savePerson(new Person("Alpha", "Zulu"));
		savePerson(new Person("Bravo", "Zulu"));
		savePerson(new Person("Charlie", "Zulu"));
	}

	public static UUID savePerson(Person pPerson)
	{
		UUID lUUID = UUID.randomUUID();
		Person lMine = new Person(lUUID, pPerson.getVorname(), pPerson.getNachname());
		persons.put(lUUID, lMine);
		logger.info(String.format("saved new Person at id %s", lUUID));
		return lUUID;
	}

	public static Collection<Person> listPersons(Optional<Set<String>> pVornames)
	{
		Collection<Person> lAll = persons.values();
		Collection<Person> lResult;
		if (pVornames.isPresent())
		{
			Set<String> lSearched = pVornames.get();
			lResult = new ArrayList<>();
			for (Person bPerson : lAll)
			{
				String lPersonVorname = bPerson.getVorname();
				for (String bSearched : lSearched)
				{
					if (lPersonVorname.startsWith(bSearched))
					{
						lResult.add(bPerson);
						break;
					}
				}
			}
		}
		else
		{
			lResult = lAll;
		}
		return lResult;
	}

	public static boolean delete(UUID pUUID)
	{
		Person lReturned = persons.remove(pUUID);
		boolean lResult = (lReturned != null);
		logger.info(String.format("deleted Person with id %s: %s", pUUID, String.valueOf(lResult)));
		return lResult;
	}
}
