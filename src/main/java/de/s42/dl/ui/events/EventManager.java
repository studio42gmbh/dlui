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

import de.s42.dl.DLAttribute.AttributeDL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Benjamin Schiller
 */
public class EventManager
{

	@AttributeDL(ignore = true)
	protected final Map<String, List<EventAction>> actionsByEvent = new HashMap<>();

	protected List<EventAction> getReceivers(Event event)
	{
		List<EventAction> result = actionsByEvent.get(event.getName());

		if (result != null) {
			return result;
		}

		return Collections.EMPTY_LIST;
	}

	public void send(String eventName) throws Exception
	{
		send(new DefaultEvent(eventName));
	}

	public void send(Event event) throws Exception
	{
		for (EventAction action : getReceivers(event)) {
			action.perform(event);
		}
	}

	public synchronized void register(String eventName, EventAction action)
	{
		List<EventAction> actions = actionsByEvent.get(eventName);

		if (actions == null) {
			actions = new ArrayList<>();
			actionsByEvent.put(eventName, actions);
		}

		actions.add(action);
	}
}
