package fr.theoszanto.voxatile.ldraw;

import fr.ign.voxatile.core.placeables.Placeable;
import fr.ign.voxatile.core.utils.world3d.WorldCoords3d;
import fr.ign.voxatile.core.world.VoxelTile;

/**
 * {@code LDrawBrick} class implements a {@link Placeable} voxel for LDraw.
 * A voxel in LDraw, known as brick, consists mainly of two parameters:
 * a reference to a part from the bricks library and a color number.
 * <p>
 * For bricks smaller or bigger than a standard voxel, better alignment
 * can be done using the {@code lduOffset}.
 * <p>
 * LDraw uses a right-handed co-ordinate system where -Y is "up".
 * LDraw Units (LDU) can be seen on the following illustration:
 * <p>
 * <img src="https://www.ldraw.org/uploads/images/Articles/dim.png" />
 * <p>
 * Note that this module uses a 2x2 brick as the voxel size, so one
 * voxel is 40 LDU wide/long and 24 LDU height.
 *
 * @param ref The brick reference string
 * @param color The color ID
 * @param lduOffset The brick placement offset in LDraw Units (LDU)
 * @param rotationAroundY The rotation angle in degrees around the LDraw Y axis (vertical)
 *
 * @see <a href="https://library.ldraw.org/parts/list">LDraw bricks</a>
 * @see <a href="https://www.ldraw.org/article/547.html">LDraw colors</a>
 * @see <a href="https://www.ldraw.org/article/218.html#coords">LDraw coordinates and units</a>
 */
public record LDrawBrick(String ref, int color, WorldCoords3d lduOffset, double rotationAroundY) implements Placeable {
	/**
	 * Fake brick, representing "no brick".
	 */
	public static final LDrawBrick NO_BRICK = new LDrawBrick("", -1, new WorldCoords3d(0, 0, 0), 0);

	/**
	 * {@return whether this brick is the special "no brick" placeholder}
	 */
	public boolean isNoBrick() {
		return ref.isEmpty() && color == -1;
	}

	@Override
	public void place(VoxelTile tile, int x, int y, int z) {
		place(tile, new WorldCoords3d(x, y, z));
	}

	@Override
	public void place(VoxelTile tile, WorldCoords3d position) {
		if (!(tile instanceof LDrawTile ldrTile))
			throw new IllegalArgumentException("LDraw brick can only be placed in a LDraw world");
		ldrTile.setBrick(position, this);
	}
}
