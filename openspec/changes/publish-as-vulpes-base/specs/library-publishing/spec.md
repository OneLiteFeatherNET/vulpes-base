# Spec Delta

## Purpose

Defines under which Maven coordinates the library is published.

## ADDED Requirements

### Requirement: Maven coordinates
The library SHALL be published with group `net.onelitefeather.vulpes` and artifactId `vulpes-base`.

#### Scenario: Generated POM
- **WHEN** the Maven publication is generated
- **THEN** its POM has groupId `net.onelitefeather.vulpes` and artifactId `vulpes-base`
