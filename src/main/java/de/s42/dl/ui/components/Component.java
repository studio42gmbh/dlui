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

import java.awt.Insets;
import java.awt.Rectangle;

/**
 *
 * @author Benjamin Schiller
 * @param <ComponentType>
 */
public interface Component<ComponentType>
{

	public ComponentType createSwingComponent();

	public Rectangle getBounds();

	public void setBounds(Rectangle bounds);

	public String getName();

	public void setName(String name);

	public int getGridX();

	public void setGridX(int gridX);

	public int getGridY();

	public void setGridY(int gridY);

	public int getGridWidth();

	public void setGridWidth(int gridWidth);

	public int getGridHeight();

	public void setGridHeight(int gridHeight);
	
	public float getWeightX();

	public void setWeightX(float weightX);

	public float getWeightY();

	public void setWeightY(float weightY);

	public Fill getFill();

	public void setFill(Fill fill);
	
	public Anchor getAnchor();

	public void setAnchor(Anchor anchor);
	
	public Insets getInsets();
	
	public void setInsets(Insets insets);
}
