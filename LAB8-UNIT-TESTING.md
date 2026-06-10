# Unit Testing Lab

Unit testing means checking small parts of the program separately to make sure that they work as expected. A unit test usually focuses on one method or one specific behaviour. This makes it easier to find problems and confirm that changes do not break existing functionality.

For this lab, I tested the Delete Selected Figures feature in JHotDraw. This feature allows the user to select one or more figures on the canvas and remove them from the drawing, either through the Edit menu or by pressing the Delete key.

Before writing the tests, I had only checked the feature manually through the graphical user interface. Manual testing is useful, but it is slower and harder to repeat. Unit tests make it easier to check the same behaviour again after changes are made to the code.

For this lab, I used JUnit 4 together with Mockito. JUnit 4 was used to create and run the tests, while Mockito was used to replace one dependency with a mock object. The JUnit 4 dependency was already available in the Maven project. The Mockito dependency was added to jhotdraw-core/pom.xml.

# What I Tested

I created unit tests for the most important part of the Delete feature: the remove() method on the Drawing interface, implemented by DefaultDrawing. This method is responsible for removing a figure from the drawing and returning a boolean result indicating whether the removal was successful.

I used real DefaultDrawing and RectangleFigure objects in most tests. This was possible because these classes do not depend on the Swing user interface and can be instantiated directly in a headless test environment.

The main test checks that when a figure exists in the drawing, calling remove() on it reduces the figure count from 1 to 0. I also tested that when multiple figures are present, removing one figure leaves the others untouched.

I additionally verified that remove() returns true when the figure is successfully removed. This return value is part of the method contract defined in the Drawing interface.

# Boundary Cases

I tested the following boundary cases:

- Figure not in the drawing: calling remove() with a figure that was never added returns false and leaves the drawing unchanged.

- Empty drawing: calling remove() on an empty drawing does not cause an error and returns false.

- Same figure removed twice: the first removal succeeds and returns true; the second removal returns false and the figure count remains at 0.

These cases confirm that the deletion logic is safe even when called with invalid or repeated input.

# Mocks and Stubs

The remove() method on Drawing calls removeNotify() on the figure being removed. This means Figure is a dependency of the method under test. I used Mockito to replace this dependency with a mock object

This test verifies that when a figure is removed from the drawing, it receives a removeNotify() call. Using a mock allowed me to verify this interaction without depending on a real figure implementation.

# Java Assertion

I added Java assertions to check invariants in the drawing after deletion:

assert drawing != null : "Drawing cannot be null";
assert drawing.getChildCount() >= 0 : "Count cannot be negative";

These are invariants because a Drawing object must always exist before any operation is performed on it, and the figure count must never be negative regardless of how many figures have been removed.

# Verification

I ran the tests using Maven from the command line with the following command:

mvn test -pl jhotdraw-core -Dtest=DeleteFigureTest

The result was 8 tests, 0 failures, 0 errors. This confirmed that all unit tests completed successfully.

# Conclusion

Writing unit tests made the expected behaviour of the Delete Selected Figures feature clearer. The tests verify the normal case, boundary cases, mock-based interaction verification, and the invariants of the drawing after deletion. They will also make it easier to detect regressions if the code is changed in the future.