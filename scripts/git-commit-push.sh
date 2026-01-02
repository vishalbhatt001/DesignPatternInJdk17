#!/usr/bin/env bash
set -euo pipefail

# Generate a unique default message each run (ISO UTC timestamp + random suffix)
DEFAULT_MSG="chore: update project files $(date -u +%Y%m%dT%H%M%SZ)-$RANDOM"
MSG="${1:-$DEFAULT_MSG}"
BRANCH="${2:-main}"

# Echo the message being used for transparency
echo "Commit message: $MSG"

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
