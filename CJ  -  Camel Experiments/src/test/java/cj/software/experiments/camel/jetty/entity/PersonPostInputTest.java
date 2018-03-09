package cj.software.experiments.camel.jetty.entity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

public class PersonPostInputTest
{
	@Test
	public void dumpXml() throws JAXBException
	{
		Person lPerson = new Person("Christian", "Jacob");
		PersonsPostInput lInput = new PersonsPostInput(lPerson);

		JAXBContext lCtx = JAXBContext.newInstance(PersonsPostInput.class);
		Marshaller lMarshaller = lCtx.createMarshaller();
		lMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		lMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		lMarshaller.marshal(lInput, System.out);
	}
}
