package fr.utbm.vi51.group11.lemmings.controller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorController
{
	/** Logger of the class */
	private final static Logger				s_LOGGER			= LoggerFactory
																		.getLogger(ErrorController.class);

	private static final Queue<Exception>	m_pendingExceptions	= new ConcurrentLinkedQueue<Exception>();

	public synchronized static void addPendingException(
			final Exception _exception)
	{
		m_pendingExceptions.add(_exception);
	}

	public synchronized static void throwPendingException() throws Exception
	{
		if (!m_pendingExceptions.isEmpty())
		{
			Exception exception = m_pendingExceptions.poll();
			s_LOGGER.error("", exception);
			throw exception;
		}
	}
}
