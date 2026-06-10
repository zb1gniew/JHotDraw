# Lab 9 Behavior Driven Testing

## Introduction – User Stories and BDD

User stories are short and simple descriptions of a capability, written from the
perspective of the person who wants it: "As a [user type], I want [some goal] so that
[some reason]." An alternative form is "As a [user type], I want [some goal] because
[why]."

Behavior Driven Development (BDD) takes a user story and turns it into a concrete,
testable scenario using the Given-When-Then structure:

- Given: the starting situation (the context before anything happens)
- When: the user does something (the action)
- Then: the expected outcome (what should be true afterwards)

## Objective

Map my User Stories to BDD Given-When-Then Scenarios.

## User Stories

I am reusing my main feature, Select All Figures, and for this lab only I added two
more user stories so I have something to map.

1. Select All Figures – As a drawing application user, I want to select all figures
   on the canvas at once, so that I do not have to click each figure individually.
2. Delete Selected Figures – As a drawing application user, I want to delete the
   figures I have selected, so that I can remove shapes I no longer want.
3. Undo Last Action – As a drawing application user, I want to undo my last action,
   so that I can recover from a mistake without starting over.

## Mapping User Stories to BDD Scenarios

Table in the portfolio. 

## Automating the Scenarios with JGiven

I used JGiven to make the scenarios runnable. I added two test dependencies to
jhotdraw-core/pom.xml: jgiven-junit 1.3.1 and assertj-core 3.24.2.

Two new files in src/test/java/org/jhotdraw/draw:

- SelectAllScenarioTest.java has one @Test per scenario, each written as
  given()...when()...then().
- SelectAllStage.java holds the steps. Given builds a DefaultDrawingView with a mocked
  Drawing, When calls selectAll(), Then checks the selection.

## Verification

I ran the test and all 3 scenarios pass (Tests run: 3, Failures: 0). JGiven printed
each scenario back as a Given-When-Then sentence, which is the readable proof the
feature does what the user stories say.



