#!/usr/bin/env bash

target="$(echo "$1" | sed 's:/*$::')"       # remove trailing slash
destination="$(echo "$2" | sed 's:/*$::')"  # remove trailing slash

link_from="../$destination/${target##*/}"

if [[ -z "$1" || -z "$2" ]]; then
    echo "Please specify target and destination"
    echo
    echo "Usage: $0 <target> <destination>"
    echo
    echo "Example: $0 ~/AndroidStudioProjects/MyApp 'M.5. Belajar Pengembangan Aplikasi Android Intermediate/01_advanced_ui/'"
    echo
elif ! [[ -d "$destination" ]]; then
    echo "Destination must be directory"
else
    echo "Applying these operations:"

    echo "      mv \"$target\" \"$destination/\""
    echo "      ln -s \"$link_from\" \"./AndroidStudioProjects/${target##*/}\""

    read -p "Continue? [y/N] " ans
    case "$ans" in
        "y" | "Y" | "yes" | "YES" | "Yes")
            mv "$target" "$destination/"
            ln -s "$link_from" "./AndroidStudioProjects/${target##*/}"

            echo "Operation done"

            ./attach.sh
            ;;
        *)
            echo "Operation cancelled"
            ;;
    esac
fi
