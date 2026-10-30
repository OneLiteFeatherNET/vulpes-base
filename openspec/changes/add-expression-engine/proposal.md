# Proposal

**Conventional Commits:** `feat(expression): evaluate content expressions at runtime`

## Why

Stelaris lets designers write formulas with EvalEx and test them in the editor (`Vulpes-Backend` #245). Such a formula reaches the game as text in the content release, but nothing can run it: a damage formula would have to be rewritten by hand in every game, and could then differ from what the editor showed. The game needs the same evaluator as the editor.

## What Changes

- `ExpressionEngine` evaluates named formulas and expressions with EvalEx, in the configuration the backend uses: the EvalEx defaults plus the project's functions.
- `Formula` describes a named formula with its parameter names and source. `NativeFunction` describes a function the game implements in Java, so that formulas can call it.
- All sources are parsed when the engine is created, so a broken formula fails at server start and not during a game. Duplicate names, unknown names and wrong argument counts fail with a message naming the formula.
- Results are read as number, boolean or string; a result of another type fails with a message naming the formula.
- EvalEx becomes a runtime dependency of the library.

## Capabilities

### New Capabilities
- `expression-engine`: Evaluating named formulas and expressions at runtime.

### Modified Capabilities
<!-- None. -->

## Non-goals

- The engine does not touch `Vulpes.bootstrap` or `VulpesContent`: generated code creates it, and games without formulas are not affected.
- No editor features such as test values or stubs.

## Impact

- Used by generated code of the Gradle plugin (`OneLiteFeatherNET/vulpes-gradle-plugin`, proposal `add-content-expressions`).
- The EvalEx version has to stay equal to the backend's so the editor and the game calculate alike.
