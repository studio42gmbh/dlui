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
package de.s42.dl.ui.components;

import de.s42.dl.DLAttribute.AttributeDL;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Benjamin Schiller
 */
public class Panel extends AbstractContainer<JPanel, Component>
{

	public enum Layout
	{
		NONE,
		VERTICALFLOW,
		HORIZONTALFLOW,
		GRIDBAG
	}

	@AttributeDL(required = false, defaultValue = "NONE")
	protected Layout layout = Layout.NONE;

	@Override
	public JPanel createSwingComponent()
	{
		JPanel component = new JPanel();

		component.setBorder(new EmptyBorder(getInsets()));

		if (layout == null || layout == Layout.NONE) {
			component.setLayout(null);
		} else if (layout == Layout.VERTICALFLOW) {
			component.setLayout(new BoxLayout(component, BoxLayout.PAGE_AXIS));
		} else if (layout == Layout.HORIZONTALFLOW) {
			component.setLayout(new BoxLayout(component, BoxLayout.LINE_AXIS));
		} else if (layout == Layout.GRIDBAG) {
			component.setLayout(new GridBagLayout());
		}

		// Iterate components and add them to content pane
		boolean first = true;
		for (Component child : components) {

			JComponent jComponent = (JComponent) child.createSwingComponent();
			jComponent.setAlignmentX(JComponent.LEFT_ALIGNMENT);

			// Add spacers in Box layouts
			if (!first
				&& (layout == Layout.VERTICALFLOW || layout == Layout.HORIZONTALFLOW)) {
				component.add(Box.createRigidArea(new Dimension(5, 5)));
			}
			
			// Handle Gridbaglayout
			if (layout == Layout.GRIDBAG) {
				GridBagConstraints c = ComponentHelper.createConstraints(child);
				component.add(jComponent, c);
			}
			else {
				component.add(jComponent);				
			}


			first = false;
		}
		
		initComponent(component);

		return component;
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public Layout getLayout()
	{
		return layout;
	}

	public void setLayout(Layout layout)
	{
		this.layout = layout;
	}
	//</editor-fold>
}
