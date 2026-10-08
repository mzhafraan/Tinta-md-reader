# Product Requirement Document (PRD)

## Tinta — Vintage Editorial Markdown Reader

### Informasi Dokumen

* **Nama Proyek:** Tinta

* **Versi:** 1.0.0 (MVP)

* **Status:** Disetujui untuk Pengembangan (Ready for Dev)

* **Target Rilis:** Android (Minimum SDK 26 / Android 8.0, Target SDK 34+)

* **Pola Arsitektur:** Clean Architecture + MVVM (Model-View-ViewModel)

* **Konektivitas:** 100% Offline (Zero Network Permissions)

## 1. Ringkasan Eksekutif & Visi Produk

### 1.1 Visi

Tinta adalah aplikasi penampil berkas Markdown (`.md`) lokal berbasis Android yang memadukan keandalan teknis pembaca dokumen dengan estetika surat kabar klasik (*vintage newspaper/editorial print*).

### 1.2 Latar Belakang

Mayoritas penampil Markdown di ekosistem mobile saat ini mengadopsi antarmuka serba monokrom, minimalis datar, atau tema gelap berkontras tajam (*cold developer aesthetic*). Pendekatan ini sering kali menyebabkan kelelahan visual (*eye strain*) saat digunakan membaca dokumen berformat panjang, seperti dokumentasi teknis, catatan arsitektur, dan laporan hasil keluaran agen kecerdasan buatan (*AI agent artifacts*). Tinta menghadirkan antarmuka alternatif bernuansa kertas koran hangat (*warm parchment*) yang nyaman di mata dan memberikan pengalaman membaca arsip cetak fisik di era digital.

### 1.3 Nilai Inti Produk

* **Distraction-Free Editorial Experience:** Tipografi serif elegan dan palet warna kertas koran yang dirancang khusus untuk kenyamanan membaca jangka panjang.

* **Absolute Privacy & Zero Trust:** Beroperasi sepenuhnya luring tanpa memerlukan akun, tanpa analitik daring, dan tanpa izin jaringan (`android.permission.INTERNET`).

* **Frictionless Local Access:** Memanfaatkan Storage Access Framework Android dengan persistensi URI, memungkinkan dokumen dibuka ulang tanpa proses navigasi berkas yang repetitif.

## 2. Profil Pengguna & Masalah

### 2.1 Target Pengguna

1. **Pengembang Perangkat Lunak & AI Prompt Engineers:** Kerap membaca dokumen PRD, ringkasan kode, diagram alur, dan laporan artefak `.md` dari laptop ke perangkat seluler.

2. **Penulis Teknis & Akademisi:** Membutuhkan wadah membaca draf tulisan atau catatan riset lokal tanpa gangguan notifikasi atau sinkronisasi komputasi awan.

3. **Pembaca Minimalis:** Pengguna yang memprioritaskan aplikasi ringan, cepat, hemat baterai, dan menghormati privasi data lokal.

### 2.2 Pernyataan Masalah (Problem Statements)

* *P1 (Eye Fatigue):* Kontras hitam-putih murni pada pembaca teks biasa membuat mata cepat lelah saat membaca draf dokumen teknis lebih dari 15 menit.

* *P2 (File Picker Overhead):* Sistem Android bawaan sering kali meminta pemilihan berkas ulang setiap kali aplikasi ditutup atau dimulai kembali jika hak akses berkas tidak disimpan secara persisten.

* *P3 (Code Readability):* Pembaca dokumen non-teknis kerap merusak indentasi dan tipografi blok kode program, sementara pembaca teknis minim sentuhan visual estetik.

## 3. Batasan Produk (Scope of Work)

| Cakupan Termasuk (In-Scope — MVP) | Batasan Tidak Termasuk (Out-of-Scope — Post-MVP) | 
| ----- | ----- | 
| Pemilihan berkas `.md` & `.txt` via Storage Access Framework (SAF) | Penyuntingan teks langsung (*Rich text editor* / WYSIWYG) | 
| Retensi hak akses berkas via `takePersistableUriPermission` | Sinkronisasi awan otomatis (Google Drive, Dropbox, GitHub) | 
| Dasbor "The Archives" (kisi kartu riwayat dokumen lokal) | Pembuatan berkas Markdown baru dari dalam aplikasi | 
| Layar baca "The Gazette" dengan tipografi surat kabar klasik | Rendering mesin diagram dinamis (*Mermaid.js runtime engine*) | 
| Kotak blok kode monospace dengan tombol salin instan (*one-tap copy*) | Fitur ekspor berkas ke format PDF/EPUB | 
| Format Markdown standar: Heading 1–6, list, quote, divider, bold, italic | Sintaks penulisan matematika lanjutan ($LaTeX$ formula parser) | 

## 4. Kebutuhan Fungsional (Functional Requirements)

### 4.1 Modul: Akses Berkas & Manajemen Dokumen (File & Storage)

* **FR-01: Pemilihan Berkas Sistem**

  * *Deskripsi:* Pengguna dapat memicu penjelajah berkas bawaan sistem Android untuk memilih dokumen bertipe Markdown atau teks murni.

  * *Spesifikasi Teknis:* Menggunakan `ActivityResultContracts.OpenDocument()` dengan MIME types `text/markdown`, `text/plain`, dan `*/*`.

  * *Kriteria Penerimaan:* Sistem hanya menampilkan berkas yang valid dan berhasil mengembalikan `Uri` dokumen ke aplikasi tanpa meminta izin penyimpanan luas (`MANAGE_EXTERNAL_STORAGE`).

* **FR-02: Persistensi Izin Akses Berkas**

  * *Deskripsi:* Berkas yang telah dipilih oleh pengguna harus tetap dapat diakses pada peluncuran aplikasi berikutnya.

  * *Spesifikasi Teknis:* Memanggil `context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)` segera setelah `Uri` diterima.

  * *Kriteria Penerimaan:* Dokumen di daftar riwayat dapat dibuka langsung tanpa memicu dialog pemilihan berkas ulang dari sistem.

* **FR-03: Penyimpanan Metadata Kliping (Recent Archives)**

  * *Deskripsi:* Aplikasi menyimpan riwayat metadata dokumen yang pernah dibuka ke penyimpanan preferensi lokal.

  * *Atribut Tersimpan:* `uriString`, `title`, `lastOpenedTimestamp`, `approximateWordCount`, `previewSnippet`.

  * *Spesifikasi Teknis:* Disimpan via Jetpack DataStore Preferences dalam bentuk serialized JSON string list.

  * *Kriteria Penerimaan:* Riwayat tersimpan secara instan saat dokumen dibuka dan langsung diperbarui di urutan teratas.

* **FR-04: Penghapusan Entri Arsip**

  * *Deskripsi:* Pengguna dapat menghapus dokumen tertentu dari daftar riwayat kliping tanpa menghapus berkas fisik asli di penyimpanan perangkat.

  * *Kriteria Penerimaan:* Terdapat opsi atau ikon hapus pada setiap kartu arsip; saat dihapus, data preferensi diperbarui seketika.

### 4.2 Modul: Dasbor Riwayat ("The Archives")

* **FR-05: Tampilan Kartu Arsip (Clipping Grid)**

  * *Deskripsi:* Menyajikan daftar dokumen yang pernah dibuka dalam tata letak kisi 2 kolom bergaya kliping koran.

  * *Elemen Kartu:*

    * Judul berkas (tanpa ekstensi `.md`).

    * Tanggal stempel terakhir dibaca (format: `MMM dd, yyyy` bergaya cetak).

    * Estimasi jumlah kata / karakter.

    * Cuplikan teks pembuka (1–2 baris teks pertama).

  * *Kriteria Penerimaan:* Tampilan kartu memiliki garis tepi tipis (*hairline border*) dan efek elevasi minimalis menyerupai potongan kertas.

* **FR-06: Tombol Impor Berkas ("Add Clipping")**

  * *Deskripsi:* Kartu khusus atau Floating Action Button (FAB) berlabel "Add Clipping" untuk memicu `OpenDocument()`.

  * *Kriteria Penerimaan:* Menempati posisi yang mudah dijangkau satu tangan dan memicu alur pemilihan berkas dengan respons instan.

* **FR-07: Status Kosong (Empty State Experience)**

  * *Deskripsi:* Menampilkan grafis dan pesan editorial puitis saat pengguna pertama kali membuka aplikasi atau belum memiliki berkas tersimpan.

  * *Teks Acuan:* *"The Archives are presently empty. Procure a dispatch to begin reading."*

### 4.3 Modul: Pembaca Dokumen ("The Gazette Reader")

* **FR-08: Parsing & Rendering Markdown**

  * *Deskripsi:* Mengonversi sintaks Markdown mentah menjadi antarmuka visual Compose yang tertata rapi.

  * *Dukungan Format:*

    * Headings: H1 (Headline Utama), H2 (Sub-judul), H3–H6 (Bagian Dokumen).

    * Paragraf: Perataan kiri alami dengan jarak baris longgar (*loose line-height*).

    * Penekanan Teks: Huruf tebal (*bold*), miring (*italic*), dan coret (*strikethrough*).

    * Daftar: Unordered list (bullet klasik) dan Ordered list (nomor desimal).

    * Kutipan (*Blockquotes*): Kotak berlatar kertas buram dengan garis penanda tebal di tepi kiri.

    * Pembatas Bagian: Garis horizontal putus-putus atau solid tipis bergaya editorial.

* **FR-09: Blok Kode Bergaya Mesin Tik (Typewriter Code Blocks)**

  * *Deskripsi:* Menampilkan kode program dalam kontainer bertekstur stempel cetak dengan tipografi monospace.

  * *Aksi:* Tombol ikon "Copy" di pojok kanan atas blok kode.

  * *Kriteria Penerimaan:* Menekan tombol salin akan menyimpan seluruh isi kode ke dalam clipboard Android dan memunculkan toast/snackbar konfirmasi.

* **FR-10: Bilah Atas Editorial & Aksi Berbagi**

  * *Deskripsi:* Top App Bar menyajikan tombol kembali, judul dokumen yang terpotong rapi dengan elipsis jika terlalu panjang, serta tombol berbagi (*Share Dispatch*).

  * *Kriteria Penerimaan:* Tombol *Share* memicu `Intent.ACTION_SEND` untuk membagikan isi teks dokumen ke aplikasi lain.

## 5. Spesifikasi Desain & Antarmuka (UI/UX)

### 5.1 Filosofi Visual

Antarmuka Tinta mengambil inspirasi dari tata letak surat kabar terbitan awal abad ke-20: penggunaan kontras tinta di atas kertas bubur kayu (*pulp paper*), garis pemisah tipis (*editorial column dividers*), dan hierarki tipografi yang berwibawa.

### 5.2 Token Palet Warna (Color Palette)

```kotlin
// Token Warna Tema Tinta (Vintage Editorial)
val TintaParchmentBackground = Color(0xFFF5EFE0) // Latar belakang utama layar
val TintaPaperCard           = Color(0xFFFAF6EE) // Latar kartu arsip & blockquote
val TintaCharcoalInk         = Color(0xFF24211E) // Teks utama / Headline cetak
val TintaMutedInk            = Color(0xFF6B6257) // Teks sekunder / Stempel tanggal
val TintaTerracotta          = Color(0xFF8A3324) // Aksen utama / FAB / Penanda aktif
val TintaBorderHairline      = Color(0xFFC8BCAB) // Garis pemisah 1dp / Garis tepi kartu
val TintaCodeBackground      = Color(0xFFEDE6D4) // Latar belakang blok kode
```

### 5.3 Sistem Tipografi (Typography Hierarchy)

| Skala Tipe | Nama Font Acuan | Bobot (Weight) | Ukuran (Size) | Kegunaan dalam Aplikasi | 
| ----- | ----- | ----- | ----- | ----- | 
| `displayLarge` | Playfair Display | Bold | 28.sp | Header Utama "THE ARCHIVES" / "THE GAZETTE" | 
| `headlineMedium` | Playfair Display | SemiBold | 22.sp | H1 pada dokumen Markdown | 
| `titleMedium` | Playfair Display | Medium | 18.sp | H2 & Judul pada Kartu Kliping Arsip | 
| `bodyLarge` | Merriweather | Regular | 16.sp (LH: 26.sp) | Teks Paragraf Dokumen Utama | 
| `bodyMedium` | Merriweather | Regular | 14.sp (LH: 22.sp) | Ringkasan teks kartu arsip | 
| `labelSmall` | Courier Prime | Bold | 11.sp | Stempel tanggal, word count, dan label badge | 
| `bodySmall` (Code) | Courier Prime | Regular | 13.sp | Blok kode dan kode segaris (*inline code*) | 

## 6. Kebutuhan Non-Fungsional (Non-Functional Requirements)

### 6.1 Kinerja (Performance)

* **Waktu Muat (Cold Start):** Aplikasi terbuka hingga layar arsip siap berinteraksi dalam waktu $\le 1.0$ detik pada perangkat referensi kelas menengah.

* **Kecepatan Rendering Dokumen:** Berkas Markdown berukuran hingga 50.000 kata harus berhasil di-parse dan dirender dalam waktu kurang dari 750 milidetik.

* **Kelancaran Scroll:** Antarmuka daftar arsip dan pembaca dokumen wajib mempertahankan frame-rate konstan 60/120 fps tanpa *stuttering* atau pembekuan pada UI thread.

### 6.2 Keamanan & Privasi (Security & Privacy)

* **Nol Izin Jaringan:** Berkas `AndroidManifest.xml` tidak memuat izin `android.permission.INTERNET`.

* **Sandboxed Storage:** Tidak ada pembacaan data di luar berkas spesifik yang diberikan hak aksesnya oleh pengguna secara sadar lewat Storage Access Framework.

### 6.3 Kompatibilitas & Ketahanan (Reliability & Edge Cases)

* **Berkas yang Dipindahkan/Dihapus:** Apabila berkas asal telah dihapus dari memori HP oleh pengguna dari luar aplikasi, Tinta tidak boleh *crash*. Aplikasi harus menampilkan dialog kesalahan ramah pengguna dan menawarkan opsi untuk membersihkan entri dari riwayat.

* **Format Berkas Ilegal:** Berkas biner non-teks yang diimpor secara paksa ditolak secara aman dengan pesan: *"Unable to decode textual contents of this dispatch."*

## 7. Arsitektur Teknis & Struktur Data

### 7.1 Struktur Direktori Proyek

```
com.tinta.reader/
├── data/
│   ├── model/
│   │   └── DocumentArchive.kt       // Data class model metadata arsip
│   ├── repository/
│   │   └── DocumentRepository.kt    // Logika pembacaan InputStream & SAF
│   └── preferences/
│       └── ArchivePreferences.kt    // Serialisasi DataStore untuk riwayat
├── ui/
│   ├── archives/
│   │   ├── ArchivesScreen.kt        // Layar utama (kisi kliping berkas)
│   │   ├── ArchivesViewModel.kt     // State holder layar arsip
│   │   └── components/
│   │       ├── ArchiveCard.kt       // Komponen kartu bergaya koran
│   │       └── EmptyArchivesView.kt // Komponen status kosong
│   ├── reader/
│   │   ├── ReaderScreen.kt          // Layar pembaca dokumen Markdown
│   │   ├── ReaderViewModel.kt       // State holder pembacaan konten
│   │   └── components/
│   │       ├── VintageCodeBlock.kt  // Komponen blok kode mesin tik
│   │       └── EditorialDivider.kt  // Garis pemisah klasik
│   ├── navigation/
│   │   └── TintaNavGraph.kt         // Rute navigasi Compose (Archives -> Reader)
│   └── theme/
│       ├── Color.kt                 // Definisi palet warna koran
│       ├── Type.kt                  // Definisi FontFamily Playfair, Merriweather, Courier
│       └── Theme.kt                 // Setup MaterialTheme Material 3
└── MainActivity.kt                  // Single Activity host
```

### 7.2 Model Data (Kotlin)

```kotlin
package com.tinta.reader.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DocumentArchive(
    val uriString: String,
    val title: String,
    val lastOpenedEpoch: Long,
    val wordCount: Int,
    val snippet: String
)
```

### 7.3 Dependensi Gradle Inti (`app/build.gradle.kts`)

```kotlin
dependencies {
    // Jetpack Compose & Material 3
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    // DataStore Preferences & Kotlin Serialization
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Markdown Parsing Engine
    implementation("com.github.jeziellago:compose-markdown:0.5.6")
}
```

## 8. Rencana Rilis & Milestone

```
[Minggu 1: Fondasi & UI Kit]
├── Setup Project, Theme.kt, Custom Font Families (.ttf)
├── Implementasi DocumentRepository & SAF Persistable Uri
└── Setup DataStore Preferences untuk riwayat dokumen

[Minggu 2: Dasbor The Archives]
├── Implementasi ArchivesScreen dengan Grid 2 Kolom
├── Integrasi File Picker Contract (OpenDocument)
└── Pengujian persistensi berkas & penanganan empty state

[Minggu 3: The Gazette Reader]
├── Implementasi ReaderScreen & integrasi compose-markdown
├── Kustomisasi tipografi Markdown (Headings, Quotes, Lists)
└── Implementasi VintageCodeBlock dengan tombol Salin Cepat

[Minggu 4: Pengujian & Pemolesan]
├── Pengujian berkas besar (stress test > 50k words)
├── Penanganan skenario edge case (berkas hilang, revoked Uri)
└── Audit aksesibilitas (kontras warna teks & ukuran target sentuh)
```

## 9. Kriteria Keberhasilan (Definition of Done — MVP)

1. Aplikasi dapat terpasang (*clean install*) dan dijalankan pada emulator/perangkat fisik Android 8.0 hingga Android 14+.

2. Pengguna dapat memilih berkas Markdown dari manajer berkas perangkat tanpa kendala izin.

3. Berkas yang dipilih muncul di layar *The Archives* dan dapat dibuka kembali setelah aplikasi dihentikan paksa (*force closed*).

4. Layar pembaca menampilkan format teks dengan tipografi bertema koran vintage yang konsisten.

5. Tombol salin pada blok kode menyalin teks yang akurat ke clipboard sistem.

6. Manifest aplikasi bebas dari deklarasi izin `INTERNET`.