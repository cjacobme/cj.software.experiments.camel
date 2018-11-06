package cj.software.experiments.camel.wiretap;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class WiretapExample01
{
	public static void main(String[] pArgs) throws Exception
	{
		WiretapExample01 lInstance = new WiretapExample01();
		lInstance.runExample();
	}

	public void runExample() throws Exception
	{
		CamelContext lCtx = new DefaultCamelContext();
		try
		{
			lCtx.addRoutes(new WiretapExample01RB());
			ProducerTemplate lProducerTemplate = lCtx.createProducerTemplate();
			lCtx.start();
			lProducerTemplate.sendBody("direct:start", new Holder("one"));
			Thread.sleep(4000l);
		}
		finally
		{
			lCtx.stop();
		}
	}

	static class Holder
	{
		private String content;

		public Holder(String pContent)
		{
			this.content = pContent;
		}

		public String getContent()
		{
			return this.content;
		}
	}

	public static Holder create2ndHolder()
	{
		return new Holder("asdf");
	}

	class WiretapExample01RB
			extends RouteBuilder
	{

		@Override
		public void configure() throws Exception
		{
			//@formatter:off
			from("direct:start")
				.routeId("start")
				.log("${exchangeId}: send '${body}' to tap router...")
				.wireTap("direct:tap")
				.setBody(method(WiretapExample01.class, "create2ndHolder"))
				.log("${exchangeId}: finished with '${body}'")
			;

			from("direct:tap")
				.routeId("tap")
				.process(new Processor()
					{
						@Override
						public void process(Exchange pExchange) throws Exception
						{
							Thread.sleep(1500l);
						}
					})
				.log(LoggingLevel.DEBUG, "${exchangeId}: body in tap is '${body}'")
			;
			//@formatter:on
		}

	}
}
