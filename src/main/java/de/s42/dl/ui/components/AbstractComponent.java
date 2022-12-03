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

import de.s42.base.strings.StringHelper;
import de.s42.dl.DLAttribute.AttributeDL;
import de.s42.dl.ui.events.EventAction;
import java.awt.Rectangle;
import java.util.Optional;
import javax.swing.JComponent;

/**
 *
 * @author Benjamin Schiller
 * @param <ComponentType>
 */
public abstract class AbstractComponent<ComponentType> implements Component<ComponentType>
{

	public final static String COMPONENT_CLIENT_PROPERTY_KEY = "DLComponent";

	protected String name;

	@AttributeDL(required = false)
	protected Rectangle bounds;

	@AttributeDL(required = false)
	protected EventAction onCreate;

	@AttributeDL(required = false, defaultValue = "0")
	protected int gridX = 0;

	@AttributeDL(required = false, defaultValue = "0")
	protected int gridY = 0;

	@AttributeDL(required = false, defaultValue = "0.0")
	protected float weightX = 0.0f;

	@AttributeDL(required = false, defaultValue = "0.0")
	protected float weightY = 0.0f;

	public static <AbstractComponentType extends AbstractComponent> Optional<AbstractComponentType> getComponent(JComponent jComponent)
	{
		Object comp = jComponent.getClientProperty(COMPONENT_CLIENT_PROPERTY_KEY);

		if (comp instanceof AbstractComponent) {
			return Optional.of((AbstractComponentType) comp);
		}

		return Optional.empty();
	}

	protected void initComponent(JComponent component)
	{
		component.putClientProperty(COMPONENT_CLIENT_PROPERTY_KEY, this);

		if (bounds != null) {
			component.setBounds(bounds);
			component.setPreferredSize(bounds.getSize());
			component.setSize(bounds.getSize());
			component.setMaximumSize(bounds.getSize());
			component.setMinimumSize(bounds.getSize());
		}

		onCreate(component);
	}

	@Override
	public String toString()
	{
		return StringHelper.toString(this);
	}

	public void onCreate(Object component)
	{
		if (onCreate != null) {
			try {
				onCreate.perform(new CreateComponentEvent(this, component));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	@Override
	public void setBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	public EventAction getOnCreate()
	{
		return onCreate;
	}

	public void setOnCreate(EventAction onCreate)
	{
		this.onCreate = onCreate;
	}

	@Override
	public int getGridX()
	{
		return gridX;
	}

	@Override
	public void setGridX(int gridX)
	{
		this.gridX = gridX;
	}

	@Override
	public int getGridY()
	{
		return gridY;
	}

	@Override
	public void setGridY(int gridY)
	{
		this.gridY = gridY;
	}

	@Override
	public float getWeightX()
	{
		return weightX;
	}

	@Override
	public void setWeightX(float weightX)
	{
		this.weightX = weightX;
	}

	@Override
	public float getWeightY()
	{
		return weightY;
	}

	@Override
	public void setWeightY(float weightY)
	{
		this.weightY = weightY;
	}
	//</editor-fold>	
}
