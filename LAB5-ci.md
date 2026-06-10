# Continuous Integration Lab

## Introduction

Continuous integration (CI) is the practice of merging every developer's working copy into a shared mainline several times a day, and having an automated server build and test the result on each merge. The point is to catch broken builds and failing tests early, while the change is still small and easy to fix. For this lab I set up a simple CI pipeline on GitHub Actions that builds JHotDraw and runs its tests automatically on every push and pull request.

---

## What I Did

I started by reading the two links the lab pointed me at: the GitHub guide on building and testing Java with Maven, and the guide on working with the Apache Maven registry on GitHub Packages. The first one gave me the basic workflow shape (checkout, set up Java, run a Maven command). The second explained how Maven authenticates against GitHub Packages so it can pull the shared jars this project depends on.

The repository already had a workflow file under .github/workflows and a .maven-settings.xml in the project root, but neither was actually wired up to work together, so I fixed both.

In the workflow I kept the triggers on push and pull_request for the develop and master branches, so the build runs on every pull request as the lab asks. I set up JDK 11 with the temurin distribution and turned on Maven caching so the local dependency cache is reused between runs and the build is faster. Then I split the work into two clearly named steps: a Build with Maven step that runs mvn package while skipping tests, and a separate Run tests with Maven step that runs mvn test. Splitting them gives each part its own log in the Actions UI, so it is obvious whether a failure is a compile problem or a test problem.

The key detail was getting the shared jars from GitHub Packages to download during CI. The project's pom.xml declares a GitHub repository (sweat-tek/MavenRepository) with the id github, and GitHub Packages requires authentication even to read. So both Maven steps point Maven at the settings file in the project root with the -s flag, and both export GITHUB_ACTOR and GITHUB_TOKEN as environment variables. The token comes from the automatic GITHUB_TOKEN secret that Actions injects into every run, so no personal access token needs to be stored.

While doing this I found that the original settings file would not have worked. Its server block used the GitHub Actions expression syntax for the actor and token. That syntax only resolves inside the workflow YAML, never inside a Maven file. Maven reads the settings file on its own and would have treated those as literal text. The correct way to inject values into a settings file is Maven's environment property syntax, so I changed them to read GITHUB_ACTOR and GITHUB_TOKEN from the environment, which match the env vars the workflow now sets. I also replaced the leftover OWNER/REPOSITORY placeholder in the repository URL with the real sweat-tek/MavenRepository, and made sure the server id stayed github so it matches the repository id the pom.xml uses (authentication is matched by id, not by URL).

---

## CI Pipeline Results

The pipeline now does three things automatically on every push and pull request to develop or master:

1. Checks out the code and sets up a cached JDK 11 / Maven environment.
2. Builds all modules with Maven, authenticating to GitHub Packages so the shared jars resolve.
3. Runs the test suite as a separate, clearly labelled step.
