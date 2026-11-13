package fr.theoszanto.voxatile.ldraw;

import fr.ign.voxatile.core.parameters.OutputFormat;
import fr.ign.voxatile.core.parameters.ParamsParser;
import fr.ign.voxatile.core.utils.modules.Module;

/**
 * LDraw format (LEGO®) module.
 * @see <a href="https://www.ldraw.org/article/218.html">LDraw File Format Specification</a>
 */
public class LDrawModule extends Module {
	@Override
	public void registerParams(ParamsParser parser) {
		parser.registerFormat("vox", new OutputFormat(LDrawWorld::new, LDrawBrickParams.class, LDrawBrickParams::fromString));
	}
}
