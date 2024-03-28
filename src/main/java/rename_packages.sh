#!/bin/bash

# Function to fix package declaration in Java files
fix_package_declaration() {
    local file="$1"
    local package=$(dirname "$file" | sed 's#/#.#g')
    local filename=$(basename "$file")
    local package_line="package ${package#".."};"

    # Replace the first line with the new package declaration
    sed -i "1s/.*/$package_line/" "$file"
}

# Iterate over all Java files recursively in the current directory
find . -type f -name '*.java' | while IFS= read -r java_file; do
    fix_package_declaration "$java_file"
    echo "fixed for $java_file"
done

echo "Package declarations fixed in all Java files."
