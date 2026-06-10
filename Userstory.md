# Align Selected Figures

## User Story

As a person creating a drawing, I want to align my selected figures to the same left edge with one click, so that they line up neatly without me having to drag each one into place by hand.

## Acceptance Criteria

- The Align Left button is enabled only when two or more figures are selected. If nothing is selected, or only one figure is selected, the button stays disabled.
- When the button is clicked, every selected figure moves so its left edge matches the leftmost edge of the selection.
- Figures that are not selected do not move.
- The action can be undone in a single step, and redone afterwards.
