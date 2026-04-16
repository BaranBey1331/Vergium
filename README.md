# Vergium

Vergium, Minecraft 1.20.1 Forge için geliştirilmiş, yüksek performanslı ve sızıntısız (leak-free) bir optimizasyon modudur. Özellikle **Samsung Xclipse 940 (AMD RDNA 3)** mimarisi ve **Vulkan (ANGLE)** sürücüleri için baştan sona tasarlanmış profesyonel bir rendering pipeline'ı sunar.

## 🌟 v1.3.0 Major Özellikleri

- **Vulkan Fast-Path:** Xclipse 940'ın donanımsal yeteneklerini kullanarak ANGLE katmanındaki darboğazları en aza indiren özel çizim hattı.
- **Zero-Allocation Memory:** `BufferPool` mimarisi ile her frame'de yeni bellek alanı açmak yerine mevcut alanları geri dönüştürür. Bu, Java GC kaynaklı takılmaları (stutter) ve bellek sızıntılarını (leak) tamamen ortadan kaldırır.
- **Universal Mod-Bridge:** Ağır mod paketlerinde (Twilight Forest, Alex's Mobs vb.) ortaya çıkan dağınık render isteklerini merkezi bir sistemde toplayıp optimize eder.
- **Robust Resource Management:** Merkezi `ResourceManager` sayesinde oyunun kapanışı veya dünya geçişleri sırasında tüm donanım kaynakları %100 temizlenir.

## 🛠 Teknik Mimari

Vergium, Minecraft'ın standart render döngüsünü modern Vulkan prensiplerine dönüştürür:
1. **Batching:** Binlerce çizim komutu yerine tek bir "Indirect" çizim.
2. **State Sorting:** Shader ve texture değişimlerini minimize eden akıllı sıralama.
3. **Occlusion Culling:** Görünmeyen her şeyi (blok, mob, parçacık) GPU'ya ulaşmadan eleme.

## 🚀 Kurulum ve Uyumluluk

- **Forge:** 1.20.1 (Tüm sürümlerle uyumlu).
- **GPU:** Samsung Xclipse 940 önerilir (Tüm GLES 3.2 cihazları destekler).
- **Launcher:** PojavLauncher veya Zalith Launcher üzerinde **ANGLE (Vulkan)** modu seçilmelidir.

## 📝 Lisans
MIT
