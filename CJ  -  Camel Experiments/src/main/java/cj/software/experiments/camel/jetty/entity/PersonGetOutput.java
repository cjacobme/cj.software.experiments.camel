package cj.software.experiments.camel.jetty.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "person-get-output")
@XmlType(name = "", propOrder =
{ "person"
})
public class PersonGetOutput
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "person", required = true)
	private PersonDetail person;

	public PersonDetail getPerson()
	{
		return this.person;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		protected PersonDetail person;

		protected Builder()
		{
		}

		public Builder withPersonDetail(PersonDetail pPerson)
		{
			this.person = pPerson;
			return this;
		}

		public PersonGetOutput build()
		{
			PersonGetOutput lResult = new PersonGetOutput();
			lResult.person = this.person;
			return lResult;
		}
	}
}
