# Detail

Tapping a Home row opens `Todo Details` for that `todoItemId`. Close returns to the list.

## What it does

- `detail-title` is the top bar `Todo Details`.
- `detail-load` calls `GET /todos/{id}` from `DetailViewModel.fetchTodo`.
- `detail-header` shows `todo.title`, `User #<userId>` under the title (same label as Home via `TodoOwnerLabel`), a status chip (`Completed` or `Active`), and a display-only checkbox.
- `detail-back` is the Close icon, contentDescription `Back`.
- `detail-edit` is the Edit icon, contentDescription `Edit Todo`. It is a no-op.

## How a user opens it

- On Home, tap a todo card (`SingleTodoItem` `onTap` → `AppRoute.Detail(id)`).
- There is no deep link.

## How verify drives it

Preconditions: Home has loaded at least one row.

- **Open.** Tap `delectus aut autem`. Heading `Todo Details`. Title text matches the row. Body shows `User #1` under the title (jsonplaceholder todo id 1).
- **Owner label.** After Success, assert visible text `User #1` on Detail for todo id 1; it must match Home’s `User #<id>` formatting.
- **Chip.** Incomplete todos show `Active`. Completed show `Completed`.
- **Back.** Tap `Back`. Heading `Todo List` again. Same rows (5-minute list cache).
- **Proof.** `./verify screenshot` on Detail before Back.

Placeholder cards `Description` / `No description provided` and dates `13/02/2025` + `16/04/2026` are hardcoded in `Detail.kt`, not from the API.

## What usually lies

- `Edit Todo` looking unchanged after tap is success. Do not treat it as a missed tap.
- Checkbox on Detail does not mutate `todo.completed`.
- Staggered card enter (header, then Description, then Details) means an early screenshot can miss the last card. Wait ~400 ms after Success.
