#!/usr/bin/env bash
# Builds (if needed) and starts the game interactively.
set -e
cd "$(dirname "$0")/.."
[ -d out ] || scripts/build.sh
java -cp out restaurantrush.boundary.Main
