package fr.theoszanto.voxatile.ldraw;

import fr.ign.voxatile.core.generation.SquareUnitsTileGenerator;
import fr.ign.voxatile.core.utils.FileHelpers;
import fr.ign.voxatile.core.utils.world2d.WorldBBox2d;
import fr.ign.voxatile.core.utils.world2d.WorldCoords2d;
import fr.ign.voxatile.core.utils.world3d.WorldBBox3d;
import fr.ign.voxatile.core.utils.world3d.WorldCoords3d;
import fr.ign.voxatile.core.world.MapWriteException;
import fr.ign.voxatile.core.world.VoxelTile;
import fr.ign.voxatile.core.world.VoxelWorld;
import fr.ign.voxatile.core.world.VoxelWorldMetadata;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link VoxelWorld} that creates a LDraw model.
 */
public class LDrawWorld extends VoxelWorld {
	private final File destination;
	private final File tiles;
	private final Map<String, WorldCoords2d> tileOffsets = new HashMap<>();

	// Not at all hard limits, but LDraw is a text-based format
	// and such large models would realistically be unusable.
	private static final WorldBBox3d MAX_LIMIT = new WorldBBox3d(
			new WorldCoords3d(-50000, -50000, -50000),
			new WorldCoords3d(+50000, +50000, +50000)
	);

	/**
	 * Constructs a new {@code LDrawWorld}.
	 * The limits of the world have to be set using {@link #setLimits(WorldBBox3d)}
	 *
	 * @param destination Directory where to save data to. If null nothing is saved.
	 */
	public LDrawWorld(File destination) {
		super(new VoxelWorldMetadata());
		this.destination = destination;
		this.tiles = new File(destination, "tiles");
	}

	@Override
	public WorldBBox3d maxLimits() {
		return MAX_LIMIT;
	}

	@Override
	public VoxelTile newTile(WorldBBox3d limits) {
		String name = "t.%d.%d.ldr".formatted(limits.minX(), limits.minY());
		tileOffsets.put(name, limits.min().to2d());
		return new LDrawTile(new File(tiles, name), limits);
	}

	@Override
	public void initialize() throws MapWriteException {
		if (destination == null)
			return;
		if (!tiles.isDirectory() && tiles.mkdir())
			throw new MapWriteException("Tiles directory %s cannot be created".formatted(tiles));
	}

	@Override
	public void finalizeAndSave() throws MapWriteException {
		if (destination == null)
			return;
		StringBuilder out = new StringBuilder();
		tileOffsets.forEach((name, offset) -> {
			// XYZ => X/-Z/Y
			int x = offset.x() * 40;
			int z = offset.y() * 40;
			out.append("1 16 ").append(x).append(" 0 ").append(z).append(" 1 0 0 0 1 0 0 0 1 tiles/").append(name).append("\r\n");
		});
		try {
			FileHelpers.write(new File(destination, generateLDrawFileName()), out.toString());
		} catch (IOException e) {
			throw new MapWriteException(e);
		}
	}

	@Override
	public Collection<WorldBBox2d> tiles(int maxTileSize) {
		// No reason to define a specific minimum unit size, tiles can have any size
		return new SquareUnitsTileGenerator(1, limits().to2d()).getTiles(maxTileSize);
	}

	private String generateLDrawFileName() {
		return Objects.requireNonNullElse(metadata.getWorldName(), "voxatile")
				.toLowerCase()
				.replaceAll("[^a-z0-9]+", "_") + ".ldr";
	}
}
