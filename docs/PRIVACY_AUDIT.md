# Privacy fork dependency audit

## Audited on 2026-09-14

The `qobuz-privacy` branch was audited for dependency and endpoint provenance before the next build.

### Removed in this cleanup

- JioSaavn module and startup/device-router path.
- JioSaavn fallback from `YTPlayerUtils`.
- ShazamKit module dependency; recognition compatibility now uses a local enum and the recognition feature remains disabled.
- Qobuz third-party uptime/proxy reference from the settings uptime screen.
- Owner-hosted/keyless AI relay paths from `AiPlaylistService`; AI generation now requires an explicitly configured API key.
- Remote player-config updater; `RemotePlayerConfig` is now a no-network compatibility shim.
- Deezer migration network access; `DeezerSource` is now a no-network compatibility stub because the migration DI graph still references its type.

### Intentionally retained

- Direct Qobuz API and Qobuz playback/authentication.
- Tidal API/OAuth and playback.
- Spotify API/authentication and playback.
- YouTube/InnerTube code that is still structurally used by the main player. This is a separate cleanup target because it is deeply integrated with `MusicService`/`YTPlayerUtils`.

### Deferred cleanup

- Last.fm is still present and opt-in, but remains outside the requested Qobuz/Tidal/Spotify core. It should be converted to a no-op compatibility layer in a later pass if Last.fm is not required.
- GMS Firebase/Drive dependencies remain to be audited against the GMS flavor separately.
- Other lyrics/provider modules remain until reference analysis proves they can be removed without breaking retained playback/provider flows.

### Important audit result

The earlier APK contained JioSaavn, ShazamKit and keyless AI paths in active source/dependency code. These were not merely harmless URL strings. They were therefore treated as real attack-surface reduction targets.

The old Qobuz proxy and licensing/recognition backends had already been removed in earlier privacy-fork commits.

The latest source fixes are intentionally kept as compatibility shims/stubs where removing the class entirely would break the existing DI/source graph; the stubs do not perform network I/O.
