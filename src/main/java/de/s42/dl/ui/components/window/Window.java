// <editor-fold desc="The MIT License" defaultstate="collapsed">
/*
 * The MIT License
 * 
 * Copyright 2022 Studio 42 GmbH (https://www.s42m.de).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
//</editor-fold>
package de.s42.dl.ui.components.window;

import de.s42.dl.DLAttribute.AttributeDL;
import de.s42.dl.ui.components.AbstractContainer;
import de.s42.dl.ui.components.Component;
import de.s42.dl.ui.events.EventAction;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Benjamin Schiller
 */
public class Window extends AbstractContainer<JFrame>
{

	@AttributeDL(required = false)
	protected String title;

	@AttributeDL(required = false)
	protected EventAction onClose;

	@Override
	public JFrame createSwingComponent()
	{
		JFrame frame = new JFrame();

		frame.setTitle(getTitle());

		// Do nothing by default on close window
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		// Set size and fill content pane with view
		if (getBounds() != null) {
			frame.setSize(getBounds().getSize());
			frame.setLocation(getBounds().getLocation());
		}
		frame.getContentPane().setLayout(null);

		// Iterate components and add them to content pane
		for (Component component : components) {

			JComponent jComponent = (JComponent) component.createSwingComponent();
			frame.getContentPane().add(jComponent);
		}

		// Add window listeners
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				onClose(frame);
			}
		});

		// Send on create instance event
		onCreate(frame);

		// Make sure the creation of the window happens in the UI Thread
		java.awt.EventQueue.invokeLater(() -> {

			// Fit window, center and show
			//window.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});

		return frame;
	}

	public void onClose(JFrame frame)
	{
		if (onClose != null) {
			try {
				onClose.perform(new CloseWindowEvent(Window.this, frame));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public EventAction getOnClose()
	{
		return onClose;
	}

	public void setOnClose(EventAction onClose)
	{
		this.onClose = onClose;
	}

	public EventAction getOnCreate()
	{
		return onCreate;
	}

	public void setOnCreate(EventAction onCreate)
	{
		this.onCreate = onCreate;
	}
	//</editor-fold>
}
