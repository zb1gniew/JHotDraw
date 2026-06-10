# Continuous Integration Lab

# Introduction

Continuous integration is the practice of automatically building and testing the codebase on every push or pull request, so that broken code is caught before it reaches the shared branch. For this lab I configured a GitHub Actions pipeline that builds JHotDraw with Maven and runs its tests on every push and pull request to the develop and master branches.

# What I Have Done

The repository already had a maven.yml workflow and a .maven-settings.xml file, but neither of them was actually working together. I read through both files and found two problems before writing a single line.

The first problem was in .maven-settings.xml. The repository URL still contained the placeholder OWNER/REPOSITORY from the template — Maven had no real address to fetch the shared jars from. I replaced it with sweat-tek/MavenRepository, which is the actual GitHub Packages repository this project uses. The same file also had ${{ github.actor }} and ${{ github.token }} in the credentials block. That syntax belongs to the GitHub Actions YAML engine — it is evaluated by GitHub before the job runs. Maven reads the settings file independently and would have treated those as literal strings, meaning authentication would silently fail. The correct syntax for reading environment variables inside a Maven settings file is ${env.GITHUB_ACTOR} and ${env.GITHUB_TOKEN}, so I changed them.

The second problem was in maven.yml. The build step ran mvn -B package --file pom.xml with no -s flag, so Maven never loaded the settings file at all. The shared jars would never resolve and the build would fail. I added -s .maven-settings.xml to point Maven at the fixed settings file, and I exported GITHUB_ACTOR and GITHUB_TOKEN as environment variables on the step — these come from the github.actor context and the automatic secrets.GITHUB_TOKEN that GitHub injects into every Actions run, so no personal token needs to be stored.

I also split the work into two named steps. The first step builds all modules with -DskipTests so a compile failure shows up clearly on its own. The second step runs mvn test separately. Splitting them means the Actions log labels each part individually, making it obvious whether a failure is a build problem or a test problem.

# Result

The pipeline now runs automatically on every push and pull request to develop or master and does three things in sequence: checks out the code and restores the Maven cache, builds all modules while authenticating to GitHub Packages, then runs the test suite as a separate step.
