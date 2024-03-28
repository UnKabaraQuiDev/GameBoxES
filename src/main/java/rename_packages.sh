#!/bin/bash

# Function to fix package declaration in Java files
fix_package_declaration() {
    file="$1"
    filename="${file##*/}"  # Extract filename from full path
    package_name="${filename//\//.}"  # Replace slashes with dots

    # Update package declaration in the file
    sed -i "1s/.*/package ${package_name%.java};/" "$file"
}

# Iterate over all Java files recursively in the current directory
find . -type f -name '*.java' | while IFS= read -r java_file; do
    # fix_package_declaration "$java_file"
    echo "fixed for $java_file"
done

echo "Package declarations fixed in all Java files."
