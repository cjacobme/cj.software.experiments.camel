package cj.software.experiments.camel.jetty.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "persons-get-output")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "persons"
})
public class PersonsGetOutput
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElementWrapper(name = "persons", required = true)
	@XmlElement(name = "person")
	private List<Person> persons = new ArrayList<>();

	private PersonsGetOutput()
	{
	}

	public List<Person> getPersons()
	{
		return Collections.unmodifiableList(this.persons);
	}

	public static Builder builder()
	{
		return new Builder();
	}

	@XmlTransient
	public static class Builder
	{
		protected PersonsGetOutput instance;

		protected Builder()
		{
			this.instance = new PersonsGetOutput();
		}

		public Builder withPersons(Collection<Person> pPersons)
		{
			this.instance.persons.clear();
			return this.addPersons(pPersons);
		}

		public Builder addPersons(Collection<Person> pPersons)
		{
			this.instance.persons.addAll(pPersons);
			return this;
		}

		public PersonsGetOutput build()
		{
			PersonsGetOutput lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
