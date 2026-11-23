# LDraw output format

LDraw output format exports world in [.ldr](https://www.ldraw.org/article/218.html).

LDraw uses a [right-handed co-ordinate system](https://www.ldraw.org/article/218.html#coords) where -Y is "up". LDraw Units (LDU) can be seen on the following illustration:

![Brick dimensions in LDraw Units](https://www.ldraw.org/uploads/images/Articles/dim.png)

The size of a 2x2 brick ([`parts/3003.dat`](https://library.ldraw.org/parts/3486)) is used as the standard voxel size, so one voxel is 40 LDU wide/long and 24 LDU height. This means that voxels are not regular cubes, and a vertical scale factor of 3/5 of the horizontal scale should be used if proportions must be kept (e.g. with a horizontal scale of `1`, set a vertical scale of `0.6`).

## Table of contents

* [Output format description](#output-format-description)
* [Voxel placeable description](#voxel-placeable-description)
  * [Full format](#full-format)
  * [Short format](#short-format)

## Output format description

```yaml
format: ldraw
```

The generated model will be saved as a `.ldr` file in the destination directory. The file will be named after the `worldName` parameter (with special characters replaced by underscores).

## Voxel placeable description

Voxels in LDraw are bricks, from the [official parts library](https://library.ldraw.org/parts/list), with a [known color](https://www.ldraw.org/article/547.html).

Not all bricks have the size of a standard voxel, and some might also have a different anchor point. To properly use those bricks, an offset (expressed in LDU) can be set.

#### Full format

Example of a LDraw voxel description for `place` field:

```yaml
place:
  ref: parts/3003.dat # 2x2 standard brick
  color: 1 # Dark blue
```

Fields:
- `ref`: Brick [reference](https://library.ldraw.org/parts/list) (required)
- `color`: Brick [color ID](https://www.ldraw.org/article/547.html) (required)
- `lduOffsetX`: X-coordinate of the brick placement offset, in LDU (optional, default `0`)
- `lduOffsetY`: Y-coordinate of the brick placement offset, in LDU (optional, default `0`)
- `lduOffsetZ`: Z-coordinate of the brick placement offset, in LDU (optional, default `0`)
- `rotationAroundY`: The rotation angle in degrees around the LDraw Y axis (optional, default `0`)

Remember that Y axis points downward in LDU!

#### Short format

The LDraw voxel short form string is interpreted as the brick reference followed by the brick color ID (separated by a whitespace).

The special value `noBrick` represents a fake voxel that can be used to remove an already placed brick.
