# 🎨 Sistem Desain: Vintage Editorial & Tipografi Cetak

Dokumen ini menguraikan fondasi estetika, palet warna, tipografi, dan arsitektur komponen Jetpack Compose yang memberi jiwa klasik pada **Tinta**.

---

## 1. Filosofi Estetika: Melawan "Cold Developer UI"

Kebanyakan aplikasi pembaca kode atau Markdown mengadopsi tema gelap berkontras tajam (*pure black* `#000000` dengan teks putih murni `#FFFFFF`). Meskipun disukai di editor kode saat mengetik, kontras setinggi ini sangat melelahkan mata (*eye fatigue*) saat digunakan membaca artikel panjang atau dokumen PRD lebih dari 15 menit.

**Tinta** mengadaptasi prinsip keterbacaan koran cetak fisik (*physical editorial print*):
* **Kertas Hangat (Warm Paper)**: Latar belakang tidak berwarna putih silau, melainkan warna krem kertas koran pagi.
* **Tinta Arang Lembut (Soft Charcoal Ink)**: Teks tidak berwarna hitam pekat, melainkan warna abu-abu arang tinta cetak yang lembut.
* **Tipografi Berbobot (Weighted Serif)**: Judul menggunakan font serif bertipe *transitional* atau *didone* yang memberikan wibawa editorial seperti lembaran *The New York Times* atau *The Daily Telegraph*.

---

## 2. Token Warna & Palet Kertas (Color Tokens)

File lokasi: [Color.kt](file:///d:/TelyuTask/01_Akademik_Semester/semester_5/Tinta/app/src/main/java/com/zeeonworks/tinta/ui/theme/Color.kt)

```
┌────────────────────────────────────────────────────────┐
│                   PALET RESMI TINTA                    │
├────────────────────┬────────────┬──────────────────────┤
│ Token              │ HEX        │ Peran Desain         │
├────────────────────┼────────────┼──────────────────────┤
│ PaperParchment     │ #F7F4EB    │ Kanvas dasar kertas  │
│ InkBlack           │ #1F1D1A    │ Teks judul & tajuk   │
│ CharcoalEditorial  │ #383531    │ Teks isi (body text) │
│ VintageSepia       │ #786C5E    │ Metadata & tanggal   │
│ AgedCream          │ #EDE8DC    │ Kartu & blok kode    │
│ PressGold          │ #B8860B    │ Aksen tombol utama   │
└────────────────────┴────────────┴──────────────────────┘
```

---

## 3. Skala Tipografi (Typography Scale)

File lokasi: [Type.kt](file:///d:/TelyuTask/01_Akademik_Semester/semester_5/Tinta/app/src/main/java/com/zeeonworks/tinta/ui/theme/Type.kt)

* **Display & Headings (H1 – H6)**:
  * Menggunakan tipografi Serif dengan *line-height* yang proporsional agar judul panjang tidak saling tumpang tindih.
* **Body Text**:
  * Ukuran 16sp dengan rasio keterbacaan 1.5x *line height* untuk kenyamanan mata pembaca.
* **Code & Monospace**:
  * Menggunakan gaya font mesin tik kuno (*Vintage Typewriter*) dengan latar belakang kartu sedikit lebih gelap dari kertas kanvas (`AgedCream`) dan bingkai tipis bertekstur.

---

## 4. Anatomi Komponen Khas

### A. Garis Pemisah Editorial (`EditorialDivider.kt`)
Bukan sekadar garis horizontal tipis biasa (`HorizontalDivider`), melainkan ornamen pemisah ganda khas koran fisik cetak (garis tebal di atas, garis tipis di bawah dengan ornamen titik di tengah).

### B. Blok Kode Mesin Tik (`VintageCodeBlock.kt`)
* Dilengkapi tombol salin satu sentuhan (*one-tap copy button*).
* Indentasi dan spasi dipertahankan secara akurat menggunakan teks *pre-formatted* tanpa auto-wrap yang merusak struktur kode.

### C. Kartu Kliping Koran (`ArchiveCard.kt`)
* Menampilkan jumlah kata (*word count*) dan perkiraan waktu baca.
* Dilengkapi kutipan ringkasan (*snippet*) yang telah dibersihkan dari simbol markdown murni (`#`, `*`, `>`).
