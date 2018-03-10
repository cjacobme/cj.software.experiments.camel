package cj.software.experiments.camel.jetty.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "person-post-input")
@XmlType(name = "", propOrder =
{ "person"
})
public class PersonsPostInput
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	private PersonDetail person;

	PersonsPostInput()
	{
	}

	public PersonsPostInput(PersonDetail pPerson)
	{
		this.person = pPerson;
	}

	public PersonDetail getPerson()
	{
		return this.person;
	}

}
