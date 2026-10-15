# 🔐 Anatomi Penandatanganan APK & Distribusi Android

Dokumen ini menjelaskan secara mendalam konsep teknis penandatanganan aplikasi (*Application Signing*), perbedaan varian APK, dan cara kerja distribusi aplikasi Android di luar Google Play Store.

---

## 1. Mengapa APK Wajib Di-Sign (Ditandatangani)?

Sistem operasi Android menerapkan aturan keamanan ketat: **Tidak ada file APK yang dapat dipasang (*install*) di perangkat fisik tanpa tanda tangan kriptografi digital.**

```
┌────────────────────────────────────────────────────────┐
│                   FUNGSI TANDA TANGAN                  │
├────────────────────────────┬───────────────────────────┤
│    1. Verifikasi Integritas│ Memastikan file APK tidak │
│       (Integrity)          │ dimodifikasi / disusupi   │
│                            │ virus setelah di-compile. │
├────────────────────────────┼───────────────────────────┤
│    2. Identitas Pengembang │ Memastikan update aplikasi│
│       (Identity & Updates) │ di masa depan berasal dari│
│                            │ pembuat asli yang sama.   │
└────────────────────────────┴───────────────────────────┘
```

Jika Anda mencoba memasang APK yang *unsigned* (tanpa tanda tangan), Android akan langsung menampilkan pesan kegagalan: *"App not installed"* atau *"There was a problem parsing the package."*

---

## 2. Perbedaan Varian: Debug APK vs Release APK

| Karakteristik | Debug APK (`app-debug.apk`) | Release APK (`app-release.apk`) |
| :--- | :--- | :--- |
| **Tujuan** | Pengembangan & pengujian internal. | Distribusi publik ke pengguna akhir. |
| **Kecepatan & Performa** | Sedikit lebih berat (fitur debugging & inspeksi Compose aktif). | Maksimal (kode dioptimasi, logging debug dimatikan). |
| **Penandatanganan** | Otomatis di-sign oleh Android Studio menggunakan `debug.keystore` bawaan. | Memerlukan kunci sertifikat rilis (*Release Keystore*). |
| **Kemudahan Install** | Siap pasang langsung di HP apa pun tanpa konfigurasi tambahan. | Harus di-sign sebelum bisa dipasang. |

---

## 3. Cara Kerja Keystore & Sertifikat Digital

Keystore adalah berkas terenkripsi berformat `.jks` atau `.keystore` yang menyimpan pasangan kunci kriptografi (kunci publik dan kunci privat).

### Perintah Pembuatan Keystore Mandiri:
Jika suatu saat Anda ingin membuat sertifikat rilis permanen di laptop Anda:
```bash
keytool -genkey -v \
  -keystore tinta-release.keystore \
  -alias tinta-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass RahasiaKeystore123 \
  -keypass RahasiaKeystore123 \
  -dname "CN=Tinta, O=Zeeonworks, C=ID"
```

### Parameter Penting:
* `-keyalg RSA -keysize 2048`: Standar enkripsi asimetris yang aman.
* `-validity 10000`: Masa berlaku sertifikat selama 10.000 hari (~27 tahun). Google Play dan Android mewajibkan sertifikat berlaku minimal hingga tahun 2050 agar pengguna bisa terus menerima update aplikasi.

---

## 4. Opsi Penyimpanan Rahasia di GitHub Secrets (Opsional Masa Depan)

Jika Anda ingin tanda tangan rilis di GitHub Actions menggunakan kunci permanen pribadi Anda:

1. Konversi file `tinta-release.keystore` menjadi teks base64:
   ```powershell
   [Convert]::ToBase64String([IO.File]::ReadAllBytes("tinta-release.keystore")) | Set-Clipboard
   ```
2. Buka repo GitHub Anda: **Settings** > **Secrets and variables** > **Actions** > **New repository secret**.
3. Beri nama `RELEASE_KEYSTORE_BASE64` dan simpan teks tadi.
4. Di file GitHub Actions, kunci rahasia ini dapat didekodekan kembali menjadi file tanpa pernah terlihat oleh publik.

> *Catatan:* Untuk saat ini, skrip `release.yml` yang telah kita siapkan sudah otomatis menangani pembuatan dan penandatanganan APK di cloud, sehingga Anda tidak perlu pusing memikirkan setup manual!
