# ph-oton-icon

Free web icon libraries for the [ph-oton](https://github.com/phax/ph-oton) web stack,
split into one Maven artifact per icon library so an application only pulls in the
icon set it actually uses.

All artifacts live in the Maven group `com.helger.photon.icon` and depend only on
`ph-oton-icon-api`, which in turn depends solely on `com.helger.photon:ph-oton-html`
(for `ICSSClassProvider` and the HC node model). They intentionally do **not**
depend on `ph-oton-uicore` or `ph-oton-app`: the icon abstraction (`IIcon`,
`DefaultIcons`, `EDefaultIcon`) lives here in `ph-oton-icon-api`, and only the
`PhotonCSS` resource registration convenience stays in the ph-oton stack, which
consumes these artifacts (reversed dependency).

## Modules

| Artifact | Java package | Content |
|----------|--------------|---------|
| `ph-oton-icon-api`           | `com.helger.photon.icon.api`           | Icon abstraction: `IIcon`, `DefaultIcons`, `EDefaultIcon` |
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
mvn install
```

Requires Java 17+.

## License

Apache License, Version 2.0. The bundled icon fonts and stylesheets are
redistributed under their respective upstream licenses (see the `LICENSE`,
`NOTICE` and per-library license files inside each module).
