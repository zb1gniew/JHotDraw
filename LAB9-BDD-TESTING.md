# BDD TESTS

Behaviour-Driven Development (BDD) is an approach to software testing that connects user stories to automated tests. Instead of writing tests in technical language, BDD scenarios are written in plain English using the Given-When-Then structure. This makes it easier to verify that the implemented feature matches the original requirement.

# User Story

As a user, I want to delete selected figures from the drawing so that I can remove unwanted elements from the canvas.

# Mapping to BDD Scenarios

The user story was mapped to three Given-When-Then scenarios:

# Scenario 1 – Delete an existing figure

Given a drawing with one figure
When the figure is deleted
Then the drawing is empty

# Scenario 2 – Delete a figure that does not exist

Given a drawing with one figure
When a non-existent figure is deleted
Then the drawing still has 1 figure

# Scenario 3 – Delete one figure from multiple

Given a drawing with multiple figures
When one of the figures is deleted
Then the drawing has 2 figures

# Implementation with JGiven

I used JGiven to automate the BDD scenarios. JGiven is a Java library that allows writing BDD tests using the Stage pattern. Each stage is a separate class:

GivenDrawingStage – sets up the drawing with figures before the test
WhenDeleteAction – performs the delete operation
ThenDrawingState – verifies the result using AssertJ assertions
The test class DeleteFigureBDDTest extends ScenarioTest and connects the three stages:

@Test
public void deleting_an_existing_figure_removes_it_from_the_drawing() {
    given().a_drawing_with_one_figure();
    when().the_figure_is_deleted();
    then().the_drawing_is_empty();
}
For assertions I used the AssertJ library, which provides readable domain-specific assertions. For example:

Assertions.assertThat(drawing.getChildCount()).isEqualTo(0);
Both the jgiven-junit and assertj-core dependencies were already available in jhotdraw-core/pom.xml.

# Verification

I ran the BDD tests using Maven from the command line:

mvn -s .maven-settings.xml test -pl jhotdraw-core -Dtest=DeleteFigureBDDTest
JGiven automatically printed each scenario in Given-When-Then format in the terminal output. The result was 3 tests, 0 failures, 0 errors.

# Conclusion

Mapping the user story to BDD scenarios made it easier to verify that the Delete feature behaves as expected from the user's perspective. JGiven produced readable test output that connects the automated tests directly to the original requirement. AssertJ provided clear and readable assertions for verifying the state of the drawing after each delete operation.