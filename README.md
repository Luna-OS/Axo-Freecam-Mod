# ☾ Axo Freecam

Freecam-Mod für **Minecraft 26.3** (Fabric) im **Luna-OS**-Stil.

Steuere deine Kamera unabhängig von deinem Spieler: Fliege frei herum und durch Blöcke
innerhalb deiner Sichtweite. Beim Ausschalten bist du sofort wieder an deiner ursprünglichen
Position. Funktioniert auch im Multiplayer – kann aber auf manchen Servern als Cheat gelten,
prüfe also vorher die Regeln.

Die Spielmechanik ist 1:1 die der bekannten [Freecam-Mod](https://github.com/MinecraftFreecam/Freecam)
(MIT-Lizenz), portiert auf ein reines Fabric-26.3-Projekt und mit Luna-OS-Design versehen
(Mond-Icon, lila Einstellungsmenü, eigene Tastenkategorie „☾ Axo Freecam“).

## Installation

1. [Fabric Loader](https://fabricmc.net/use/) **0.19.5+** für Minecraft **26.3** installieren.
2. [Fabric API](https://modrinth.com/mod/fabric-api) in den `mods`-Ordner legen.
3. `axo-freecam-<version>.jar` in den `mods`-Ordner legen.
4. Optional: [Mod Menu](https://modrinth.com/mod/modmenu) für den Einstellungs-Button in der Modliste.

Cloth Config (für das Einstellungsmenü) ist bereits in der Mod enthalten.
Die originale Freecam-Mod nicht gleichzeitig installieren – beide steuern dieselbe Kamera.

## Tastenbelegung

| Name                  | Beschreibung                                                                          | Standard     |
|-----------------------|---------------------------------------------------------------------------------------|--------------|
| Freecam umschalten    | Schaltet Freecam an/aus                                                               | `F4`         |
| Konfigurationsmenü    | Öffnet die Einstellungen                                                              | nicht belegt |
| Spieler kontrollieren | Gibt die Steuerung an den Spieler zurück, die Kamera bleibt wo sie ist               | nicht belegt |
| Stativ zurücksetzen   | Setzt eine Stativ-Kamera zurück (zusammen mit einer Hotbar-Taste `1`–`9`)             | nicht belegt |
| Spieler-Kontur        | Schaltet die Umrandung deines Spielers an/aus                                         | nicht belegt |

**Stativ-Modus:** `F4` + Hotbar-Taste (`1`–`9`) speichert bzw. öffnet bis zu 9 feste Kamerapositionen,
zwischen denen du jederzeit wechseln kannst.

## Einstellungen

Alle Optionen der Original-Mod sind vorhanden:

- **Bewegung:** Flugmodus (Standard/Kreativ), horizontale und vertikale Geschwindigkeit
- **Kollision:** durch transparente/öffnenbare/alle Blöcke fliegen, eigene Block-Liste (IDs & Regex)
- **Darstellung:** Start-Perspektive, Spieler anzeigen, Hand anzeigen, volle Helligkeit,
  Unterwasser-Nebel, Spieler-Kontur
- **Nützliches:** bei Schaden deaktivieren, Spieler einfrieren, Interaktion erlauben (Kamera/Spieler)
- **Multiplayer:** Server-Whitelist/-Blacklist
- **Benachrichtigungen:** Freecam- und Stativ-Meldungen

Die Einstellungen werden in `config/axofreecam.json` gespeichert.

## Selbst bauen

Benötigt **JDK 25**.

```sh
./gradlew build
```

Die fertige Mod liegt danach in `build/libs/axo-freecam-<version>.jar`.
Zum Testen im Spiel: `./gradlew runClient`.

## Lizenz

MIT – siehe [LICENSE](LICENSE). Basiert auf Freecam von den MinecraftFreecam-Mitwirkenden.
