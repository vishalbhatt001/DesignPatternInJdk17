#!/usr/bin/env bash
set -euo pipefail

MSG="${1:-chore: update project files}"
BRANCH="${2:-}"
if [ -n "$BRANCH" ]; then
  git checkout "$BRANCH"
fi

# Ensure 'origin' remote exists; add it if missing
git remote get-url origin >/dev/null 2>&1 || \
  git remote add origin https://github.com/vishalbhatt001/DesignPatternInJdk17.git

git add -A
if git diff --staged --quiet; then
  echo "No staged changes to commit."
else
  git commit -m "$MSG"
  git push
fi
