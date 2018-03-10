package cj.software.experiments.camel.jetty.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "person", propOrder =
{ "id", "vorname", "nachname"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class Person
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	private UUID id;

	@XmlElement(required = true)
	private String vorname;

	@XmlElement(required = true)
	private String nachname;

	protected Person()
	{
	}

	public UUID makeOrGetId()
	{
		if (this.id == null)
		{
			this.id = UUID.randomUUID();
		}
		return this.id;
	}

	public UUID getId()
	{
		return this.id;
	}

	public String getVorname()
	{
		return this.vorname;
	}

	public String getNachname()
	{
		return this.nachname;
	}

	@Override
	public String toString()
	{
		//@formatter:off
		StringBuilder lSB = new StringBuilder ("Person[")
				.append("id=").append(this.id)
				.append(",vorname=").append(this.vorname)
				.append(",nachname=").append(this.nachname)
				.append("]");
		//@formatter:on
		String lResult = lSB.toString();
		return lResult;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	@XmlTransient
	public static class Builder
	{
		private String vorname;

		private String nachname;

		public <T extends Builder> T withVorname(String pVorname, Class<? extends T> pBuilderClass)
		{
			this.vorname = pVorname;
			return pBuilderClass.cast(this);
		}

		public <T extends Builder> T withNachname(
				String pNachname,
				Class<? extends T> pBuilderClass)
		{
			this.nachname = pNachname;
			return pBuilderClass.cast(this);
		}

		public Person build()
		{
			Person lResult = new Person();
			lResult = this.fill(lResult);
			return lResult;
		}

		protected Person fill(Person pPerson)
		{
			Person lResult = pPerson;
			lResult.vorname = this.vorname;
			lResult.nachname = this.nachname;
			this.nachname = null;
			this.vorname = null;
			return lResult;
		}
	}
}
