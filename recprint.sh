#!/bin/bash

# Function to process and print a file
process_file() {
    local file="$1"
    local relative_path="${file#./}"  # Remove leading "./" if present
    echo "=== $relative_path ==="
    
    # Loop through the file, skipping lines starting with "import" and empty lines
    while IFS= read -r line; do
        if [[ "$line" =~ ^import|^$ ]]; then
            continue
        fi
        echo "$line"
    done < "$file"
    echo
}

# Find all files and loop through them
find "app/src/main/java/ru/snowadv/comapr/" -type f | while read -r file; do
    process_file "$file"
done

