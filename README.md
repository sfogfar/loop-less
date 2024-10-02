# loop-less

## Required software

This project uses:

- [Volta](https://volta.sh/) to manage node and yarn. Shadow CLJS will be
  installed by yarn.

## Quick start guide

*Note:* The firebase config required to run this project is currently stored
locally.

- `$ yarn` to install node dependencies.
- `$ shadow-cljs watch frontend` to start a development server and nREPL
  instance.

If Conjure connects to the nREPL instance but the `js/` namespace is not
available try `:ConjureShadowSelect frontend`.

