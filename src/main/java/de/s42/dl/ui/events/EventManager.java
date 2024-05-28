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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Benjamin Schiller
 */
public class EventManager
{

	//private final static Logger log = LogManager.getLogger(EventManager.class.getName());
	@AttributeDL(ignore = true)
	protected final Map<String, List<WeakReference<EventAction>>> actionsByEvent = new HashMap<>();

	public void send(String eventName) throws Exception
	{
		assert eventName != null : "eventName != null";

		send(new DefaultEvent(eventName));
	}

	public void send(Event event) throws Exception
	{
		assert event != null : "event != null";

		boolean cleanup = false;

		for (WeakReference<EventAction> actionRef : new ArrayList<>(getReceivers(event.getName()))) {

			EventAction action = actionRef.get();

			if (action != null) {
				action.perform(event);
			} else {
				cleanup = true;
			}
		}

		if (cleanup) {
			cleanupReceivers(getReceivers(event.getName()));
		}
	}

	public synchronized void registerWeak(String eventName, EventAction action)
	{
		assert eventName != null : "eventName != null";
		assert action != null : "action != null";

		List<WeakReference<EventAction>> actions = actionsByEvent.get(eventName);

		if (actions == null) {
			actions = new ArrayList<>();
			actionsByEvent.put(eventName, actions);
		}

		actions.add(new WeakReference(action));
	}

	public synchronized boolean unregisterWeak(String eventName, EventAction action)
	{
		assert eventName != null : "eventName != null";
		assert action != null : "action != null";

		//log.debug("unregisterWeak", eventName);
		List<WeakReference<EventAction>> actions = actionsByEvent.get(eventName);

		if (actions != null) {
			for (WeakReference<EventAction> act : actions) {
				if (act.refersTo(action)) {
					actions.remove(act);
					//log.debug("unregisterWeak:removing", eventName);
					return true;
				}
			}
		}

		return false;
	}

	protected List<WeakReference<EventAction>> getReceivers(String eventName)
	{
		assert eventName != null : "eventName != null";

		List<WeakReference<EventAction>> result = actionsByEvent.get(eventName);

		if (result != null) {
			return result;
		}

		return Collections.EMPTY_LIST;
	}

	/**
	 * Iterates all listeners and removed those that are cleared
	 *
	 * @param receivers
	 * @return
	 */
	protected boolean cleanupReceivers(List<WeakReference<EventAction>> receivers)
	{
		assert receivers != null : "receivers != null";

		boolean cleared = false;

		synchronized (receivers) {

			Iterator<WeakReference<EventAction>> iterator = receivers.iterator();

			while (iterator.hasNext()) {

				WeakReference<EventAction> ref = iterator.next();

				EventAction action = ref.get();

				// Listener is cleared -> remove
				if (action == null) {
					//log.debug("clearListeners:clearedListener");
					iterator.remove();
					cleared = true;
				}
			}
		}

		return cleared;
	}

	/**
	 * Cleans up all event listener lists by event name
	 *
	 * @return
	 */
	public boolean cleanup()
	{
		boolean cleanup = false;
		for (String eventName : actionsByEvent.keySet()) {
			cleanup &= cleanupReceivers(getReceivers(eventName));
		}

		return cleanup;
	}

	/**
	 * Cleans up event listener list for one event name
	 *
	 * @param eventName
	 * @return
	 */
	public boolean cleanup(String eventName)
	{
		assert eventName != null : "eventName != null";

		return cleanupReceivers(getReceivers(eventName));
	}
}
