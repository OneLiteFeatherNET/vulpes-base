# Spec Delta

## Purpose

Defines how a game evaluates its formulas without parsing their sources again on every call.

## ADDED Requirements

### Requirement: Formulas are parsed once
The engine SHALL parse every formula and expression source once, when it is created, and every evaluation, including every nested formula call, SHALL reuse that parsed form. Results, rounding, error messages and the recursion depth limit SHALL stay the same as without reuse.

#### Scenario: Repeated evaluation
- **WHEN** a formula is evaluated many times with the same arguments
- **THEN** every result equals the result of a fresh engine evaluating it once

#### Scenario: Nested call
- **WHEN** a formula calls another formula
- **THEN** the called formula uses its parsed form and is not parsed again

#### Scenario: Concurrent evaluation
- **WHEN** several threads evaluate formulas of the same engine at once
- **THEN** every thread gets the correct result for its own arguments

#### Scenario: Recursive formula
- **WHEN** a formula calls itself without end
- **THEN** evaluation fails at the depth limit with a message naming the formula
