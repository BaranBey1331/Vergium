#!/bin/bash
set -euo pipefail

echo "🚀 Vergium integrity check starting..."

FILES=(
  "src/main/resources/pack.mcmeta"
  "src/main/resources/META-INF/mods.toml"
  "src/main/resources/vergium.mixins.json"
  "build.gradle"
  "settings.gradle"
)

echo "🔍 Checking required files..."
for file in "${FILES[@]}"; do
  if [ ! -f "$file" ]; then
    echo "❌ Missing required file: $file"
    exit 1
  fi
done
echo "✅ Required files are present."

echo "🔍 Validating mixin configuration..."
if ! grep -q '"MixinLevelRenderer"' src/main/resources/vergium.mixins.json; then
  echo "❌ Mixin config is missing MixinLevelRenderer."
  exit 1
fi

if ! grep -q 'config="vergium.mixins.json"' src/main/resources/META-INF/mods.toml; then
  echo "❌ mods.toml is missing the Vergium mixin config."
  exit 1
fi
echo "✅ Mixin configuration looks valid."

echo "🧪 Running unit tests..."
./gradlew test --no-daemon
echo "✅ Unit tests passed."

echo "🔨 Running full build..."
./gradlew build --no-daemon
echo "✅ Build passed."

echo "🎉 Vergium integrity check completed successfully."
