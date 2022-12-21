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
import de.s42.dl.types.DLContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Benjamin Schiller
 * @param <ComponentType>
 * @param <ContainedComponentType>
 */
public abstract class AbstractContainer<ComponentType, ContainedComponentType> extends AbstractComponent<ComponentType> implements DLContainer<ContainedComponentType>
{

	@AttributeDL(ignore = true)
	protected final List<ContainedComponentType> components = new ArrayList<>();

	@Override
	public void addChild(String name, ContainedComponentType child)
	{
		assert child != null;

		addComponent(child);
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public List<ContainedComponentType> getComponents()
	{
		return Collections.unmodifiableList(components);
	}

	public void setComponents(List<ContainedComponentType> components)
	{
		assert components != null;

		this.components.clear();
		this.components.addAll(components);
	}

	public void addComponent(ContainedComponentType component)
	{
		assert component != null;

		components.add(component);
	}
	//</editor-fold>
}
