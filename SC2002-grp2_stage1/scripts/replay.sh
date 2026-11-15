#!/usr/bin/env bash
# Replays a recorded run, e.g.  scripts/replay.sh scripts/stage1-victory.txt
# Every line of the file is exactly what a human would type at a prompt.
set -e
cd "$(dirname "$0")/.."
[ -d out ] || scripts/build.sh
java -cp out restaurantrush.boundary.Main < "${1:?usage: scripts/replay.sh <script file>}"
