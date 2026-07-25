# ph-oton-icon

Free web icon libraries for the [ph-oton](https://github.com/phax/ph-oton) web stack,
split into one Maven artifact per icon library so an application only pulls in the
icon set it actually uses.

All artifacts live in the Maven group `com.helger.photon.icon`. Each library module
depends on `com.helger.photon:ph-oton-uicore`, which provides the icon abstraction
(`IIcon`, `DefaultIcons`, `EDefaultIcon`) and the HC node model. Only the `PhotonCSS`
resource registration convenience (`ph-oton-app`) is left out &ndash; register a
library's `getAllCSSFiles ()` on the ph-oton side.

## Modules

| Artifact | Java package | Content |
|----------|--------------|---------|
| `ph-oton-icon-fontawesome4`  | `com.helger.photon.icon.fontawesome4`  | FontAwesome 4.7.0 |
| `ph-oton-icon-fontawesome5`  | `com.helger.photon.icon.fontawesome5`  | FontAwesome 5.15.4 |
| `ph-oton-icon-materialdesign` | `com.helger.photon.icon.materialdesign` | Material Design Icons 3.0.1 |
| `ph-oton-icon-bootstrap`     | `com.helger.photon.icon.bootstrap`     | Bootstrap Icons 1.11.3 |

## Usage

Add the module for the icon set you want, e.g. Bootstrap Icons:

```xml
<dependency>
  <groupId>com.helger.photon.icon</groupId>
  <artifactId>ph-oton-icon-bootstrap</artifactId>
  <version>1.0.0</version>
</dependency>
```

Each library module exposes:

* `E...Icon` &ndash; the enum of all icons of that library. It implements `IIcon`,
  so `getCSSClass ()` returns the CSS class name and `getAsNode ()` returns a
  self-contained `HCI` node. `setAsDefault ()` wires the library into
  `DefaultIcons` for the semantic `EDefaultIcon` roles.
* `EIconCSSPathProvider` &ndash; the `ICSSPathProvider`s for the library's CSS files;
  `E...Icon.getAllCSSFiles ()` returns them for registration (register them with
  `PhotonCSS` on the ph-oton side).
* `C...CSS` &ndash; the raw `ICSSClassProvider` constants for every CSS class.

The bundled font and CSS resources are served from the classpath under
`external/<library>/<version>/...` (and `ph-oton/...` for the supplementary
ph-oton stylesheets).

## Building

```bash
mvn clean install
```

Requires Java 17+.

## License

Apache License, Version 2.0. The bundled icon fonts and stylesheets are
redistributed under their respective upstream licenses (see the `LICENSE`,
`NOTICE` and per-library license files inside each module).


## News and noteworthy

v1.0.0 - work in progress
* Initial release
* Extracted the icon libraries from the `ph-oton-icon` module of [ph-oton](https://github.com/phax/ph-oton) into this standalone project
* The Maven groupId is `com.helger.photon.icon`
* One artifact per icon library, so an application only pulls in the icon set it uses: FontAwesome 4.7.0, FontAwesome 5.15.4, Material Design Icons 3.0.1 and Bootstrap Icons 1.11.3
* The icon enums implement `IIcon` and use `DefaultIcons` / `EDefaultIcon` from `com.helger.photon:ph-oton-uicore`
* Requires Java 17 or later

