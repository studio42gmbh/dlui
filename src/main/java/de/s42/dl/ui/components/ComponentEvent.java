// <editor-fold desc="The MIT License" defaultstate="collapsed">
/*
 * The MIT License
 * 
 * Copyright 2022 Studio 42 GmbH ( https://www.s42m.de ).
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

import de.s42.dl.ui.events.*;

/**
 *
 * @author Benjamin Schiller
 * @param <ComponentType>
 * @param <JComponentType>
 */
public class ComponentEvent<ComponentType, JComponentType> extends DefaultEvent
{

	protected ComponentType component;

	protected JComponentType jComponent;

	public ComponentEvent(String eventName, ComponentType component, JComponentType jComponent)
	{
		super(eventName);
		this.jComponent = jComponent;
		this.component = component;
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public ComponentType getComponent()
	{
		return component;
	}

	public void setComponent(ComponentType component)
	{
		this.component = component;
	}

	public JComponentType getjComponent()
	{
		return jComponent;
	}

	public void setjComponent(JComponentType jComponent)
	{
		this.jComponent = jComponent;
	}
	//</editor-fold>
}
