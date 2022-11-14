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
package de.s42.dl.ui;

import de.s42.dl.DLCore;
import de.s42.dl.DLModule;
import de.s42.dl.exceptions.DLException;
import de.s42.dl.ui.actions.AbstractAction;
import de.s42.dl.ui.actions.Action;
import de.s42.dl.ui.actions.DebugAction;
import de.s42.dl.ui.actions.RefAction;
import de.s42.dl.ui.events.SendEventAction;
import de.s42.dl.ui.actions.SequenceAction;
import de.s42.dl.ui.annotations.ZoneAnnotation;
import de.s42.dl.ui.components.AbstractComponent;
import de.s42.dl.ui.components.AbstractContainer;
import de.s42.dl.ui.components.button.Button;
import de.s42.dl.ui.components.Component;
import de.s42.dl.ui.components.CreateComponentEvent;
import de.s42.dl.ui.components.Label;
import de.s42.dl.ui.components.Panel;
import de.s42.dl.ui.components.Text;
import de.s42.dl.ui.components.TextComponent;
import de.s42.dl.ui.components.dropdown.Dropdown;
import de.s42.dl.ui.components.window.CloseWindowAction;
import de.s42.dl.ui.components.window.CloseWindowEvent;
import de.s42.dl.ui.components.window.Window;
import de.s42.dl.ui.events.DefaultEvent;
import de.s42.dl.ui.events.Event;
import de.s42.dl.ui.events.EventAction;
import de.s42.dl.ui.events.EventAnnotation;
import de.s42.dl.ui.events.EventManager;
import de.s42.dl.ui.events.RefEventAction;
import de.s42.dl.ui.events.SequenceEventAction;
import de.s42.dl.ui.pragmas.LoadPluginClassPragma;
import de.s42.log.LogManager;
import de.s42.log.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Benjamin Schiller
 */
public final class UICoreHelper
{

	private final static Logger log = LogManager.getLogger(UICoreHelper.class.getName());

	private UICoreHelper()
	{
		// never instantiated
	}

	public static void init(DLCore core) throws DLException, IOException
	{
		// Pragmas
		core.definePragma(new LoadPluginClassPragma());

		// Annotations
		core.defineAnnotation(new ZoneAnnotation());
		core.defineAnnotation(new EventAnnotation());

		// Core types
		core.defineType(new ColorDLType(), "Color");
		core.defineType(new RectangleDLType(), "Rectangle");
		core.defineType(new InsetsDLType(), "Insets");
		core.defineType(new PointDLType(), "Point");
		core.defineType(core.createType(Font.class), "Font");

		// Event types
		core.defineType(core.createType(Event.class), "Event");
		core.defineType(core.createType(DefaultEvent.class), "DefaultEvent");
		core.defineType(core.createType(EventManager.class), "EventManager");

		// Action types
		core.defineType(core.createType(Action.class), "Action");
		core.defineType(core.createType(AbstractAction.class), "AbstractAction");
		core.defineType(core.createType(EventAction.class), "EventAction");
		core.defineType(core.createType(RefAction.class), "RefAction");
		core.defineType(core.createType(SendEventAction.class), "SendEventAction");
		core.defineType(core.createType(SequenceAction.class), "SequenceAction");
		core.defineType(core.createType(SequenceEventAction.class), "SequenceEventAction");
		core.defineType(core.createType(RefEventAction.class), "RefEventAction");
		core.defineType(core.createType(DebugAction.class), "DebugAction");

		// Component types
		core.defineType(core.createType(Component.class), "Component");
		core.defineType(core.createType(CreateComponentEvent.class), "CreateComponentEvent");
		core.defineType(core.createType(AbstractComponent.class), "AbstractComponent");
		core.defineType(core.createType(AbstractContainer.class), "AbstractContainer");
		core.defineType(core.createType(TextComponent.class), "TextComponent");
		core.defineType(core.createType(Button.class), "Button");
		core.defineType(core.createType(Label.class), "Label");
		core.defineType(core.createType(Text.class), "Text");
		core.defineType(core.createType(Panel.Layout.class), "PanelLayout");
		core.defineType(core.createType(Panel.class), "Panel");
		core.defineType(core.createType(Dropdown.class), "Dropdown");

		// Allow to access window
		core.defineType(core.createType(Window.class), "Window");
		core.defineType(core.createType(CloseWindowAction.class), "CloseWindowAction");
		core.defineType(core.createType(CloseWindowEvent.class), "CloseWindowEvent");
	}

	/*	public static void loadL10N(DLCore core, String l10nPath) throws DLException, IOException
	{
		DefaultDLType i18nType = new DefaultDLType(I18N.class.getName());
		i18nType.setAllowDynamicAttributes(true);
		core.defineType(i18nType);
		I18N i18n = new I18N(i18nType);
		i18n.init(l10nPath);
		core.addExported(i18n);
	}
	 */
	public static void loadPlugins(DLCore core, Path pluginPath) throws IOException
	{
		Files.find(pluginPath, 4, (file, attributes) -> {
			return file.getFileName().toString().equals("plugin.dl");
		}).forEach((file) -> {
			try {
				log.debug("Loading plugin", file.toAbsolutePath());
				core.parse(file.toString());
			} catch (DLException ex) {
				log.error(ex, "Error loading plugin", file.toAbsolutePath());
			}
		});
	}

	public static Window createWindow(DLCore core, String windowFile) throws DLException
	{
		assert core != null;
		assert windowFile != null;

		DLModule module = core.parse(windowFile);

		Window window = (Window) module.getChildAsJavaObject(core.getType(Window.class).orElseThrow(), core).orElseThrow();

		return window;
	}
}
