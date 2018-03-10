package cj.software.experiments.camel.jetty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import cj.software.experiments.camel.jetty.entity.Person;
import cj.software.experiments.camel.jetty.entity.PersonDetail;

public class PersonDatastore
{
	private static Map<UUID, PersonDetail> persons = new HashMap<>();

	private static Logger logger = Logger.getLogger(PersonDatastore.class);

	static
	{
		savePerson(
				PersonDetail
						.builder()
						.withVorname("Alpha", PersonDetail.Builder.class)
						.withNachname("Zulu", PersonDetail.Builder.class)
						.withAddress("somewhere")
						.withHobbies(Arrays.asList("Swimming", "Surfing"))
						.build());
		savePerson(
				PersonDetail
						.builder()
						.withVorname("Bravo", PersonDetail.Builder.class)
						.withNachname("Zulu", PersonDetail.Builder.class)
						.withAddress("Route 66")
						.withHobbies(Arrays.asList("Computer", "Physics", "Volleyball"))
						.build());
		savePerson(
				PersonDetail
						.builder()
						.withVorname("Alpha", PersonDetail.Builder.class)
						.withNachname("Zulu", PersonDetail.Builder.class)
						.withAddress("somewhere in milky way")
						.withHobbies(Arrays.asList("fast", "sports", "instead", "of", "soccer"))
						.build());
	}

	public static UUID savePerson(PersonDetail pPerson)
	{
		UUID lUUID = pPerson.makeOrGetId();
		persons.put(lUUID, pPerson);
		logger.info(String.format("saved new Person at id %s", lUUID));
		return lUUID;
	}

	public static Collection<Person> listPersons(Optional<Set<String>> pVornames)
	{
		Collection<PersonDetail> lAll = persons.values();
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
			lResult = new ArrayList<>();
			for (PersonDetail bPerson : lAll)
			{
				lResult.add(bPerson);
			}
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

	public static PersonDetail readPerson(UUID pUUID)
	{
		PersonDetail lResult = persons.get(pUUID);
		return lResult;
	}
}
