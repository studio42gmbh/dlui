// <editor-fold desc="The F12 License" defaultstate="collapsed">
/*
 * Copyright F12 Studio s.r.o 2022. All rights reserved.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * For details to the License read https://www.f12-studio.com/license
 */
//</editor-fold>
package de.s42.dl.ui.pragmas;

import de.s42.base.compile.CompileHelper;
import de.s42.base.compile.InvalidCompilation;
import de.s42.base.files.FilesHelper;
import de.s42.dl.DLCore;
import de.s42.dl.core.BaseDLCore;
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

			((BaseDLCore) core).setClassLoader(closeActionClass.getClassLoader());
			core.defineType(core.createType(closeActionClass));
			((BaseDLCore) core).setClassLoader(null);

		} catch (InvalidCompilation | DLException | IOException ex) {
			throw new InvalidPragma("Error loading plugin - " + ex.getMessage(), ex);
		}
	}
}
