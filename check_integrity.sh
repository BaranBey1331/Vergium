#!/bin/bash

echo "🚀 Vergium CI/CD Integrity Checker başlatılıyor..."

# 1. Dosya Kontrolleri
echo "🔍 Kritik dosyalar kontrol ediliyor..."
FILES=("src/main/resources/pack.mcmeta" "src/main/resources/META-INF/mods.toml" "build.gradle" "settings.gradle")

for file in "${FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo "❌ HATA: $file bulunamadı!"
        exit 1
    fi
done
echo "✅ Dosya kontrolleri başarılı."

# 2. Mixin Konfigürasyonu
echo "🔍 Mixin konfigürasyonu kontrol ediliyor..."
if ! grep -q "vergium.mixins.json" src/main/resources/META-INF/mods.toml; then
    echo "❌ HATA: mods.toml içinde mixin konfigürasyonu eksik!"
    exit 1
fi
echo "✅ Mixin yapılandırması doğrulanmış."

# 3. Derleme Testi (Dry Run)
echo "🔨 Derleme testi yapılıyor (./gradlew compileJava)..."
if ./gradlew compileJava; then
    echo "✅ Derleme başarılı."
else
    echo "❌ HATA: Kod derlenemiyor! Lütfen hataları kontrol edin."
    exit 1
fi

echo "🎉 Tüm kontroller başarıyla tamamlandı! Push etmeye hazırsınız."
exit 0
