# Changelog

## [0.6.1](https://github.com/OneLiteFeatherNET/vulpes-base/compare/v0.6.0...v0.6.1) (2026-10-31)


### Performance Improvements

* **expression:** reuse parsed formulas ([#115](https://github.com/OneLiteFeatherNET/vulpes-base/issues/115)) ([652355f](https://github.com/OneLiteFeatherNET/vulpes-base/commit/652355f248f5e80556762f28e32c951c76e285cb))

## [0.6.0](https://github.com/OneLiteFeatherNET/vulpes-base/compare/v0.5.0...v0.6.0) (2026-10-30)


### Features

* **expression:** evaluate content expressions at runtime ([#112](https://github.com/OneLiteFeatherNET/vulpes-base/issues/112)) ([0300f53](https://github.com/OneLiteFeatherNET/vulpes-base/commit/0300f531fabb4550fed94e052f2a0d5baf1b8239))

## 0.5.0 (2026-10-30)


### ⚠ BREAKING CHANGES

* the library is published as net.onelitefeather.vulpes:vulpes-base instead of net.onelitefeather:vulpes.
* **registry:** generated code registers through VulpesContent and Vulpes.bootstrap; the static accessors fromKey, values, registry, roots and children and the generated supertypes are removed, and CustomAttribute gains minimumValue, translationKey and synced.
* **font:** add code-based custom font symbol registry ([#101](https://github.com/OneLiteFeatherNET/vulpes-base/issues/101))
* **attribute:** add code-based custom attribute registry ([#99](https://github.com/OneLiteFeatherNET/vulpes-base/issues/99))

### Features

* add background glyph handling ([#90](https://github.com/OneLiteFeatherNET/vulpes-base/issues/90)) ([e23f27a](https://github.com/OneLiteFeatherNET/vulpes-base/commit/e23f27a6904d2b5a81b41559e4f227ebdb0ae4cf))
* **advancement:** add code-based custom advancement registry ([#103](https://github.com/OneLiteFeatherNET/vulpes-base/issues/103)) ([1caaff5](https://github.com/OneLiteFeatherNET/vulpes-base/commit/1caaff5b4fb72fff76c2e741c05644d35cea6a50))
* **attribute:** add code-based custom attribute registry ([#99](https://github.com/OneLiteFeatherNET/vulpes-base/issues/99)) ([b8f1412](https://github.com/OneLiteFeatherNET/vulpes-base/commit/b8f1412a8f02730bba4189845df414c864df89b9))
* **font:** add code-based custom font symbol registry ([#101](https://github.com/OneLiteFeatherNET/vulpes-base/issues/101)) ([cfa2d1b](https://github.com/OneLiteFeatherNET/vulpes-base/commit/cfa2d1b64c764ab9b435ad62f2ed67ffacf034e0))
* **item:** add code-based custom item registry ([#102](https://github.com/OneLiteFeatherNET/vulpes-base/issues/102)) ([b6a872d](https://github.com/OneLiteFeatherNET/vulpes-base/commit/b6a872ddeb1ff409eafd55ec1c099f5e94d26c77))
* **project:** add changelog file ([5c40de5](https://github.com/OneLiteFeatherNET/vulpes-base/commit/5c40de5dc335d6ac2980274f6b0285930d39958e))
* **registry:** let generated content register itself ([#108](https://github.com/OneLiteFeatherNET/vulpes-base/issues/108)) ([255c292](https://github.com/OneLiteFeatherNET/vulpes-base/commit/255c29280ef9a73290655293733c2c337a9c7669))
* **sound:** add code-based custom sound registry ([#100](https://github.com/OneLiteFeatherNET/vulpes-base/issues/100)) ([cd03f2a](https://github.com/OneLiteFeatherNET/vulpes-base/commit/cd03f2a6b187382b108f6d46b570ba2fa45efbb5))


### Bug Fixes

* **deps:** update bom to v1.7.1 ([#71](https://github.com/OneLiteFeatherNET/vulpes-base/issues/71)) ([4ccbd8c](https://github.com/OneLiteFeatherNET/vulpes-base/commit/4ccbd8c4d12256931baab18100d2665628dc24cf))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.1 ([#48](https://github.com/OneLiteFeatherNET/vulpes-base/issues/48)) ([574e8d1](https://github.com/OneLiteFeatherNET/vulpes-base/commit/574e8d1ef2eac9ca5dd9c75f7a909e971c755c92))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.2 ([#51](https://github.com/OneLiteFeatherNET/vulpes-base/issues/51)) ([73cbbc5](https://github.com/OneLiteFeatherNET/vulpes-base/commit/73cbbc5e02c93e9aabfd67cafb5cb9e6c0454155))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.3 ([#53](https://github.com/OneLiteFeatherNET/vulpes-base/issues/53)) ([9c14541](https://github.com/OneLiteFeatherNET/vulpes-base/commit/9c14541341e8f557f857e870203bdb486294296d))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.4 ([#60](https://github.com/OneLiteFeatherNET/vulpes-base/issues/60)) ([33d2c3a](https://github.com/OneLiteFeatherNET/vulpes-base/commit/33d2c3afe6a76f9bff66dcb4a3bbb34104f661ec))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.5 ([#67](https://github.com/OneLiteFeatherNET/vulpes-base/issues/67)) ([1443223](https://github.com/OneLiteFeatherNET/vulpes-base/commit/14432239d78557b8222710e6606c8052ac1bebef))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.6.7 ([#69](https://github.com/OneLiteFeatherNET/vulpes-base/issues/69)) ([5371894](https://github.com/OneLiteFeatherNET/vulpes-base/commit/5371894e66c69b215e4ee0decd57e53f2cb9183b))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.7.2 ([#75](https://github.com/OneLiteFeatherNET/vulpes-base/issues/75)) ([c53cac5](https://github.com/OneLiteFeatherNET/vulpes-base/commit/c53cac5d13732f623d5151ba8f3afd0cb5b3de70))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.8.1 ([#79](https://github.com/OneLiteFeatherNET/vulpes-base/issues/79)) ([abebf09](https://github.com/OneLiteFeatherNET/vulpes-base/commit/abebf097b4e947bd65f5798fa5730709e620327a))
* **deps:** update dependency net.onelitefeather:mycelium-bom to v1.8.2 ([#83](https://github.com/OneLiteFeatherNET/vulpes-base/issues/83)) ([e3219e8](https://github.com/OneLiteFeatherNET/vulpes-base/commit/e3219e8949af880890ca7baede3308780f6ad0e5))
* **deps:** update dependency net.worldseed.multipart:worldseedentityengine to v11.5.6 ([#47](https://github.com/OneLiteFeatherNET/vulpes-base/issues/47)) ([3b0a559](https://github.com/OneLiteFeatherNET/vulpes-base/commit/3b0a5596343e724a7d8a0f4f51c9800871523b66))
* **deps:** update dependency net.worldseed.multipart:worldseedentityengine to v11.6.1 ([#66](https://github.com/OneLiteFeatherNET/vulpes-base/issues/66)) ([fae8ebd](https://github.com/OneLiteFeatherNET/vulpes-base/commit/fae8ebd1f61923189f78e8137e9166dd17769243))
* **deps:** update dependency net.worldseed.multipart:worldseedentityengine to v13 ([#80](https://github.com/OneLiteFeatherNET/vulpes-base/issues/80)) ([26fe1c4](https://github.com/OneLiteFeatherNET/vulpes-base/commit/26fe1c48e80a78c62f16fd2026bd3912aba3c751))
* **deps:** update dependency net.worldseed.multipart:worldseedentityengine to v13.0.4 ([#89](https://github.com/OneLiteFeatherNET/vulpes-base/issues/89)) ([5b1a33a](https://github.com/OneLiteFeatherNET/vulpes-base/commit/5b1a33a82cfc2383120835a4df73a9d2fbbcdafb))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.19 ([#91](https://github.com/OneLiteFeatherNET/vulpes-base/issues/91)) ([736878a](https://github.com/OneLiteFeatherNET/vulpes-base/commit/736878af099e4216f680e8ada5ecf59be18bd1db))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.20 ([#95](https://github.com/OneLiteFeatherNET/vulpes-base/issues/95)) ([028fb38](https://github.com/OneLiteFeatherNET/vulpes-base/commit/028fb38335aa0b9eb7a141bfa7e5c76b37a1a7a6))
* **deps:** update mycelium-bom to 1.8.9 ([#98](https://github.com/OneLiteFeatherNET/vulpes-base/issues/98)) ([1724e01](https://github.com/OneLiteFeatherNET/vulpes-base/commit/1724e0166e14bf7b7d1223aabfe999f9471f6468))
* **item:** decode registry-backed item components ([#107](https://github.com/OneLiteFeatherNET/vulpes-base/issues/107)) ([cdaad73](https://github.com/OneLiteFeatherNET/vulpes-base/commit/cdaad7364618923f43b043b857b276571ad20fb3))


### Documentation

* correct since tags for the 0.5.0 release ([79b70e6](https://github.com/OneLiteFeatherNET/vulpes-base/commit/79b70e62bee174166e92ee11fe78c1f5cf1ba41c))


### Build System

* publish as net.onelitefeather.vulpes:vulpes-base ([#109](https://github.com/OneLiteFeatherNET/vulpes-base/issues/109)) ([9dfecdb](https://github.com/OneLiteFeatherNET/vulpes-base/commit/9dfecdb412074b520a97ba424914307210edca8e))
