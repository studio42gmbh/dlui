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
package de.s42.dl.ui.events;

import de.s42.dl.DLAttribute.AttributeDL;
import de.s42.log.LogManager;
import de.s42.log.Logger;
import java.util.function.Function;

/**
 *
 * @author Benjamin Schiller
 * @param <EventType>
 */
public class SendEventAction<EventType extends Event> implements EventAction<EventType>
{

	private final static Logger log = LogManager.getLogger(SendEventAction.class.getName());

	@AttributeDL(required = true)
	protected EventManager eventManager;

	@AttributeDL(ignore = true)
	protected Function<EventType, Event> transform;

	public SendEventAction()
	{
	}

	public SendEventAction(EventManager eventManager)
	{
		this.eventManager = eventManager;
	}
	
	public SendEventAction(EventManager eventManager, Function<EventType, Event> transform)
	{
		this.eventManager = eventManager;
		this.transform = transform;
	}
	

	@Override
	public void perform(EventType event) throws Exception
	{
		assert event != null;


		if (transform != null) {
			Event transformed = transform.apply(event);
			log.debug("Sending event", event.getName(), "transformed to", transformed.getName());
			eventManager.send(transformed);
		} else {
			log.debug("Sending event", event.getName());
			eventManager.send(event);
		}
	}

	// <editor-fold desc="Getters/Setters" defaultstate="collapsed">
	public EventManager getEventManager()
	{
		return eventManager;
	}

	public void setEventManager(EventManager eventManager)
	{
		this.eventManager = eventManager;
	}

	public Function<EventType, Event> getTransform()
	{
		return transform;
	}

	public void setTransform(Function<EventType, Event> transform)
	{
		this.transform = transform;
	}
	//</editor-fold>
}
