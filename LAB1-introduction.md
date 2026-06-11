# Introduction Lab – Getting Started

## Objectives

- Get started with the Maven build system.
- Get started with the GitHub workflow.

## What I Did

The point of this first lab was to check out the CASE study source code for the course and make sure the whole toolchain actually works before any real maintenance starts in the later labs.

### GitHub setup

I started on the GitHub side. My team created a fork of the JHotDraw project repository, going from sweat-tek/JHotDraw to our own zb1gniew/JHotDraw, so we have a copy to work on without touching the original. I also added the original repository as an upstream remote, which means I can pull in future changes from the main project and keep my fork in sync.

I read through the GitHub flow that we will follow for the rest of the course: every team member works on their own feature branch, and changes only land on the main branch after going through a pull request. In practice that meant each of us (kamil, jakub) had our own branch, so no one commits straight to main and each piece of work stays isolated until it is reviewed and merged.

### Maven and Java

After that I set up the build environment. I installed Maven 3.8.x and made sure it was running on JDK 11, since the project targets Java 11. I opened the project root folder in the terminal and built everything with:

```
mvn clean install -DskipTests
```

I skipped the tests here on purpose, just to confirm the project compiles and the modules assemble cleanly. The build finished with BUILD SUCCESS and produced the jhotdraw-samples-misc-9.1-SNAPSHOT.jar artifact, so the Maven setup and the Java version were correct.

### Running the application

Finally I went into the jhotdraw-samples-misc module and launched the SVG editor from the terminal:

```
mvn exec:java "-Dexec.mainClass=org.jhotdraw.samples.svg.Main"
```

The JHotDraw GUI opened up, which is exactly what was expected. That confirmed the full chain was working: the forked repository, the Maven build, and the Java 11 configuration. At that point the project was ready for the maintenance work in the following labs.

---

## Result

The repository is forked and connected to upstream, Maven builds the project on JDK 11, and the SVG sample application starts with a working GUI. Everything is in place for the next labs.
