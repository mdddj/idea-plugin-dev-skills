# IntelliJ SVG Style and Color Rules

Canonical doc checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/icons-style.html`

Use this reference when you need to draw or review custom plugin SVG icons instead of only wiring existing assets.

## Core Style

JetBrains documents a flat geometric icon style:

- Prefer flat shapes over 3D forms.
- Prefer straight corners and edges over soft rounded forms.
- Favor `45` and `90` degree angles, or `30` and `60` degrees where appropriate.
- Keep the icon as simple as possible without losing meaning.

## Grid and Visible Area

Icons sit inside a square artboard with transparent padding.

For the default `16x16` icon:

- The transparent border is `1px`.
- The visible content should usually fit inside a `14x14` area.

Other documented sizes:

- Gutter and status bar: `12x12`
- Tool window: `13x13`
- Default toolbar/project tree/action icon: `16x16`
- Dialog icon: `32x32`

Content can go beyond the default visible area for modifiers or to compensate optical weight.

## Basic Shapes and Visual Weight

JetBrains recommends building icons from simple squares, circles, and rectangles, then adjusting them for optical balance.

Examples from the guide:

- A square baseline is `12x12`.
- A circle is `14px` in diameter so it does not feel visually lighter than a square.
- Horizontal rectangle: `10x14`.
- Vertical rectangle: `14x10`.

Rules of thumb:

- More filled shapes need less space.
- Non-square shapes often need slightly larger dimensions to feel balanced.
- Detailed icons can occupy more of the artboard than minimal icons.

## Stroke Rules

- Use a `2px` stroke as the main line weight.
- Use thinner strokes only for subtle legibility adjustments or optical correction.
- Align geometry to whole pixels when possible, or to `.5px` when integer alignment is impossible.
- Keep diagonal lines to `30`, `45`, or `60` degrees for sharper rasterization.
- Minimize anchor points to keep SVGs smaller and cleaner.

If a stroke sits off the pixel grid, the icon will blur when rasterized.

## Modifiers

A modifier is a smaller shape layered over the base icon.

- Default placement: bottom-right corner
- Alternate placement: another corner if the base icon becomes unclear or if several modifiers are needed
- Typical modifier size: `6px` to `9px`
- Spacing from base icon: `1px` to `2px`

## Arrow Rules

For arrows, the documented default is:

- Filled triangle arrowhead with a `90` degree pointing angle
- `2px` body ending in a square stroke
- Horizontal, vertical, `45` degree, or round orientation

Unfilled arrowheads are acceptable for single navigation-style arrows when a filled head would create too much visual weight.

## Color Rules

Global constraints:

- Do not use color as the only differentiator.
- Do not use gradients.
- Do not use shadows.

### Action icons

Default action icons are grey and monochromatic. Use semantic accent colors only for specific categories:

- Grey: default action icons, tool window icons
- Green: positive actions such as run or create
- Red: destructive actions such as stop or remove
- Blue: highlight popular icons or small accents in complex icons
- Yellow: warning or optimization-related actions

Documented action palette:

- Red: `#DB5860`
- Red dark: `#C75450`
- Yellow: `#EDA200`
- Yellow dark: `#F0A732`
- Green: `#59A869`
- Green dark: `#499C54`
- Blue: `#389FD6`
- Blue dark: `#3592C4`
- Grey: `#6E6E6E`
- Grey dark: `#AFB1B3`

### Tool window icons

The style guide says tool window icons should remain grey so they do not pull too much attention from the IDE content area.

### Noun icons

Noun icons cover file types and tree markers, so the palette is wider. The guide documents multiple hues and opacity levels, including:

- Grey: `#9AA7B0`
- Blue: `#40B6E0`
- Green: `#62B543`
- Yellow: `#F4AF3D`
- Purple: `#B99BF8`
- Pink: `#F98B9E`
- Red: `#F26522`
- Red status: `#E05555`

Opacity guidance:

- `60%` opacity and grey `80%` for large areas such as folders
- `70%` opacity for medium-size elements such as file type bodies
- Full opacity for small details and modifiers

## Text in Icons

When an icon needs a letter:

- Reuse letters from existing icons when possible.
- If there is no suitable source, use a common sans-serif font such as Arial or Open Sans.

## Export Rules

- Export icons as SVG.
- Use camelCase filenames such as `iconName.svg`.
- Add `iconName_dark.svg` when the dark-theme drawing differs.
- Optimize the output before saving; the guide specifically mentions SVG optimization tooling for Sketch.

## Practical Drawing Checklist

Before shipping a custom icon, verify:

- The icon is flat, geometric, and simple.
- The visible artwork fits the documented grid for its size.
- Strokes are mostly `2px` and aligned to the pixel grid.
- Modifiers are sized and spaced clearly.
- Shape, not just color, conveys meaning.
- No gradients or shadows slipped into the SVG.
- Filenames use the documented camelCase and dark-theme suffix patterns.
