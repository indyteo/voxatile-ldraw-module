package fr.theoszanto.voxatile.ldraw;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.ign.voxatile.core.parameters.placeables.PlaceableParams;
import fr.ign.voxatile.core.placeables.Placeable;
import fr.ign.voxatile.core.utils.random.Seed;
import fr.ign.voxatile.core.utils.world3d.WorldCoords3d;

import java.beans.ConstructorProperties;

/**
 * Parameters for LDraw bricks with only brick reference and optional color.
 */
public class LDrawBrickParams extends PlaceableParams {
	/**
	 * Brick reference (required).
	 */
	@JsonSetter(nulls = Nulls.FAIL)
	public String ref;

	/**
	 * Brick color ID.
	 */
	@JsonSetter(nulls = Nulls.SKIP)
	public int color = 16;

	/**
	 * X-coordinate of the brick placement offset, in LDraw Units.
	 */
	@JsonSetter(nulls = Nulls.SKIP)
	public int lduOffsetX = 0;

	/**
	 * Y-coordinate of the brick placement offset, in LDraw Units.
	 * Note that Y axis points downward in LDU!
	 */
	@JsonSetter(nulls = Nulls.SKIP)
	public int lduOffsetY = 0;

	/**
	 * Z-coordinate of the brick placement offset, in LDraw Units.
	 */
	@JsonSetter(nulls = Nulls.SKIP)
	public int lduOffsetZ = 0;

	/**
	 * The rotation angle in degrees around the LDraw Y axis (vertical).
	 */
	@JsonSetter(nulls = Nulls.SKIP)
	public double rotationAroundY = 0;

	/**
	 * Creates a new {@code LDrawBrickParams}.
	 *
	 * @param ref Brick reference
	 */
	@ConstructorProperties("ref")
	public LDrawBrickParams(String ref) {
		this.ref = ref;
	}

	private boolean isSpecialNoBrick() {
		return ref.isEmpty() && color == -1;
	}

	@Override
	public void validate() {
		if (isSpecialNoBrick())
			return;

		if (ref.isBlank())
			throw new IllegalArgumentException("ref should not be empty or blank");
		if (color < 0)
			throw new IllegalArgumentException("color should be a positive integer");
	}

	@Override
	public Placeable create(Seed seed) {
		if (isSpecialNoBrick())
			return LDrawBrick.NO_BRICK;
		return new LDrawBrick(ref, color, new WorldCoords3d(lduOffsetX, lduOffsetY, lduOffsetZ), rotationAroundY);
	}

	/**
	 * Creates a params instance from the brick short form.
	 * @param str brick ref and color string
	 * @return created params instance
	 */
	public static LDrawBrickParams fromString(String str) {
		// Special "no brick"
		if (str.equals("noBrick")) {
			LDrawBrickParams params = new LDrawBrickParams("");
			params.color = -1;
			return params;
		}

		String[] parts = str.split("\\s+");
		if (parts.length != 2)
			throw new IllegalArgumentException("Invalid brick string format: " + str);
		LDrawBrickParams params = new LDrawBrickParams(parts[0]);
		try {
			params.color = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid brick color: " + parts[1]);
		}
		return params;
	}
}
