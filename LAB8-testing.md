# Lab 8 Testing

## Introduction

Unit testing means testing the smallest pieces of a program on their own, usually a
single method, so I can be sure that piece behaves as intended before trusting it
inside the bigger application. The tests are automated, so I can re-run them any time
I change the code and immediately see if I broke something. For this lab I wrote
JUnit 4 unit tests for my feature, Select All Figures, the same feature I have
followed since the user-story and concept-location labs.

---

## What I Did

First I added the test dependencies to jhotdraw-core/pom.xml. The module already had
TestNG, so I added JUnit 4 (4.13.2) next to it, plus Mockito (mockito-core 4.11.0) for
mocking out dependencies. The lab note says Swing and JUnit extensions work best with
JUnit 4, so I stayed on JUnit 4.

Then I picked the method to test. The real domain logic of the feature is selectAll() —
SelectAllAction only forwards the call, and the actual loop that selects figures lives
in the view. From the concept-location lab I knew the focused component at runtime is a
DefaultDrawingView, so that is the class I instantiate in the tests. That was also the
practical choice: AbstractDrawingView (its duplicate twin from Lab 4) is abstract and
full of unimplemented Swing methods, while DefaultDrawingView is concrete and I can
just new it.

The important part of unit testing here is not depending on real figures or a real
canvas. So I used Mockito to mock the Drawing and the Figure objects. Each figure mock
is told what to answer for isSelectable(), and the drawing mock is told what
getChildren() returns. That way every test drives one exact path through selectAll()
and nothing else.

The one thing that caught me out: the first time I ran a test it threw a
NullPointerException before selectAll() even started. setDrawing() eventually calls
drawing.getDrawingArea(1.0) inside validateViewTranslation(), and a bare mock returns
null there, which the view then tries to clone. I fixed it in setUp() by stubbing
getDrawingArea(...) to return an empty rectangle. After that the view sets up cleanly
and the tests only exercise the selection logic.


---

## Mocks and Stubs

The lab says that when a method reaches outside itself you have a dependency and
should use mocks/stubs (mockito.org). selectAll() has three dependencies that go
outside the method: it reads drawing.getChildren(), it asks each figure.isSelectable(),
and it repaints the view. I handled all three:

- Drawing is a Mockito mock, so getChildren() returns exactly the list I want for that
  test instead of a real composite figure.
- Each Figure is a Mockito mock, so isSelectable() returns the value I set and I never
  need a real RectangleFigure or its geometry.
- The Swing painting dependency is avoided for free, because with a mocked drawing
  there is nothing real to paint and repaint() on an undisplayed component does
  nothing.

Mockito earns its place here because Figure and Drawing are large interfaces (dozens
of methods); hand-writing stub classes for them would be huge, but a mock only needs
the one or two methods I actually stub.

---

## Java Assertions

For the assertions step I added a Java assert for an invariant inside selectAll() (in
both DefaultDrawingView and AbstractDrawingView, since Lab 4 showed the method is
duplicated and the two copies have to stay consistent):

    assert selectedFigures.size() <= drawing.getChildren().size()
            : "selectAll() selected more figures than the drawing contains";

This is a thing that should never happen: select-all can only ever pick figures that
are already in the drawing, so the selection can never be bigger than the drawing. If
that were ever false something is badly wrong, which is exactly what an assertion is
for. The difference from an exception matters here: an assertion stops the program so
a broken invariant is caught immediately during development, whereas an exception is
meant for situations the program should handle and keep running from. Assertions are
also off by default at runtime, so this adds no cost to the real application, but
Maven Surefire runs tests with assertions enabled, so the invariant is actually
checked every time the tests run.

---

## How I Verified the Feature

Each test maps back to how Select All is supposed to behave: every selectable figure
gets selected, locked figures are left alone, an empty canvas selects nothing, and
listeners are told the selection changed. Together they cover the best case and the
boundary cases for the feature.

To run only these tests:

    mvn -Dtest=SelectAllTest test

A green run is the verification, all eight tests pass, which means the selection
logic behind Select All works for the normal case and the edge cases I identified.
