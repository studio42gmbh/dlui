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

import de.s42.base.beans.BeanHelper;
import de.s42.base.beans.BeanInfo;
import de.s42.base.beans.BeanProperty;
import de.s42.base.beans.InvalidBean;
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
 * Default implementation of a Binding using the Bean pattern to access the object data. ATTENTION : This binding adds
 * the listeners via WeakReferences so the client has to hold a strong reference to make sure the listener does not get
 * cleared!
 *
 * @author Benjamin Schiller
 * @param <ObjectType> The object to which is bound
 * @param <DataType> The data type of the bound data
 */
public class DefaultBinding<ObjectType, DataType> implements Binding<ObjectType, DataType>
{

	private final static Logger log = LogManager.getLogger(DefaultBinding.class.getName());

	protected final ObjectType object;
	protected final String name;

	protected final BeanInfo<ObjectType> info;
	protected final BeanProperty<ObjectType, DataType> property;

	protected final List<WeakReference<Consumer<Optional<DataType>>>> listeners = new ArrayList<>();

	protected DataType currentValue;

	public DefaultBinding(ObjectType object, String name) throws InvalidBean
	{
		assert object != null;
		assert name != null;

		this.object = object;
		this.name = name;

		info = (BeanInfo<ObjectType>) BeanHelper.getBeanInfo(object.getClass());

		property = (BeanProperty<ObjectType, DataType>) info.getProperty(name).orElseThrow(() -> {
			return new InvalidBean("Missing property '" + name + "'");
		});

		currentValue = property.read(object);
	}

	@Override
	public ObjectType getObject()
	{
		return object;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Class<? extends DataType> getDataType()
	{
		return property.getPropertyClass();
	}

	@Override
	public Optional<DataType> getValue()
	{
		try {
			return Optional.ofNullable(property.read(object));
		} catch (InvalidBean ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void setValue(DataType value)
	{
		//log.debug("setValue", getName(), value);

		try {
			// Just update if the value has changed			
			if (!Objects.equals(currentValue, value)) {

				//log.debug("setValue:update", getName(), value);
				currentValue = value;
				property.write(object, value);
				updateListeners(value);
			}

		} catch (InvalidBean ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Is intended to be called by referenced data object to lny updtae but not set the property into it again.
	 *
	 * @param value
	 */
	public void updateValue(DataType value)
	{
		//log.debug("updateValue", getName(), value);
		// Just update if the value has changed			
		if (!Objects.equals(currentValue, value)) {

			//log.debug("updateValue:update", getName(), value);
			currentValue = value;
			updateListeners(value);
		}
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
				if (listener != null && listener.equals(lst)) {
					//log.debug("removeChangeListener:removedListener", listener);
					iterator.remove();
					removed = true;
				} // Ref has been cleared -> remove from list
				else if (listener == null) {
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
	public boolean clearListeners()
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
