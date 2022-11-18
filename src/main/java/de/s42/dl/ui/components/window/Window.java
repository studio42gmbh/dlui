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
import static de.s42.dl.ui.components.AbstractComponent.COMPONENT_CLIENT_PROPERTY_KEY;
import de.s42.dl.ui.components.AbstractContainer;
import de.s42.dl.ui.components.Component;
import de.s42.dl.ui.events.EventAction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
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
	
	@AttributeDL(required = false, defaultValue = "false")
	protected boolean maximized = false;
	
	public static Optional<Window> getWindow(JFrame jFrame)
	{
		Object comp = jFrame.getRootPane().getClientProperty(COMPONENT_CLIENT_PROPERTY_KEY);

		if (comp instanceof Window) {
			return Optional.of((Window) comp);
		}

		return Optional.empty();
	}
	
	@Override
	public JFrame createSwingComponent()
	{
		JFrame frame = new JFrame();
		
		frame.getRootPane().putClientProperty(COMPONENT_CLIENT_PROPERTY_KEY, this);

		// Do nothing by default on close window
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		frame.getContentPane().setLayout(new GridBagLayout());

		// Iterate components and add them to content pane
		for (Component component : components) {

			JComponent jComponent = (JComponent) component.createSwingComponent();
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.gridx = component.getGridX();
			c.gridy = component.getGridY();
			c.weightx = component.getWeightX();
			c.weighty = component.getWeightY();
			
			frame.getContentPane().add(jComponent, c);
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
		
		// Set size and fill content pane with view
		if (getBounds() != null) {
			frame.setSize(getBounds().getSize());
			frame.setLocation(getBounds().getLocation());
		}
				
		frame.setTitle(getTitle());
		
		if (isMaximized()) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}

		// Make sure the creation of the window happens in the UI Thread
		java.awt.EventQueue.invokeLater(() -> {

			// Fit window, center and show
			//window.pack();
			//frame.setLocationRelativeTo(null);
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

	@Override
	public EventAction getOnCreate()
	{
		return onCreate;
	}

	@Override
	public void setOnCreate(EventAction onCreate)
	{
		this.onCreate = onCreate;
	}
	
	public boolean isMaximized()
	{
		return maximized;
	}

	public void setMaximized(boolean maximized)
	{
		this.maximized = maximized;
	}
	//</editor-fold>
}
