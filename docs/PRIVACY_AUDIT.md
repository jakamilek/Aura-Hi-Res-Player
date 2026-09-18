# Privacy fork dependency audit

## Audited on 2026-09-15

The `qobuz-privacy` branch is being audited for dependency and endpoint provenance before each release candidate build.

### Removed in this cleanup

- JioSaavn module and startup/device-router path.
- JioSaavn fallback from `YTPlayerUtils`.
- ShazamKit module dependency; recognition compatibility now uses a local enum and the recognition feature remains disabled.
- Qobuz third-party uptime/proxy reference from the settings uptime screen.
- Owner-hosted/keyless AI relay paths from `AiPlaylistService`; AI generation now requires an explicitly configured API key.
- Remote player-config updater; `RemotePlayerConfig` is now a no-network compatibility shim.
- Deezer migration network access; `DeezerSource` is now a no-network compatibility stub because the migration DI graph still references its type.
- ListenBrainz network client and automatic listen submission.
- Automatic external scrobbling from the player; `ScrobbleManager` is now a no-network compatibility shim so playback does not submit listening history or track metadata.

### Intentionally retained

- Direct Qobuz API and Qobuz playback/authentication.
- Tidal API/OAuth and playback.
- Spotify API/authentication and playback.
- YouTube/InnerTube code that is still structurally used by the main player. This is a separate cleanup target because it is deeply integrated with `MusicService`/`YTPlayerUtils`.
- TinyPinyin core for the existing lyric romanization path; the privacy build pins the JitPack tag `v2.0.3` rather than restoring the removed Aliyun Maven mirror.

### Deferred cleanup

- Last.fm source/settings/recommendation code is still present for compatibility, but the player-side scrobbling path is disabled in this privacy build. A later pass should remove or stub the remaining Last.fm network implementation and UI if Last.fm is not required.
- SoundCloud endpoints still need provenance tracing to determine whether they come from active source code or a retained dependency before removal.
- GMS Firebase/Drive dependencies remain to be audited against the GMS flavor separately.
- Other lyrics/provider modules remain until reference analysis proves they can be removed without breaking retained playback/provider flows.

### Important audit result

The earlier APK contained JioSaavn, ShazamKit and keyless AI paths in active source/dependency code. These were not merely harmless URL strings. They were therefore treated as real attack-surface reduction targets.

The old Qobuz proxy and licensing/recognition backends had already been removed in earlier privacy-fork commits.

The latest source fixes intentionally retain compatibility shims/stubs where removing a class outright would break the existing DI/source graph. These shims do not perform network I/O.

ListenBrainz was found to submit track title, artist, release and playback timing to `api.listenbrainz.org` when enabled; that network client and the player submission path have now been removed/disabled.
