# Change Impact Analysis Lab: Select All Figures

## Feature: Change Request

My feature is Select All Figures from Lab 1 and Lab 2: the user triggers it from the Edit menu or by pressing Ctrl+A, and every figure on the canvas becomes selected. In Lab 2 concept location led me to the view class where the selectAll logic actually runs, so that is where I started the impact analysis.

---

## What I Did

I followed the activities from Figure 7.9. First I built an interaction diagram for the feature by reading the source code statically and confirming it with what I saw in the debugger during Lab 2. I started from SelectAllAction, which is the entry point, and traced every class it talks to on the way to selecting figures, and every class that selectAll talks to once it runs.

After that I marked all the classes in the diagram as blank. I marked the class found during concept location, DefaultDrawingView, as changed, because that is the class whose selectAll method actually loops over the figures and selects them. Then I started the loop from the algorithm: I looked at every blank neighbour of a changed or propagates class, marked them as next, and went through them one by one to decide whether each one was unchanged, propagates, or changed.

The first thing I noticed is that DefaultDrawingView does not extend AbstractDrawingView. It extends JComponent and implements the same interfaces directly. Both classes have their own copy of the selectAll method, and the two copies are identical line for line. Because of that I had to mark AbstractDrawingView as changed too, since a real change to how select all works would have to be made in both places to stay consistent. That was the most interesting thing I found.

From the changed view I looked at its neighbours. SelectAllAction and EditableComponent both pass the request along to selectAll but neither of them needs editing, because the method signature does not change, so I marked them as propagates. The classes that selectAll only reads from or notifies, like Drawing, Figure, CompositeFigure, FigureSelectionEvent and FigureSelectionListener, all stay unchanged, because they are reached through stable contracts that the feature does not touch. DelegatorDrawingView just inherits selectAll from AbstractDrawingView with no override, so it gets the change for free and stays unchanged. AbstractSelectionAction only manages whether the action is enabled, so it is unchanged as well. After that round there were no blank neighbours left, so the loop stopped.

---

## Estimated Impact Set

In total I visited 12 classes: 2 changed, 2 propagates, and 8 unchanged.

## Tables

Tables in portfolio.


