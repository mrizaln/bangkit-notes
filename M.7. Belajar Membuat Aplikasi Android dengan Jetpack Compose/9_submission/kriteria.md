# Kriteria

Fitur yang harus ada pada aplikasi

1. **Halaman List**

   - [x] Menampilkan data dalam format List dengan jumlah minimal 10 item yang berbeda.
   - [x] Gunakan LazyList/LazyGrid untuk menyusun datanya.
   - [x] Memunculkan halaman detail ketika salah satu item ditekan.

2. **Halaman Detail**

   - [x] Menampilkan gambar dan informasi yang relevan pada halaman detail.
   - [x] Informasi yang relevan mencakup kesamaan informasi yang ditampilkan pada halaman utama dengan halaman detail.
     - [x] Terdapat judul dan gambar yang sesuai dengan list
     - [x] Terdapat informasi tambahan yang tidak sama dengan list

3. **Halaman About**

   - [x] Menampilkan foto diri, nama, dan email yang terdaftar di Dicoding.
   - [x] Dalam mengakses halaman about, pastikan terdapat tombol yang bisa digunakan untuk mengakses halamannya.
   - [x] Untuk cara mengaksesnya, Anda bisa mengimplementasikan:
     - [x] Dengan menambahkan elemen View khusus (bisa option menu, tombol, atau tab) yang mengandung contentDescription "about_page"

# Kriteria Opsional

1. **Fitur Percarian**

   - [x] Ketentuan
     - [x] Jika kolom pencarian tidak kosong, maka aplikasi hanya menampilkan data yang judulnya mengandung kata kunci yang dimasukkan
     - [x] Jika kolom pencariannya kosong, maka aplikasi menampilkan seluruh data.
   - [x] Memanfaatkan ViewModel dalam membangun fitur pencarian.

2. **Fitur Menambah & Menghapus data**

   - [x] Aplikasi memiliki fitur untuk menambah dan menghapus data.
   - [x] Berikut beberapa skenario yang bisa diimplementasikan:
     - [ ] Menambah & menghapus data utama
     - [x] Menambah & menghapus data favorit
     - [ ] Menambah & menghapus data keranjang
   - [x] Teknik penyimpanan cukup menggunakan fake List. Jika ingin menggunakan database asli atau API pun tidak masalah.
   - [x] Jika data kosong, menampilkan informasi bahwa data kosong.

# Saran

- [x] Menerapkan tampilan aplikasi yang sesuai standar:

  - [x] Memiliki width, height, dan padding yang sesuai.
  - [x] Komponen tidak saling bertumpuk.
  - [x] Penggunaan komponen sesuai dengan fungsinya.
  - [x] Penggunaan warna yang sesuai.

- [x] Menuliskan kode dengan baik sesuai best-practice:
  - [x] Tidak membuat komponen yang tidak diperlukan
  - [x] Memecah UI menjadi komponen sekecil mungkin (sesuai tanggung jawabnya).
  - [x] Menambahkan default Modifier pada setiap komponen.
  - [x] Tidak menambahkan object sekaligus sebagai parameter, tetapi cukup yang dibutuhkan saja.
  - [x] Menggunakan key untuk LazyList/LazyGrid
  - [x] Memanajemen state dengan tepat.
- [x] Membuat fitur pencarian
- [x] Membuat fitur menambahkan dan menhapus data
- [ ] Membuat end-to-end testing untuk memeriksa fungsional seluruh halaman, baik positif case maupun negatif case.
