# Actualization & SOLID Principles

# S – Single Responsibility Principle
A class should have only one reason to change.

DeleteAction has one responsibility: receive the event, resolve the focused component, and dispatch the delete() call. It never touches figure state directly.

AbstractDrawingView.delete() has one responsibility: own the deletion logic — check removability, capture z-indices, remove figures, fire the undo record.

DefaultDrawingView violates this principle. It is a large Swing view class responsible for rendering, input handling, and layout, yet it also contains its own independent copy of delete(). The view accumulated deletion logic it should not own separately — which is exactly why the duplication appeared in the first place.

# O – Open/Closed Principle
Software entities should be open for extension but closed for modification.

DeleteAction is open for extension: any new component type that implements EditableComponent works with DeleteAction immediately, no changes required. That is the open part.

The else branch in actionPerformed breaks the closed part. If a new component type appears that is neither EditableComponent nor JTextComponent, a developer must open actionPerformed and edit it directly. The Lab 6 refactoring (Extract Variable, Compose Method) did not fix this, but it made the structure easier to read and the problem more visible.

# L – Liskov Substitution Principle
Subtypes must be substitutable for their base type without altering behaviour.

DelegatorDrawingView extends AbstractDrawingView and has no override of delete(). It inherits the implementation and substitutes correctly — Lab 4 confirmed it as unchanged.

DefaultDrawingView does not extend AbstractDrawingView. It extends JComponent directly and carries its own delete() with a small behavioural difference: it calls getToolkit().beep() when a non-removable figure is found, while AbstractDrawingView returns silently. Both are used as DrawingView implementations, so a caller treating them as the same type would observe different behaviour — a Liskov violation.

# I – Interface Segregation Principle
Clients should not be forced to depend on methods they do not use.

DeleteAction depends only on EditableComponent. It calls delete() and nothing else. It is never exposed to the full DrawingView interface with its rendering, scaling, and layout methods. The interface boundary keeps the action layer thin and focused.

# D – Dependency Inversion Principle
High-level modules should depend on abstractions, not on concretions.

DeleteAction imports EditableComponent, not DefaultDrawingView or AbstractDrawingView. The concrete class behind the interface is resolved at runtime through the keyboard focus manager. The action layer depends on the abstraction; the framework resolves the concrete instance.

# Clean Architecture – JHotDraw Case Study

Clean Architecture organises a system into concentric layers: domain entities at the centre, use cases around them, interface adapters outside those, and frameworks and drivers on the outside. The core rule is that dependencies point inward — outer layers may know about inner ones, but inner layers must not know about outer ones.

Mapping the Delete feature to these layers:
•	Entities — Figure and Drawing. Core domain objects that model the canvas and its contents.
•	Use cases — The deletion logic in AbstractDrawingView.delete(): check removability, capture z-indices, remove figures, register an undoable edit.
•	Interface adapters — DeleteAction, which translates a Swing ActionEvent into a use-case call via EditableComponent.
•	Frameworks / drivers — Swing, JComponent, KeyboardFocusManager, and the menu and key binding infrastructure.

JHotDraw does not enforce this cleanly. Figure and Drawing both import Swing packages, so the inner layers are already coupled to the framework. The deletion use-case logic lives inside AbstractDrawingView and DefaultDrawingView, both of which are framework-layer classes that extend JComponent. The cost shows directly in the feature: because there is no clean separation, the logic was duplicated in DefaultDrawingView instead of being inherited from a single authoritative place. The Compose Method refactoring in Lab 6 improved the readability of deleteNextChar, but the deeper architectural problem — use-case logic living inside a framework class — remains.
