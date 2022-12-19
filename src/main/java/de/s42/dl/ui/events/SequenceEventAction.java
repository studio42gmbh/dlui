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

import de.s42.dl.ui.actions.*;
import de.s42.dl.DLAttribute.AttributeDL;
import de.s42.dl.types.DLContainer;
import de.s42.log.LogManager;
import de.s42.log.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benjamin Schiller
 * @param <EventType>
 */
public class SequenceEventAction<EventType extends Event> implements EventAction<EventType>, DLContainer<Action>
{

	private final static Logger log = LogManager.getLogger(SequenceEventAction.class.getName());

	@AttributeDL(ignore = true)
	protected List<Action> actions = new ArrayList<>();

	@Override
	public void perform(EventType event) throws Exception
	{
		for (Action action : actions) {
			if (action instanceof EventAction) {
				((EventAction)action).perform(event);
			}
			else {
				action.perform();
			}
		}
	}

	@Override
	public void addChild(String name, Action child)
	{
		assert child != null;

		actions.add(child);
	}
}
