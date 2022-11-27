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
package de.s42.dl.ui.pragmas;

import de.s42.base.compile.CompileHelper;
import de.s42.base.compile.InvalidCompilation;
import de.s42.base.files.FilesHelper;
import de.s42.dl.DLCore;
import de.s42.dl.exceptions.DLException;
import de.s42.dl.exceptions.InvalidPragma;
import de.s42.dl.pragmas.AbstractDLPragma;
import de.s42.log.LogManager;
import de.s42.log.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Benjamin Schiller
 */
public class LoadPluginClassPragma extends AbstractDLPragma
{

	private final static Logger log = LogManager.getLogger(LoadPluginClassPragma.class.getName());

	public final static String DEFAULT_IDENTIFIER = "loadPluginClass";

	public LoadPluginClassPragma()
	{
		super(DEFAULT_IDENTIFIER);
	}

	public LoadPluginClassPragma(String identifier)
	{
		super(identifier);
	}

	@Override
	public void doPragma(DLCore core, Object... parameters) throws InvalidPragma
	{
		try {
			assert core != null;

			parameters = validateParameters(parameters, new Class[]{String.class, Path.class});

			String className = (String) parameters[0];
			Path classFile = (Path) parameters[1];

			//log.debug("Loading plugin class", className, "from file", classFile.toAbsolutePath());
			
			log.debug(FilesHelper.createMavenNetbeansFileConsoleLink("Loading plugin class " + className + " from file",
				classFile.getFileName().toString(),
				classFile.toAbsolutePath().toString(), 1, 1, false));

			log.start("CompilationTime");
			Class closeActionClass = CompileHelper.getCompiledClass(
				Files.readString(classFile),
				className
			);
			log.stopInfo("CompilationTime");

			core.defineType(core.createType(closeActionClass));
		} catch (InvalidCompilation | DLException | IOException ex) {
			throw new InvalidPragma("Error loading plugin - " + ex.getMessage(), ex);
		}
	}
}
