package cj.software.experiments.camel.jetty.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "persons-post-output")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "id"
})
public class PersonsPostOutput
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private UUID id;

	PersonsPostOutput()
	{
	}

	public PersonsPostOutput(UUID pId)
	{
		this();
		this.id = pId;
	}

	public UUID getId()
	{
		return this.id;
	}

}
