# Refactoring Lab

# Feature: Delete Selected Figures

# Finding Code Smells with SonarLint

While analyzing the modules explored in Labs 3 and 4 with SonarLint, I found several issues in DeleteAction.java. The tool reported two code-quality concerns: a repeated method invocation within the actionPerformed method and a hardcoded string literal used in the constructor’s lambda expression. Beyond the automated warnings, a manual review of the deleteNextChar Javadoc during Lab 3 revealed an additional issue that was not detected by the tool.

# Smell 1 – Duplicate Expression

Inside DeleteAction.actionPerformed() at lines 111–114, the expression KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() is evaluated twice within the same if block.

The first evaluation checks whether the result is an instance of JComponent. If that check passes, the exact same call is made again immediately to retrieve the value for the cast. This means the focus owner is looked up twice from the keyboard focus manager instead of once. SonarLint flags this as a redundant duplicate call. If the focus owner changed between the two calls — unlikely but possible — the code would also behave inconsistently.

# Refactoring of Code Smell 1 – Extract Variable

Store the result of the first call in a local variable and reuse it in the cast.

The focus owner is resolved once, the condition reads more clearly, and the cast on the next line uses the same object that was already checked. No behaviour changes.

# Smell 2 – Magic String

Inside the constructor of DeleteAction at line 98, a raw string literal "enabled" is used inside the property change lambda.

The string has no name and no explanation. A reader has to know that "enabled" is the standard Java Beans property name for a component's enabled state to understand what this condition is checking. If the same string were needed elsewhere in the class, it would have to be typed again by hand with no guarantee both copies stay in sync.

# Refactoring of Code Smell 2 – Replace Magic Literal

Declare a named constant at the top of the class alongside the existing constants and replace the raw string with it.

The intent of the condition is now stated in the constant name. One declaration, one use, zero guessing.

# Smell 3 – Duplicate Code

The Javadoc comment above deleteNextChar() at line 125 reads:

> This method was copied from DefaultEditorKit.DeleteNextCharAction.actionPerformed(ActionEvent).

# Refactoring of Code Smell 3 - Remove Duplication (stuck)

The clean fix would be to delegate to DefaultEditorKit.DeleteNextCharAction directly instead of maintaining a copy. The problem is that DefaultEditorKit is a JDK class and DeleteNextCharAction is a package-private inner class that cannot be instantiated or subclassed from outside the JDK. The duplication cannot be fully eliminated without access to the original class. The smell is real but the refactoring is blocked by the library boundary — the best outcome is to leave the comment in place so future maintainers understand why the copy exists.