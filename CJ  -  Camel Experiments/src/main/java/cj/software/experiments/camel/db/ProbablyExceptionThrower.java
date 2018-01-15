package cj.software.experiments.camel.db;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * a processor that throws a {@link RuntimeException} if the <i>name</i> contains the substring
 * <tt>throw</tt>. This is to demonstrate the error behaviour.
 */
public class ProbablyExceptionThrower
		implements
		Processor
{

	@Override
	public void process(Exchange pExchange) throws Exception
	{
		Message lIn = pExchange.getIn();
		@SuppressWarnings("unchecked")
		Map<String, Object> lBody = lIn.getBody(Map.class);
		String lName = (String) lBody.get("name");
		if (lName.toLowerCase().indexOf("throw") >= 0)
		{
			throw new RuntimeException(lName);
		}
	}

}
