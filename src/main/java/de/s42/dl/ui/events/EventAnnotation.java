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
package de.s42.dl.ui.events;

import de.s42.dl.exceptions.InvalidAnnotation;
import de.s42.dl.*;
import de.s42.dl.DLAnnotated.DLMappedAnnotation;
import de.s42.dl.annotations.AbstractDLAnnotation;
import de.s42.dl.exceptions.DLException;
import de.s42.log.LogManager;
import de.s42.log.Logger;

/**
 *
 * @author Benjamin Schiller
 */
public class EventAnnotation extends AbstractDLAnnotation
{

	private final static Logger log = LogManager.getLogger(EventAnnotation.class.getName());

	public final static String DEFAULT_SYMBOL = "event";

	public EventAnnotation()
	{
		this(DEFAULT_SYMBOL);
	}

	public EventAnnotation(String name)
	{
		super(name);
	}

	@Override
	public void bindToInstance(DLCore core, DLModule module, DLInstance instance, Object... parameters) throws DLException
	{
		assert core != null;
		assert instance != null;

		Object[] params = validateParameters(parameters, new Class[]{String.class});
		String eventName = (String) params[0];

		// Ensure an Action is tagged
		if (!instance.getType().isDerivedTypeOf(core.getType(EventAction.class).orElseThrow())) {
			throw new InvalidAnnotation("the event annotation can only be used to tag Action types");
		}

		log.debug("EVENT", eventName, instance.getName());
	}

	public static String getEventName(DLMappedAnnotation mappedAnnotation) throws InvalidAnnotation
	{
		if (!mappedAnnotation.getAnnotation().getClass().equals(EventAnnotation.class)) {
			throw new InvalidAnnotation("Mapped Annotation is not EventAnnotation");
		}

		Object[] params = validateParameters(mappedAnnotation.getParameters(), new Class[]{String.class});

		return (String) params[0];
	}
}
