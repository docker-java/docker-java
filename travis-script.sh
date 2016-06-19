#!/usr/bin/env bash

if [ "$COVERITY_ADDON" != "true" ] && [ -n "$COVERITY_SCAN_TOKEN" ]; then
    echo "Skipping run from non coverity addon with non zero COVERITY_SCAN_TOKEN."
    exit 0
fi

if [ "${FAST_BUILD}" == true ]; then
    if [ "$COVERITY" == true ]; then
        export COVERITY_SCAN_BUILD_COMMAND="mvn package"
        curl -s "https://scan.coverity.com/scripts/travisci_build_coverity_scan.sh" | bash
    else
        mvn package
    fi
else
    if [ "$COVERITY" == true ]; then
        COVERITY_SCAN_BUILD_COMMAND="mvn verify"
        curl -s "https://scan.coverity.com/scripts/travisci_build_coverity_scan.sh" | bash
    else
        mvn verify
    fi
fi
