# Concept Location Lab – Delete Selected Figures

## Feature: Change Request

My selected feature is Delete Selected Figures: the user triggers it from the Edit menu or by pressing the Delete key, and every selected figure is removed from the canvas. The action is undoable.

# What I Have Done

I approached the feature from the user’s point of view: when the Delete key is pressed, any selected figures should be removed. Because the action can be triggered through a menu option, I assumed the starting point would be located in the actions package.
I quickly located DeleteAction in jhotdraw-actions/src/main/java/org/jhotdraw/action/edit/ by reasoning that a menu-triggered action would follow the same naming convention as other actions in the edit package. To verify that this class handles the Delete command, I placed a breakpoint in the actionPerformed method at line 109.
After launching the application in debug mode, I created several shapes, selected one of them, and pressed Delete. Execution paused at the breakpoint inside DeleteAction, confirming that the action begins there. Stepping through the method showed that it retrieves the component currently holding focus and verifies whether it implements EditableComponent. In my test case, the focused object was a DefaultDrawingView, so the code cast it accordingly and invoked its delete() method.
To continue tracing the execution path, I added another breakpoint in AbstractDrawingView.delete() at line 938 and resumed the program. The debugger then stopped at that location.
Examining the delete() implementation revealed that it first iterates through the figures returned by getSelectedFigures(), checking whether each figure can be removed via isRemovable(). Once all figures passed the validation, their z-order positions were stored in an integer array. The method then cleared the selection and executed drawing.removeAll(deletedFigures) to remove the figures from the drawing. Finally, an undoable edit was created, preserving both the deleted figures and their original indices so that an undo operation could restore them to the correct positions.
By inspecting the call stack, I was able to reconstruct the complete execution chain: DeleteAction → AbstractDrawingView → Drawing → Figure.
