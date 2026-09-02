# Loading and error

Home and Detail wrap API state in `Crossfade`. Loading is a skeleton or spinner. Error is red body text.

## What it does

- `home-skeleton` draws 6 `SkeletonTodoItem` rows while `HomeUiState.Loading`.
- `home-error` centers `HomeUiState.Error.message` in `colorScheme.error`.
- `detail-spinner` is a 48 dp `CircularProgressIndicator` while `DetailUiState.Loading`.
- `detail-error` centers `DetailUiState.Error.message` the same way.

## How a user opens it

- Cold launch with no cache and a slow or dead network — Home stays on skeleton, then Success or Error.
- Open a Detail id the API rejects — spinner, then Error.

## How verify drives it

Preconditions: decide whether you want the error path. Default jsonplaceholder returns 200.

- **Load.** Cold launch, dump immediately. Six skeleton rows, no `delectus aut autem`.
- **Success.** Wait. Heading still `Todo List`. Real titles replace the skeleton.
- **Error.** Point `BASE_URL` in `local.properties` at a closed port, delete the process so the 5-minute cache is gone, relaunch. Centered error text, not a list.
- **Proof.** `./verify screenshot` on the state you claimed.

## What usually lies

- `RepositoryImpl` keeps expired cache on a failed refetch. Killing only the activity is not enough if the process lived. Force-stop the package before claiming Error.
- `./verify test` never hits the network. A green unit test is not a Home load receipt.
