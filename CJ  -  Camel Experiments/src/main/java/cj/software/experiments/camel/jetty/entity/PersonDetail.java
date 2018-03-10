package cj.software.experiments.camel.jetty.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "person-detail", propOrder =
{ "address", "hobbies"
})
public class PersonDetail
		extends Person
{
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "address", required = true)
	private String address;

	@XmlElementWrapper(name = "hobbies", required = true)
	@XmlElement(name = "hobby")
	private List<String> hobbies = new ArrayList<>();

	public String getAddress()
	{
		return this.address;
	}

	public List<String> getHobbies()
	{
		return Collections.unmodifiableList(this.hobbies);
	}

	@Override
	public String toString()
	{
		//@formatter:off
		StringBuilder lSB = new StringBuilder ("Person[")
				.append("id=").append(this.getId())
				.append(",vorname=").append(this.getVorname())
				.append(",nachname=").append(this.getNachname())
				.append(",address=").append(this.address)
				.append(",#hobbies=").append(this.hobbies.size())
				.append("]");
		//@formatter:on
		String lResult = lSB.toString();
		return lResult;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
			extends Person.Builder
	{
		private String address;

		private List<String> hobbies = new ArrayList<>();

		public Builder withAddress(String pAddress)
		{
			this.address = pAddress;
			return this;
		}

		public Builder withHobbies(Collection<String> pHobbies)
		{
			this.hobbies.clear();
			this.hobbies.addAll(pHobbies);
			return this;
		}

		@Override
		public PersonDetail build()
		{
			PersonDetail lResult = new PersonDetail();
			lResult = (PersonDetail) super.fill(lResult);
			lResult.address = this.address;
			lResult.hobbies.clear();
			lResult.hobbies.addAll(this.hobbies);
			return lResult;
		}
	}
}
