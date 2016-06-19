#!/usr/bin/env bash

if [ "$COVERITY_ADDON" != "true" ] && [ -n "$COVERITY_SCAN_TOKEN" ]; then
    echo "Skipping run from non coverity addon with non zero COVERITY_SCAN_TOKEN."
    exit 0
fi

if [ "${FAST_BUILD}" == true ]; then
    if [ -z "$COVERITY_SCAN_TOKEN" ]; then
        mvn package
    fi
else
    mvn verify;
fi
