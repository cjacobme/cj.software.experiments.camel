package cj.software.experiments.camel.jetty.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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

	Person()
	{
	}

	public Person(String pVorname, String pNachname)
	{
		this.vorname = pVorname;
		this.nachname = pNachname;
	}

	public Person(UUID pId, String pVorname, String pNachname)
	{
		this(pVorname, pNachname);
		this.id = pId;
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
}
