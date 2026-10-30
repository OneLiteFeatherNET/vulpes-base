# Spec Delta

## Purpose

Defines how a game evaluates the formulas and expressions of its content at runtime.

## ADDED Requirements

### Requirement: Evaluate named formulas
The engine SHALL evaluate a formula or expression by name with positional arguments, using the EvalEx default configuration plus the project's functions.

#### Scenario: Formula with arguments
- **WHEN** formula `armorDamage(base, armor)` with source `base * (1 - armor / 100)` is evaluated with 100 and 25
- **THEN** the number 75 is returned

#### Scenario: Function calling a function
- **WHEN** an expression calls the project formula `armorDamage`
- **THEN** the result uses the value of that formula

### Requirement: Native functions
The engine SHALL let formulas call functions the game implements, passing numbers as `BigDecimal`, booleans as `Boolean` and strings as `String`.

#### Scenario: Native boolean function
- **WHEN** a formula calls the native function `inArena("spawn")` and the game returns true
- **THEN** the formula sees the value true

### Requirement: Fail at creation
The engine SHALL parse every source when it is created and SHALL fail with a message naming the formula for a syntax error, a duplicate name, an unknown function or variable, or a wrong argument count.

#### Scenario: Syntax error
- **WHEN** the engine is created with a formula with the source `1 +`
- **THEN** creation fails and the message names the formula

#### Scenario: Duplicate name
- **WHEN** a formula and an expression have the same name
- **THEN** creation fails and the message names the formula

### Requirement: Typed results
The engine SHALL return the result as the requested type and SHALL fail with a message naming the formula when the result has another type.

#### Scenario: Wrong result type
- **WHEN** a formula evaluates to a boolean and a number is requested
- **THEN** evaluation fails and the message names the formula and both types

### Requirement: Bounded recursion
The engine SHALL fail with a message naming the formula when a chain of formula calls is deeper than 32.

#### Scenario: Recursive formula
- **WHEN** a formula calls itself without end
- **THEN** evaluation fails and the message names the formula

### Requirement: Thread safety
An engine SHALL be safe to use from several threads at once and SHALL keep no global state.

#### Scenario: Two engines
- **WHEN** two engines define a formula with the same name and different sources
- **THEN** each evaluates its own source
