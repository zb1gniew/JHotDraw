# Change Impact Analysis Lab: Delete Selected Figures

## Feature: Change Request

My feature is Delete Selected Figures established in Labs 1, 2 and 3. The user selects one or more figures and triggers deletion via the Edit menu or the Delete key. Concept location in Lab 3 led me to AbstractDrawingView.delete(), so that is where I seeded the impact analysis.

## What I Have Done

I created the interaction diagram by combining static code analysis with the execution trace gathered during the Lab 3 debugging session. Beginning with DeleteAction, I followed each class involved before the call to delete(), and then continued tracing the classes accessed by delete() throughout its execution.

Once the diagram was complete, I initially labelled every class as blank. I then marked AbstractDrawingView as changed because the concept location identified it as the primary implementation point and it contains the main deletion functionality. From there, I applied the propagation algorithm.

The first neighbouring blank class I investigated was DefaultDrawingView. Looking through its source code, I discovered a delete() implementation at line 1288 that is nearly identical to the version in AbstractDrawingView. The only significant distinction is that DefaultDrawingView invokes getToolkit().beep() whenever a non-removable figure is encountered, whereas AbstractDrawingView simply exits without any notification. Since modifications to the deletion behaviour would need to be reflected in both implementations, I classified DefaultDrawingView as changed as well. This turned out to be the key finding of the analysis because the duplicated code means the feature cannot be updated in only one location.

Next, I reviewed the remaining blank neighbours connected to the two changed classes. DeleteAction merely forwards the request and has no knowledge of the internal deletion process, so I marked it as propagates. The EditableComponent interface serves only as the mechanism that enables this dispatch and was therefore also marked as propagates. Drawing participates through calls such as sort(), removeAll(), indexOf(), add(), and fireUndoableEditHappened(), but these are standard operations whose contracts remain unaffected by changes to deletion behaviour, so I classified it as unchanged. Figure contributes only the isRemovable() check through a stable interface and is likewise unchanged. AbstractUndoableEdit functions as the superclass of the anonymous undo object created within delete(), and because its framework-defined methods would not require modification, it also remains unchanged. DelegatorDrawingView does not provide its own implementation of delete(), instead inheriting the behaviour from AbstractDrawingView, which means it automatically benefits from any updates and can be considered unchanged. Finally, AbstractSelectionAction is responsible only for enabling or disabling the action according to the current selection state and plays no role in the actual deletion process, so it was marked unchanged as well.

After evaluating all neighbouring classes, no blank nodes remained in the diagram, which brought the propagation process to an end.

## Estimated Impact Set

In total I visited 10 classes: 2 changed, 2 propagates, and 6 unchanged.