# Lab 7 – Actualization

## SOLID Principles

### S – Single Responsibility Principle

A class should have only one reason to change. SelectAllAction handles the event and delegates; AbstractDrawingView.selectAll() owns the selection logic , one responsibility each. DefaultDrawingView violates it by doing too much, which is why it accumulated a duplicate selectAll() instead of inheriting one.

---

### O – Open/Closed Principle

Software entities should be open for extension but closed for modification. SelectAllAction never needs to change when a new component type implements EditableComponent , that is the open part. The instanceof chain in actionPerformed breaks the closed part: adding a type requires editing the method directly.

---

### L – Liskov Substitution Principle

Subtypes must be substitutable for their base type without altering behaviour. DelegatorDrawingView extends AbstractDrawingView and inherits selectAll() with no override. Lab 4 confirmed it as unchanged , it substitutes correctly.

---

### I – Interface Segregation Principle

Clients should not be forced to depend on methods they do not use. SelectAllAction depends only on EditableComponent and is never exposed to the full DrawingView interface with its unrelated rendering and layout methods.

---

### D – Dependency Inversion Principle

High-level modules should depend on abstractions, not on concretions. SelectAllAction imports EditableComponent, not DefaultDrawingView or AbstractDrawingView , it depends on the abstraction, and the concrete class is resolved at runtime.

---

## Clean Architecture

Clean Architecture organises a system into concentric layers , domain entities at the centre, use cases around them, interface adapters outside those, and frameworks and drivers on the outside. The core rule is that dependencies point inward: outer layers can know about inner ones, but inner layers must not know about outer ones. This protects business logic from framework changes.

Mapping the Select All feature:

- Entities: The core business objects with no framework dependency. Here that is Figure and Drawing.
- Use case: Application-specific business rules , what the system does. Here that is the selectAll logic: iterate figures, check selectability, fire the change event.
- Interface adapters: Convert data between the use-case layer and the outside world. Here that is SelectAllAction, which translates a Swing ActionEvent into a use-case call.
- Frameworks / drivers: The outermost layer , UI, databases, external tools. Here that is Swing, JComponent, and the key binding infrastructure.

JHotDraw does not enforce this cleanly. Figure and Drawing both import Swing packages, so the inner layers are already coupled to the framework.

The cost shows in actualization. The use-case logic lives inside DefaultDrawingView, a framework-layer class, and was duplicated in AbstractDrawingView because there is no inheritance between them. Lab 4 recorded two changed classes for one logical change. The Pull Up Method refactoring in Lab 6 addresses this directly.
