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
package de.s42.dl.ui.components.dropdown;

import de.s42.dl.DLAttribute.AttributeDL;
import de.s42.dl.ui.components.AbstractComponent;
import de.s42.dl.ui.events.EventAction;
import javax.swing.JComboBox;

/**
 *
 * @author Benjamin Schiller
 */
public class Dropdown extends AbstractComponent<JComboBox>
{

	@AttributeDL(required = false)
	protected EventAction onSelect;

	@Override
	public JComboBox createSwingComponent()
	{
		JComboBox component = new JComboBox();
		
		initComponent(component);

		// Handle on select
		component.addActionListener((evt) -> {
			onSelect(component);
		});

		return component;
	}

	public void onSelect(JComboBox component)
	{
		if (onSelect != null) {
			try {
				onSelect.perform(new DropdownSelectEvent(this, component));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public EventAction getOnSelect()
	{
		return onSelect;
	}

	public void setOnSelect(EventAction onSelect)
	{
		this.onSelect = onSelect;
	}
	//</editor-fold>
}
