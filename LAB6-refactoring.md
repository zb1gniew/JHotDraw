# Lab 6 – Refactoring

## Finding Code Smells with SonarLint

I installed SonarLint and ran it on the modules I touched during Labs 3 and 4. Two things lit up straight away on DefaultDrawingView. One rule flagged that a method has an identical copy somewhere else in the codebase, and another flagged that the class is too large. I also noticed a third problem by hand while reading SelectAllAction during Lab 3.

---

## Code Smells

### Smell 1 – Duplicate Code

The selectAll() method appears twice with the exact same body. Both AbstractDrawingView.java at line 604 and DefaultDrawingView.java at line 844 contain this:

Code in portfolio.

I already noted this in Lab 4: "the two copies are identical line for line." The problem is that if this logic ever needs to change, someone has to remember to update both places. If they only change one, the two view classes will behave differently without any compiler warning.

### Refactoring of Code Smell 1 – Pull Up Method

Make AbstractDrawingView extend JComponent, then change DefaultDrawingView to extend AbstractDrawingView instead of JComponent directly. Once that inheritance is in place, delete the duplicate selectAll() from DefaultDrawingView (lines 840–856) — it gets inherited from AbstractDrawingView and only one copy exists.

---

### Smell 2 – Magic Numbers

Inside DefaultDrawingView.getBackgroundPaint() at line 1474 there are several raw numbers with no names:

Code in portfolio.

Reading this, 16, 8, and 0xdfdfdf mean nothing without digging into what the method is doing. 16 is the tile size, 8 is half of that, and 0xdfdfdf is the grey colour of the checkerboard background. If the tile size ever needs to change, every hardcoded 16 and 8 needs to be tracked down manually. The fix is simple — give them names:

Code in portfolio.

### Refactoring of Code Smell 2 – Replace Magic Literal

Declare the three constants above in DefaultDrawingView and replace every occurrence of 16, 8, and 0xdfdfdf in getBackgroundPaint() with TILE_SIZE, HALF_TILE, and TILE_GREY.

---

### Smell 3 – Switch Statements

In SelectAllAction.actionPerformed() at line 82, the code checks what type the focused component is before deciding what to do:

Code in portfolio.

Both branches do the same thing — call selectAll() — but because JTextComponent and EditableComponent are unrelated types, there is no single call that covers both. The clean fix would be to have all selectable components share one interface so the chain collapses to a single call. The problem here is that JTextComponent comes from Swing and cannot be changed to implement EditableComponent, so the smell is partly stuck.

### Refactoring of Code Smell 3 – Replace Conditional with Polymorphism

Wrap JTextComponent in an adapter class that implements EditableComponent and delegates selectAll() to JTextComponent.selectAll(). The instanceof chain then collapses to a single cast to EditableComponent regardless of what the underlying component is.

---