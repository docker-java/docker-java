#!/bin/sh
# Read the marker line to echo it after stdin is closed
read -r marker_line_after_eof_from_stdin
# Echo all lines received from stdin
while read -r line; do echo "$line"; done
# Show that stdout still function after closing stdin
echo "$marker_line_after_eof_from_stdin"
