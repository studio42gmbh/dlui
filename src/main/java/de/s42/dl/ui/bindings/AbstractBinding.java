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
package de.s42.dl.ui.bindings;

import de.s42.log.LogManager;
import de.s42.log.Logger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Abstract base implementation of a Binding using the Bean pattern to access the object data. ATTENTION : This binding
 * adds the listeners via WeakReferences so the client has to hold a strong reference to make sure the listener does not
 * get cleared!
 *
 * @author Benjamin Schiller
 * @param <ObjectType> The object to which is bound
 * @param <DataType> The data type of the bound data
 */
public abstract class AbstractBinding<ObjectType, DataType> implements Binding<ObjectType, DataType>
{

	private final static Logger log = LogManager.getLogger(AbstractBinding.class.getName());

	protected final List<WeakReference<Consumer<Optional<DataType>>>> listeners;
	
	protected AbstractBinding()
	{
		listeners = new ArrayList<>();
	}
	
	protected AbstractBinding(List<WeakReference<Consumer<Optional<DataType>>>> listeners)
	{
		this.listeners = listeners;
	}	

	protected void updateListeners(DataType value)
	{
		Optional<DataType> opt = Optional.ofNullable(value);

		synchronized (listeners) {

			Iterator<WeakReference<Consumer<Optional<DataType>>>> iterator = listeners.iterator();

			while (iterator.hasNext()) {

				WeakReference<Consumer<Optional<DataType>>> ref = iterator.next();

				Consumer<Optional<DataType>> listener = ref.get();

				// Listener is mapped -> send update
				if (listener != null) {
					listener.accept(opt);
				} // Ref has been cleared -> remove from list
				else {
					//log.debug("updateListeners:clearedListener");
					iterator.remove();
				}
			}
		}
	}

	@Override
	public boolean addWeakChangeListener(Consumer<Optional<DataType>> listener)
	{
		assert listener != null;

		synchronized (listeners) {

			if (listeners.stream().noneMatch((ref) -> {
				return Objects.equals(ref.get(), listener);
			})) {
				return listeners.add(new WeakReference(listener));
			}
		}

		return false;
	}

	@Override
	public boolean removeWeakChangeListener(Consumer<Optional<DataType>> listener)
	{
		assert listener != null;

		boolean removed = false;

		synchronized (listeners) {

			Iterator<WeakReference<Consumer<Optional<DataType>>>> iterator = listeners.iterator();

			while (iterator.hasNext()) {

				WeakReference<Consumer<Optional<DataType>>> ref = iterator.next();

				Consumer<Optional<DataType>> lst = ref.get();

				// Listener is mapped and equal -> remove
				if (lst != null && listener.equals(lst)) {
					//log.debug("removeChangeListener:removedListener", listener);
					iterator.remove();
					removed = true;
				} // Ref has been cleared -> remove from list
				else if (lst == null) {
					//log.debug("removeChangeListener:clearedListener");
					iterator.remove();
				}
			}
		}

		return removed;
	}

	/**
	 * Iterates all listeners and removed those that are cleared
	 *
	 * @return
	 */
	public boolean cleanupListeners()
	{
		boolean cleared = false;

		synchronized (listeners) {

			Iterator<WeakReference<Consumer<Optional<DataType>>>> iterator = listeners.iterator();

			while (iterator.hasNext()) {

				WeakReference<Consumer<Optional<DataType>>> ref = iterator.next();

				Consumer<Optional<DataType>> listener = ref.get();

				// Listener is cleared -> remove
				if (listener == null) {
					//log.debug("clearListeners:clearedListener");
					iterator.remove();
					cleared = true;
				}
			}
		}

		return cleared;
	}
}
