# Concept Location Lab – Select All Figures

## Fetutre: Change Request

My selected feature is Select All Figures (derived from User Story 1): user triggers it from Edit menu or by pressing Ctrl+A, and every figure on the canvas becomes selected.

---

## What I Did

To start, I looked at the feature from a user perspective: pressing Ctrl+A should select all figures. I figured the entry point would be somewhere in the actions package since it comes from a menu item, so I started searching there.

I found the SelectAllAction class right away in jhotdraw-actions/src/main/java/org/jhotdraw/action/edit/. I set a breakpoint on the actionPerformed method to confirm this is where execution starts when Ctrl+A is pressed.

Then I ran the application in debug mode, drew a few shapes on the canvas, and pressed Ctrl+A. The debugger stopped at my breakpoint in SelectAllAction. I stepped through the code and saw it grab the currently focused component and cast it to EditableComponent, then call selectAll() on it.

The focused component turned out to be a DefaultDrawingView. I set a second breakpoint inside AbstractDrawingView.selectAll()  and continued execution. The debugger stopped there next.

Inside selectAll() I could see it looping over drawing.getChildren and checking figure.isSelectable() for each one. I set a third breakpoint on that if condition and watched it iterate through my figures in the call stack. Each selectable figure got added to the selectedFigures set, and after the loop it called fireSelectionChanged() and repaint().

From the call stack panel I traced the full chain: SelectAllAction to AbstractDrawingView to Drawing to Figure.

---

## Concept Location Results

Table in portfolio.
