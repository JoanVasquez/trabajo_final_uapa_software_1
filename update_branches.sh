#!/bin/bash
source_branch="master"
branches=("SCRUM-13" "SCRUM-19" "SCRUM-23" "SCRUM-27" "SCRUM-32" "SCRUM-33" "SCRUM-1" "SCRUM-9")

for branch in "${branches[@]}"; do
    git checkout -b "${branch}"
    git merge "$source_branch"
    git push origin "${branch}"
    echo "Completed merging $branch into $source_branch"
done

git checkout "$source_branch"
