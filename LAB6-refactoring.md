# Lab 6 – Refactoring

## Finding Code Smells with SonarLint

I installed SonarLint and ran it on the classes that sit inside my Select All Figures feature footprint from Labs 3 and 4: SelectAllAction, AbstractSelectionAction, and the EditableComponent contract between them. Two things lit up. On AbstractSelectionAction it flagged two identical branches in the listener that enables the menu item, and on SelectAllAction it flagged wildcard imports in the entry-point class.

---

## Code Smells

### Smell 1 – Duplicated Branches

File: AbstractSelectionAction.java, inside the property change listener at line 71. This listener is what enables and greys out the Select All menu item when the selection changes.

Before:

```java
String n = evt.getPropertyName();
if ("enabled".equals(n)) {
    updateEnabled();
} else if (n.equals(EditableComponent.SELECTION_EMPTY_PROPERTY)) {
    updateEnabled();
}
```

Why this is a smell: both branches do the exact same thing, call updateEnabled(), so the split just makes the reader check both to confirm they match. SonarLint flags this as rule S3923 (all branches with identical code). A second, smaller issue: the else if is written as n.equals(CONSTANT), which throws a NullPointerException if n is null.

### Refactoring of Code Smell 1 – Consolidate Conditional Expression

The fix is to keep one if and one call to updateEnabled(), and join the two conditions with || (logical OR). While doing that I also flip the second check to CONSTANT.equals(n) so the constant is on the left, that version can never throw on a null, because the constant is never null.

After:

```java
String n = evt.getPropertyName();
if ("enabled".equals(n) || EditableComponent.SELECTION_EMPTY_PROPERTY.equals(n)) {
    updateEnabled();
}
```

Tthe duplicated call is gone, the intent ("refresh enabled state when either of these two properties changes") is now readable on a single line, and the null risk is removed. The behaviour is exactly the same as before for every input.

---

### Smell 2 – Wildcard Imports

File: SelectAllAction.java, the import block at lines 10-15. This is the entry-point class of the feature, the action that runs when the user presses Ctrl+A.

Before:

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import org.jhotdraw.api.gui.EditableComponent;
import org.jhotdraw.util.*;
```

Why this is a smell: the four * imports pull in whole packages instead of the five types actually used (ActionEvent, KeyboardFocusManager, JComponent, JTextComponent, ResourceBundleUtil), so they hide what the class really depends on and risk a name clash if two packages share a class name. SonarLint flags this as rule S2208 (wildcard imports should not be used).

### Refactoring of Code Smell 2 – Replace Wildcard Imports with Explicit Imports

The fix is to delete the wildcard imports and replace each one with a single explicit import for the exact type that is used. Nothing in the body of the class changes, only the import block.

After:

```java
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.jhotdraw.api.gui.EditableComponent;
import org.jhotdraw.util.ResourceBundleUtil;
```

Why it is better: the imports now document exactly what the class depends on, and the name-clash risk is gone. This is purely a compile-time change, so it has no effect on how the program runs.

---
