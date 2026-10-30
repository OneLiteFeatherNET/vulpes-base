# Spec Delta

## Purpose

Lets generated content code register its entries with `vulpes-base` at server start, without `vulpes-base` referencing generated types.

## ADDED Requirements

### Requirement: No generated code in the library
The library SHALL compile and pass its tests without any generated source, and SHALL NOT reference types of a content project.

#### Scenario: Build without content
- **WHEN** the library is built from a clean checkout
- **THEN** the build succeeds and no source directory for generated code exists

### Requirement: Content registration
A content project SHALL register through an implementation of `VulpesContent` that lists its items, attributes, sounds, font symbols, advancements and alias keys. `Vulpes.bootstrap(VulpesContent...)` SHALL return immutable registries that contain the entries of all given contents.

#### Scenario: Two content projects
- **WHEN** `Vulpes.bootstrap(cygnus, manis)` is called with two contents that have distinct keys
- **THEN** the returned item registry contains the items of both

#### Scenario: Registries are independent
- **WHEN** `Vulpes.bootstrap` is called twice with different contents
- **THEN** each returned registries instance contains only the entries of its own call

### Requirement: Bootstrap validation
`Vulpes.bootstrap` SHALL fail with a message that names the offending keys when a key occurs twice within a type, when an alias key equals an entry key or points to a key without entry, or when an advancement parent is missing or the parents form a cycle.

#### Scenario: Duplicate key across contents
- **WHEN** two contents both contain an item with key `cygnus:iron_sword`
- **THEN** bootstrap fails and the message names `cygnus:iron_sword`

#### Scenario: Cyclic advancements
- **WHEN** advancement `a` has parent `b` and `b` has parent `a`
- **THEN** bootstrap fails and the message names both keys

### Requirement: Alias resolution
A registry returned by bootstrap SHALL resolve an alias key to the entry of its current key and SHALL NOT list alias keys in `keys()` or `values()`.

#### Scenario: Renamed item
- **WHEN** the content maps alias `cygnus:old_sword` to `cygnus:iron_sword`
- **THEN** `items().get(cygnus:old_sword)` returns the item `cygnus:iron_sword` and `items().keys()` does not contain `cygnus:old_sword`

### Requirement: Public factories
The library SHALL offer validating public factories for every content type (`CustomItem.of`, `CustomAttribute.of`, `CustomSound.of`, `CustomFontSymbol.of`, `CustomAdvancement.builder`) and SHALL apply vanilla component JSON to an item stack builder through `ItemComponents.apply`.

#### Scenario: Invalid attribute
- **WHEN** `CustomAttribute.of` is called with a default value above the maximum value
- **THEN** it throws an exception that names the key

#### Scenario: Item components
- **WHEN** `ItemComponents.apply` receives `minecraft:lore` with a list of text components
- **THEN** the built item stack carries that lore

### Requirement: Attribute contract
`CustomAttribute` SHALL expose `minimumValue`, `defaultValue`, `maximumValue`, a nullable `translationKey` and `synced`, and its factory SHALL require finite values with minimum <= default <= maximum.

#### Scenario: Minimum above default
- **WHEN** `CustomAttribute.of` is called with a minimum value above the default value
- **THEN** it throws an exception that names the key
