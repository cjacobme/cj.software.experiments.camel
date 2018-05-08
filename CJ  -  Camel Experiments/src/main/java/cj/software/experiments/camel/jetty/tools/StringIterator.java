package cj.software.experiments.camel.jetty.tools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.apache.log4j.Logger;

public class StringIterator
		implements
		Iterator<String>
{
	private Logger logger = Logger.getLogger(StringIterator.class);

	private String[] words;

	private int index;

	public StringIterator(String pSource)
	{
		Objects.requireNonNull(pSource);
		this.words = pSource.split(" ");
		this.index = 0;
		this.logger.info(String.format("StringIterator created with %d words", this.words.length));
	}

	@Override
	public boolean hasNext()
	{
		boolean lResult = this.index < this.words.length;
		return lResult;
	}

	@Override
	public String next()
	{
		if (!this.hasNext())
		{
			throw new NoSuchElementException();
		}
		String lResult = this.words[this.index++];
		this.logger.info(String.format("next[%d]=%s", this.index - 1, lResult));
		return lResult;
	}

}
