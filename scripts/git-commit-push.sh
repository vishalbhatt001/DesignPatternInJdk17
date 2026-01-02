#!/usr/bin/env bash
set -euo pipefail

MSG="${1:-chore: update project files}"
BRANCH="${2:-main}"

# Checkout the branch (create it if it doesn't exist)
if git rev-parse --verify "$BRANCH" >/dev/null 2>&1; then
  git checkout "$BRANCH"
else
  git checkout -b "$BRANCH"
fi

# Ensure 'origin' remote exists; add it if missing
git remote get-url origin >/dev/null 2>&1 || \
  git remote add origin https://github.com/vishalbhatt001/DesignPatternInJdk17.git

git add -A
if git diff --staged --quiet; then
  echo "No staged changes to commit."
  git push -u origin "$BRANCH"
else
  git commit -m "$MSG"
  git push -u origin "$BRANCH"
fi
