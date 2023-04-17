## Kriteria Submission

Fitur yang harus ada pada aplikasi.

1.  **Halaman Autentikasi**

    Syarat yang harus dipenuhi sebagai berikut.

    - Menampilkan halaman **login** untuk masuk ke dalam aplikasi. Berikut input yang dibutuhkan.

      - Email (`R.id.ed_login_email`)
      - Password (`R.id.ed_login_password`)

    - Membuat halaman **register** untuk mendaftarkan diri dalam aplikasi. Berikut input yang dibutuhkan.

      - Nama (`R.id.ed_register_name`)
      - Email (`R.id.ed_register_email`)
      - Password (`R.id.ed_register_password`)

    - Password wajib disembunyikan.
    - Membuat **Custom View** berupa EditText **pada halaman login atau register** dengan ketentuan sebagai berikut.

      - Jika jumlah password kurang dari 8 karakter, menampilkan pesan error secara langsung pada EditText tanpa harus pindah form atau klik tombol dulu.

    - Menyimpan data sesi dan token di **preferences**. Data sesi digunakan untuk mengatur alur aplikasi dengan spesifikasi seperti berikut.

      - Jika sudah login langsung masuk ke halaman utama.
      - Jika belum maka akan masuk ke halaman login.

    - Terdapat fitur untuk **logout** (`R.id.action_logout`) pada halaman utama dengan ketentuan sebagai berikut.

      - Ketika tombol logout ditekan, informasi token, dan sesi harus dihapus.

<br />

2.  **Daftar Cerita (List Story)**

    Syarat yang harus dipenuhi sebagai berikut.

    - Menampilkan **daftar cerita** dari API yang disediakan. Berikut minimal informasi yang wajib Anda tampilkan.

      - Nama user (`R.id.tv_item_name`)
      - Foto  (`R.id.iv_item_photo`)

    - Muncul **halaman detail** ketika salah satu item cerita ditekan. Berikut  minimal informasi yang wajib Anda tampilkan.

      - Nama user (`R.id.tv_detail_name`)
      - Foto (`R.id.iv_detail_photo`)
      - Deskripsi (`R.id.tv_detail_description`)

<br />

3.  **Tambah Cerita**

    Syarat yang harus dipenuhi sebagai berikut.

    - Membuat halaman untuk menambah cerita baru yang dapat diakses dari halaman daftar cerita. Berikut input minimal yang dibutuhkan.

      - File foto (wajib bisa dari kamera)
      - Deskripsi cerita (`R.id.ed_add_description`)

    - Berikut adalah ketentuan dalam menambahkan cerita baru:

      - Terdapat tombol (`R.id.button_add`) untuk upload data ke server.
      - Setelah tombol tersebut diklik dan proses upload berhasil, maka akan kembali ke halaman daftar cerita.
      - Data cerita terbaru harus muncul di paling atas.

<br />

4.  **Menampilkan Animasi**

    Syarat yang harus dipenuhi sebagai berikut.

    - Membuat animasi pada aplikasi dengan menggunakan salah satu jenis animasi berikut.

      - Property Animation
      - Motion Animation
      - Shared Element

    - Tuliskan jenis dan lokasi animasi pada Student Note.

Berikut kerangka tampilan yang bisa Anda gunakan sebagai referensi.

![Layout](https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/academy/dos:9d2844c423f0e390adcf4cedf72d30db20220221083537.jpeg)

## Saran Submission

Submission Anda akan dinilai oleh reviewer dengan **skala 1-5** berdasarkan dari parameter yang ada.

Anda dapat menerapkan beberapa **saran** di bawah ini untuk mendapatkan nilai tinggi.

- Menuliskan kode dengan bersih.
  - Bersihkan comment dan kode yang tidak digunakan.
  - Indentasi yang sesuai.
  - Menghapus import yang tidak digunakan.
- Membuat Custom View berupa EditText dengan ketentuan sebagai berikut.
  - Jika format email salah, menampilkan error langsung pada EditText.
- Menambahkan opsi mengambil gambar dari Galeri pada saat menambah cerita.
- Mengimplemantasikan alur aplikasi yang tepat:
  - Setelah login, ketika ditekan back pada halaman home, tidak kembali ke halaman login tetapi keluar.
  - Setelah upload, ketika ditekan back pada halaman home, tidak kembali ke halaman upload tetapi keluar.
  - Setelah logout, ketika ditekan back, tidak kembali ke dalam aplikasi tetapi keluar.
- Membuat stack widget untuk menampilkan daftar cerita.
- Terdapat pengaturan untuk localization (multi bahasa).
- Menambahkan informasi selama proses interaksi dengan API:
  - Loading ketika memuat data
  - Informasi error ketika gagal
  - Pesan informasi ketika tidak ada data
- Menerapkan Android Architecture Component (minimal ViewModel dan LiveData) dengan benar di semua halaman yang mengandung business logic.

Detail penilaian submission.

- **Bintang 1** : Semua ketentuan terpenuhi, namun penulisan kode masih perlu banyak diperbaiki atau terindikasi melakukan plagiat.
- **Bintang 2** : Semua ketentuan terpenuhi, namun penulisan kode masih perlu diperbaiki.
- **Bintang 3** : Semua ketentuan terpenuhi namun hanya mengikuti seperti apa yang ada pada modul.
- **Bintang 4** : Semua ketentuan terpenuhi dan menerapkan minimal **empat (4) saran** yang ada di atas.
- **Bintang 5** : Semua ketentuan terpenuhi dan menerapkan **tujuh (7)** **saran** yang ada di atas.

> **Catatan:**
>
> Jika submission Anda ditolak, maka tidak ada penilaian. Kriteria penilaian bintang di atas hanya berlaku jika submission Anda lulus.

## Submission yang Tidak Sesuai Kriteria

Submission akan ditolak jika tidak sesuai kriteria. Berikut poin-poin yang harus diperhatikan.

- Aplikasi harus memenuhi segala kriteria yang diberikan.
- Proses register dan login gagal.
- Custom EditText tidak bekerja sesuai dengan spesifikasi.
- Sudah ada Custom View tetapi tidak ada validasi jumlah karakter. Pemeriksaan justru di Activity.
- Daftar dan detail cerita tidak tampil dengan sempurna.
- Fitur tambah cerita tidak berjalan dengan lancar.
- Tidak dapat mengambil foto dari kamera.
- Data terbaru tidak langsung muncul setelah menambahkan cerita.
- Informasi yang ditampilkan pada halaman daftar cerita dengan detail cerita tidak relevan.
- Tombol logout tidak menghapus data sesi dan token.
- Menggunakan jenis animasi di luar ketentuan, seperti ActivityTransition.
- Project tidak bisa di-build.
- Aplikasi force closed.
- Mengirimkan file selain proyek Android Studio.
- Mengirimkan proyek yang bukan karya sendiri.

## Resources

- Story API adalah API untuk berbagi cerita seputar Dicoding. Mirip seperti post Instagram, tetapi spesial untuk Dicoding. Dokumentasi API dapat dilihat di:
  - [https://story-api.dicoding.dev/v1/](https://story-api.dicoding.dev/v1/)
- Simpan token yang didapatkan ketika login karena dibutuhkan untuk melakukan request endpoint lain.
- Supaya tetap bersih, data yang dikirimkan akan otomatis hilang setelah satu jam.
- Pastikan tidak menggunakan endpoint guest karena endpoint tersebut ditujukan untuk latihan saja.
