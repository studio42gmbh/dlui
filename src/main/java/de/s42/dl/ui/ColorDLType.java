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
package de.s42.dl.ui;

import de.s42.base.conversion.ConversionHelper;
import de.s42.dl.exceptions.InvalidValue;
import de.s42.dl.types.DefaultDLType;
import java.awt.Color;

/**
 *
 * @author Benjamin Schiller
 */
public class ColorDLType extends DefaultDLType
{

	public ColorDLType()
	{
		super(Color.class);
		setComplexType(false);
	}

	@Override
	public Object read(Object... sources) throws InvalidValue
	{
		assert sources != null;

		if ((sources.length == 1) && sources[0] instanceof Color) {
			return sources[0];
		}

		// Split given a 1 string parameter into 4
		if ((sources.length == 1) && sources[0] instanceof String) {
			
			if (((String)sources[0]).startsWith("#")) {
				return Color.decode((String)sources[0]);
			}
			else {
				sources = ((String) sources[0]).split("\\s*,\\s*");
			}
		}
		
		if (sources.length != 4) {
			throw new InvalidValue(getName() + " has to be <float r>, <float g>, <float b>, <float a> but has " + sources.length + " parameters");
		}

		Float[] bounds = ConversionHelper.convertArray(sources, Float.class);

		return new Color(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
}
