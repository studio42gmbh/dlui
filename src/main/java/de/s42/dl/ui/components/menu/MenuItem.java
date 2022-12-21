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
package de.s42.dl.ui.components.menu;

import de.s42.dl.DLAttribute.AttributeDL;
import de.s42.dl.ui.components.AbstractComponent;
import de.s42.dl.ui.events.EventAction;
import javax.swing.JMenuItem;

/**
 *
 * @author Benjamin Schiller
 */
public class MenuItem extends AbstractComponent<JMenuItem>
{

	@AttributeDL(required = false)
	protected EventAction onClick;

	@AttributeDL(required = true)
	protected String text;
	
	@AttributeDL(required = false)
	protected char mnemonic;
	
	@Override
	public JMenuItem createSwingComponent()
	{
		JMenuItem component = new JMenuItem(getText());
		
		if (mnemonic > 0) {
			component.setMnemonic(getMnemonic());
		}
		
		initComponent(component);

		// Handle on select
		component.addActionListener((evt) -> {
			onClick(component);
		});

		return component;
	}

	public void onClick(JMenuItem component)
	{
		if (onClick != null) {
			try {
				onClick.perform(new MenuItemClickedEvent(this, component));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public EventAction getOnClick()
	{
		return onClick;
	}

	public void setOnClick(EventAction onClick)
	{
		this.onClick = onClick;
	}
	
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}	
	
	public char getMnemonic()
	{
		return mnemonic;
	}

	public void setMnemonic(char mnemonic)
	{
		this.mnemonic = mnemonic;
	}	
	//</editor-fold>
}
