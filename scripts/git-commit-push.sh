#!/usr/bin/env bash
set -euo pipefail

# Generate a unique default message each run (ISO UTC timestamp + random suffix)
DEFAULT_MSG="chore: update project files $(date -u +%Y%m%dT%H%M%SZ)-$RANDOM"
USER_MSG="${1:-}"
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

# If user didn't provide a message, build one from staged file changes
if [ -z "$USER_MSG" ]; then
  staged="$(git diff --cached --name-status || true)"
  if [ -z "$staged" ]; then
    MSG="$DEFAULT_MSG"
  else
    # Build concise messages like: path/to/file: Modified; old -> new: Renamed
    msg_parts=()
    while IFS=$'\t' read -r status a b; do
      case "$status" in
        A) action="Added" ; entry="$a: $action" ;;
        M) action="Modified" ; entry="$a: $action" ;;
        D) action="Deleted" ; entry="$a: $action" ;;
        R*) action="Renamed" ; entry="$b: $action (from $a)" ;;
        C*) action="Copied" ; entry="$b: $action" ;;
        *) entry="$a: $status" ;;
      esac
      msg_parts+=("$entry")
    done <<< "$staged"
    # join with " ; "
    MSG="$(IFS=' ; '; echo "${msg_parts[*]}")"
  fi
else
  MSG="$USER_MSG"
fi

# Echo the message being used for transparency
echo "Commit message: $MSG"

if git diff --staged --quiet; then
  echo "No staged changes to commit."
  git push -u origin "$BRANCH"
else
  git commit -m "$MSG"
  git push -u origin "$BRANCH"
fi
