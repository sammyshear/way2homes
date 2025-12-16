#!/bin/bash

# Usage: ./scripts/publish-modrinth.sh <loader> <mc-version> <jar-file> <project-id> <mod-version>

set -e

LOADER=$1
MC_VERSION=$2
JAR_FILE=$3
PROJECT_ID=$4
MOD_VERSION=$5

TOKEN="${MODRINTH_TOKEN}"

if [ -z "$TOKEN" ]; then
    echo "Error:  MODRINTH_TOKEN environment variable is not set"
    exit 1
fi

if [ !  -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found: $JAR_FILE"
    exit 1
fi

VERSION_NUMBER="${MOD_VERSION}+${MC_VERSION}-${LOADER}"
VERSION_NAME="Way2Homes v${MOD_VERSION} [${LOADER^} ${MC_VERSION}]"
CHANGELOG_FILE="CHANGELOG.md"

if [ -f "$CHANGELOG_FILE" ]; then
    CHANGELOG=$(cat "$CHANGELOG_FILE" | jq -Rs .)
else
    CHANGELOG='"No changelog provided"'
fi

# Build JSON data based on loader
case "$LOADER" in
    fabric)
        LOADERS='["fabric"]'
        DEPENDENCIES='[{"project_id":"fabric-api","dependency_type":"required"},{"project_id":"modmenu","dependency_type":"optional"}]'
        ;;
    neoforge)
        LOADERS='["neoforge"]'
        DEPENDENCIES='[]'
        ;;
    paper)
        LOADERS='["paper"]'
        DEPENDENCIES='[{"project_id":"essentialsx","dependency_type":"optional"},{"project_id":"huskhomes","dependency_type":"optional"}]'
        ;;
    *)
        echo "Unknown loader: $LOADER"
        exit 1
        ;;
esac

JSON_DATA=$(cat <<EOF
{
  "name": "$VERSION_NAME",
  "version_number": "$VERSION_NUMBER",
  "changelog": $CHANGELOG,
  "dependencies": $DEPENDENCIES,
  "game_versions": ["$MC_VERSION"],
  "version_type": "release",
  "loaders": $LOADERS,
  "featured": true,
  "project_id": "$PROJECT_ID",
  "file_parts": ["file"]
}
EOF
)

echo "Publishing $VERSION_NAME..."
echo "File: $JAR_FILE"

# Upload to Modrinth
HTTP_CODE=$(curl -X POST "https://api.modrinth.com/v2/version" \
    -H "Authorization: $TOKEN" \
    -H "User-Agent: sammyshear/way2homes (GitHub Actions)" \
    -F "data=$JSON_DATA" \
    -F "file=@$JAR_FILE" \
    -o /tmp/modrinth-response.json \
    -w "%{http_code}" \
    -s)

if [ "$HTTP_CODE" -ge 200 ] && [ "$HTTP_CODE" -lt 300 ]; then
    echo "✓ Successfully published to Modrinth!"
    cat /tmp/modrinth-response.json | jq '.'
else
    echo "✗ Failed to publish to Modrinth (HTTP $HTTP_CODE)"
    cat /tmp/modrinth-response.json
    exit 1
fi