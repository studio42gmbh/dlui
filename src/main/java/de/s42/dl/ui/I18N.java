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

import de.s42.base.resources.ResourceHelper;
import de.s42.dl.DLCore;
import de.s42.dl.DLInstance;
import de.s42.dl.DLType;
import de.s42.dl.annotations.DontPersistDLAnnotation.dontPersist;
import de.s42.dl.instances.DefaultDLInstance;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Benjamin Schiller
 */
@dontPersist
public class I18N extends DefaultDLInstance
{

	public final static String DEFAULT_INSTANCE_NAME = "i18n";

	public I18N(DLType type)
	{
		super(type, DEFAULT_INSTANCE_NAME);
	}

	public void init(String source) throws IOException
	{
		assert source != null;

		// Load given properties file as attributes of this instance
		Properties prop = new Properties();
		try ( InputStream in = ResourceHelper.getResourceAsStream(source)) {
			prop.load(in);

			for (Map.Entry<Object, Object> entry : prop.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();

				set(key, value);
			}
		}
	}

	@Override
	public Object get(String key)
	{
		Object text = super.get(key);

		if (text != null) {
			return text;
		}
		
		return "<" + key + ">";
	}

	@Override
	public I18N toJavaObject()
	{
		return this;
	}

	@Override
	public void addChild(DLInstance child)
	{
		throw new UnsupportedOperationException("may not add children to this instance");
	}

	@Override
	public void setType(DLType type)
	{
		throw new UnsupportedOperationException("may not change type of this instance");
	}
}
