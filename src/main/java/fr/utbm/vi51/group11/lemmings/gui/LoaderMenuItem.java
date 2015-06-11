package fr.utbm.vi51.group11.lemmings.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;

public class LoaderMenuItem extends JMenuItem implements ActionListener
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	private Observer				m_observer;
	private final LevelObservable	m_observable;

	public LoaderMenuItem(final String _text)
	{
		super(_text);
		this.addActionListener(this);
		m_observable = new LevelObservable();
	}

	public void setObserver(
			final Observer _observer)
	{
		m_observer = _observer;
	}

	@Override
	public void actionPerformed(
			final ActionEvent e)
	{
		m_observer.update(m_observable, this.getText());
	}

	public class LevelObservable extends Observable
	{
	}
}
