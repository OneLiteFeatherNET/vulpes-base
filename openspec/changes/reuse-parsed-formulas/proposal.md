# Proposal

**Conventional Commits:** `perf(expression): reuse parsed formulas`

## Why

A game evaluates its formulas again and again: a damage formula runs for every hit, and a formula that evaluates per entity per tick runs many times each second. Every evaluation parses the formula's source again, and every nested formula call parses its source again too. That parsing costs about half or more of each call, so a server with many entities feels it as lag that has nothing to do with the game logic itself.

## What Changes

- `ExpressionEngine` keeps the parsed tree of every formula and expression it already parses when it is created at startup.
- Every evaluation and every nested formula call reuses that tree instead of parsing the source again.
- The public API, the results, the rounding, the error messages and the depth limit stay as they are.

## Capabilities

### New Capabilities
<!-- None. -->

### Modified Capabilities
- `expression-engine`: Formulas are parsed once, when the engine is created, and every evaluation reuses that parsed form.

## Non-goals

- No caching of results: every evaluation still computes its result from the arguments.
- No change to the EvalEx configuration; the functions and the defaults stay the same.

## Impact

- Only `ExpressionEngine` and `FormulaFunction` of `net.onelitefeather.vulpes.expression` change; generated code and games see no difference.
- Results must stay identical to those of a fresh engine, so existing tests keep passing unchanged.
