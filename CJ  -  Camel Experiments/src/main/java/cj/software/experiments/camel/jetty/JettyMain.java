package cj.software.experiments.camel.jetty;

import org.apache.camel.main.Main;

public class JettyMain
		extends Main
{
	public static void main(String[] pArgs) throws Exception
	{
		new JettyMain().run();
	}

	public JettyMain()
	{
		super();
		super.addRouteBuilder(new JettyRouteBuilder());
	}
}
