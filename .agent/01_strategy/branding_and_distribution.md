# 📢 Panduan Promosi, Distribusi & Komunitas Open-Source

Bagaimana cara membuat proyek personal yang awalnya sepi menjadi ramai dikunjungi, digunakan oleh ribuan orang, dan mendapatkan banyak kontributor? Dokumen ini merangkum taktik distribusi organik terbaik untuk Tinta.

---

## 1. Distribusi ke Ekosistem F-Droid & IzzyOnDroid

Komunitas Android yang paling loyal terhadap aplikasi open-source dan bebas pelacak berada di **F-Droid**.

```
┌────────────────────────────────────────────────────────┐
│                   JALUR DISTRIBUSI                     │
├─────────────────────────┬──────────────────────────────┤
│    Langkah 1 (Cepat)    │      Langkah 2 (Resmi)       │
│      IzzyOnDroid        │         F-Droid Main         │
├─────────────────────────┼──────────────────────────────┤
│ - Review cepat (1-3 hari│ - Review mendalam komunitas  │
│ - Otomatis ambil APK    │ - Di-compile dari source     │
│   dari GitHub Releases  │   oleh server F-Droid        │
│ - Sangat ramah indie    │ - Reputasi tertinggi         │
└─────────────────────────┴──────────────────────────────┘
```

### Syarat Masuk IzzyOnDroid / F-Droid:
1. **100% Bebas Proprietary Bloat**: Tidak menggunakan Google Play Services tertutup atau Firebase Analytics berbayar. (*Tinta sudah memenuhi syarat ini karena 100% offline!*).
2. **Lisensi Open-Source Jelas**: Memiliki file `LICENSE` (Apache 2.0 / GPL).
3. **Ada Release Tag & APK**: Memiliki tag rilis seperti `v1.0.0` beserta APK yang dapat diunduh di GitHub Releases.

### Cara Mendaftarkan Tinta ke IzzyOnDroid:
1. Buka repositori: [IzzyOnDroid Repo Submission](https://gitlab.com/IzzyOnDroid/repo/-/issues).
2. Klik tombol **New Issue** dan pilih template **App Inclusion Request**.
3. Masukkan URL repo GitHub: `https://github.com/mzhafraan/Tinta-md-reader`.
4. Setelah disetujui, setiap kali Anda membuat rilis baru di GitHub, pengguna katalog IzzyOnDroid di seluruh dunia akan otomatis menerima pembaruan aplikasi!

---

## 2. Strategi Peluncuran di Media Sosial & Komunitas

Jangan pernah memposting dengan gaya iklan kaku (*"Download my app now!"*). Komunitas teknologi menyukai **cerita di balik layar (*behind the scenes*)**, motivasi desain, dan solusi atas masalah nyata.

### A. Reddit Pitch Framework
Subreddit terbaik untuk Tinta:
* `r/androiddev` (Fokus pada aspek teknis: Jetpack Compose, CommonMark, SAF persistence).
* `r/opensource` (Fokus pada etos 100% offline, zero network permissions, Apache 2.0).
* `r/SideProject` (Fokus pada visual kertas vintage dan estetika surat kabar klasik).

**Contoh Judul Postingan yang Menarik:**
> *"I got tired of high-contrast dark mode when reading long AI reports on my phone, so I built Tinta: an offline Android Markdown reader with a warm vintage newspaper aesthetic."*

**Poin-Poin Isi Postingan:**
1. **Latar Belakang Singkat**: Ceritakan alasan Anda membuatnya (membaca PRD/artefak AI di malam hari bikin mata lelah).
2. **Pendekatan Desain**: Tampilkan 2-3 gambar UI (The Archives dan The Gazette).
3. **Privasi Absolut**: Jelaskan bahwa aplikasi tidak memiliki izin internet sama sekali.
4. **Tautan GitHub**: Ajak orang untuk mencoba file APK rilis dan memberikan masukan.

### B. Show HN (Hacker News)
Hacker News adalah tempat berkumpulnya para insinyur perangkat lunak senior:
* **Format Judul**: `Show HN: Tinta – An offline Android markdown reader with a vintage newspaper look`
* **Komentar Pertama**: Tuliskan ringkasan stack teknis (Kotlin, Compose, CommonMark Java, SAF DataStore) dan buka diskusi seputar *offline-first Android development*.

---

## 3. Rahasia Visual Repositori (GitHub Aesthetics)

Orang menilai repositori dari 5 detik pertama mereka melihat layar:
1. **Banner & Mockup**: Menggunakan format PNG berkualitas tinggi dengan sudut melengkung (*rounded corners*) dan bayangan lembut (*soft drop shadow*).
2. **Badge Berwarna Senada**: Shield badges yang serasi dengan palet tema (warna hijau tua, kuning sepia, dan abu-abu arang).
3. **GIF Demo Singkat**: Rekam layar navigasi membuka dokumen berdurasi 5-8 detik, lalu kompres ke format GIF atau WebP untuk dipasang di `README.md`.
