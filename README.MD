# test-guns [![JDK8](https://github.com/serge2nd/test-guns/workflows/JDK8/badge.svg)](https://github.com/serge2nd/test-guns/actions?query=workflow%3A%22JDK8%22) [![JDK11](https://github.com/serge2nd/test-guns/workflows/JDK11/badge.svg)](https://github.com/serge2nd/test-guns/actions?query=workflow%3A%22JDK11%22) [![JDK15](https://github.com/serge2nd/test-guns/workflows/JDK15/badge.svg)](https://github.com/serge2nd/test-guns/actions?query=workflow%3A%22JDK15%22) [![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/serge2nd/test-guns?color=green&label=latest)](https://serge2nd.jfrog.io/artifactory/maven/ru/serge2nd/test-guns) [![SNAPSHOT](https://img.shields.io/github/v/tag/serge2nd/test-guns?label=tag&color=blue)](https://serge2nd.jfrog.io/artifactory/maven/ru/serge2nd/test-guns/0.9.0-SNAPSHOT)
A little assistance in asserting/matching with [Hamcrest Matchers](https://github.com/hamcrest/JavaHamcrest).

## Apply in a project
First add the snapshot repository:
```xml
<repositories>
    <repository>
        <id>serge2nd-artifactory</id>
        <url>https://serge2nd.jfrog.io/artifactory/maven</url>
    </repository>
</repositories>
```
Then declare the dependency:
- release
```xml
<!-- currently not available -->
```
- snapshot
```xml
<dependency>
    <groupId>ru.serge2nd</groupId>
    <artifactId>test-guns</artifactId>
    <version>0.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

## Usage examples
### Multiple asserts/matches
```java
AssertThat.assertThat(
                       3 + 7, CoreMatch.sameAs(10),
                     "joker", org.hamcrest.Matchers.containsString("ok"),
    "S".getBytes("UTF-16BE"), ArrayMatch.bytes((byte)0, (byte)83),
    // extra assertions
    () -> {},
    () -> System.out.println("last assertion")/*, ... */);

AssertAllMatch.assertAllMatch(
    CoreMatch.presented(),
    Optional.of(7),
    Optional.of(""),
    Optional.of(emptyList()),
    // extra assertions
    (ToRun)() -> {},
    () -> System.out.println("last assertion")/*, ... */);

AssertForMany.assertForMany(
    org.hamcrest.Matchers.startsWith("/usr/"),
    org.hamcrest.Matchers.endsWith("/bin"),
    // many values
    "/usr/bin",
    "/usr/local/bin",
    "/usr/lib/java/current/bin"/*, ... */);
```
### Building a matcher
```java
Matcher<Object> chars(char... chars) {
    Matcher<Character[]> delegate = org.hamcrest.Matchers.arrayContaining(Values.box(chars));
    
    return new MatcherBuilder<char[]>(){} // specify the match target type via the type argument
            .then(Values::box, delegate)  // char[] -> Character[]
            .calm()                       // drop the delegate mismatch alert
            .alert((actual, d) -> d       // add a customized alert
                .appendText("chars: ")
                .appendText(Arrays.stream(actual).map(String::valueOf).collect(joining())))
            .build();
}
```
