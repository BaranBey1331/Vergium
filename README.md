# Vergium

Vergium, Minecraft 1.20.1 Forge için geliştirilmiş bir optimizasyon modudur. Özellikle **Samsung Xclipse 940 (AMD RDNA 3)** GPU mimarisine sahip Android cihazlarda (PojavLauncher, Zalith Launcher) rendering darboğazlarını azaltmayı hedefler.

## 🛠 Teknik Odak Noktaları

- **Draw Call Azaltma:** ANGLE (Vulkan üzerinden OpenGL ES) katmanının CPU üzerindeki yükünü hafifletmek için çizim komutlarını gruplar (Batching).
- **Bellek Yönetimi:** Java Heap belleği dışındaki (Off-heap) doğrudan bellek erişimiyle GC (Garbage Collection) takılmalarını minimize eder.
- **Occlusion Culling:** Görünmeyen chunk'ları ve mob'ları donanım seviyesinde eleyerek GPU yükünü düşürür.
- **Vulkan Uyumluluğu:** UBO (Uniform Buffer Objects) kullanarak matriks güncellemelerini optimize eder.

## ⚠️ Sınırlamalar ve Gerçekçi Beklentiler

- Bu mod sihirli bir değnek değildir; düşük donanımlı cihazlarda mucizevi bir FPS artışı yerine daha stabil bir oyun deneyimi sunar.
- Bazı shader modları ile uyumluluk sorunları yaşayabilir (kendi shader motoruna müdahale ettiği için).
- Xclipse 940 sürücülerine özel optimizasyonlar içerdiğinden, diğer GPU'larda performans farkı değişkenlik gösterebilir.

## 🚀 Kurulum

1. Forge 1.20.1 kurulu olduğundan emin olun.
2. Modun `.jar` dosyasını `mods` klasörüne atın.
3. Pojav/Zalith Launcher üzerinde Renderer ayarının **ANGLE (Vulkan)** olduğundan emin olun.

## 📝 Lisans
MIT
