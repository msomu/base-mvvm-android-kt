# Home list

Launch shows `Todo List`. After jsonplaceholder returns, each row is a title plus `User #<id>`.

## What it does

- `home-title` is the top bar `Todo List`.
- `home-load` fetches `GET /todos` on `HomeViewModel` init.
- `home-row` renders `todo.title` and `User #${todo.userId}`.
- `home-done` strikes through a completed title and dims the card.

## How a user opens it

- Launch the app. Home is the start route (`AppRoute.Home`).
- On Detail, tap `Back`.

## How verify drives it

Preconditions: network can reach jsonplaceholder, or a 5-minute in-memory cache from an earlier success.

- **Ready.** Dump the tree. Heading `Todo List` exists.
- **Rows.** After load, a node `delectus aut autem` exists (todo id 1 on the default API). `User #1` is on that card.
- **Open detail.** Tap that row. Heading becomes `Todo Details`.
- **Proof.** `./verify screenshot` (capture-only) then `./verify logcat`. Do not pass `--launch` after the tap — that resets to Home.

JVM receipt: `./verify test` runs `:app:testDebugUnitTest` (`ExampleUnitTest` only — no HomeViewModel host test).

## What usually lies

- A screenshot of six gray cards is Loading, not Success. Wait for a real title before capture.
- Airplane mode after a successful fetch still shows the list for 5 minutes (`RepositoryImpl` cache). That is not a live GET.
- Checkbox taps do nothing. A dump that still shows the same `completed` state is expected, not a missed tap.
