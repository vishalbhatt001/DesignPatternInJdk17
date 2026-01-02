#!/usr/bin/env bash
set -euo pipefail

MSG="${1:-chore: update project files}"
BRANCH="${2:-}"
if [ -n "$BRANCH" ]; then
  git checkout "$BRANCH"
fi

git add -A
if git diff --staged --quiet; then
  echo "No staged changes to commit."
else
  git commit -m "$MSG"
  git push
fi
