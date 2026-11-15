#!/usr/bin/env bash
# Replays a recorded run, e.g.  scripts/replay.sh scripts/stage1-victory.txt
# Every line of the file is exactly what a human would type at a prompt.
# Stage 1 scripts need the Stage 1 crowd:  scripts/replay.sh scripts/stage1-victory.txt --regular-only
set -e
cd "$(dirname "$0")/.."
[ -d out ] || scripts/build.sh
SCRIPT="${1:?usage: scripts/replay.sh <script file> [--regular-only]}"
shift
java -cp out restaurantrush.boundary.Main "$@" < "$SCRIPT"
