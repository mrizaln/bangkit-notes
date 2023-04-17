#!/usr/bin/env bash

cd AndroidStudioProjects

ls | while read f; do 
    tree_view="$(tree "$f" --gitignore -I 'test' -I 'androidTest' -I 'libs' -I 'res' -I '*gradle*' -P '*kt')"
    echo -e "\`\`\`text\n${tree_view}\n\`\`\`" > "$f/README.md"
    unset tree_view
done
