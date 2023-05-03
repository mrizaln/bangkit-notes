## Kriteria Submission

Fitur yang harus ada pada aplikasi.

1. **Mempertahankan Fitur dari Submission Sebelumnya**

   Syarat yang harus dipenuhi sebagai berikut.

   - [x] Pastikan berbagai fitur yang ada dalam submission sebelumnya berjalan dengan baik.

2. **Menampilkan Maps**

   Syarat yang harus dipenuhi sebagai berikut.

   - [x] Menampilkan satu halaman baru berisi peta yang menampilkan semua cerita yang memiliki lokasi dengan benar. Bisa berupa marker maupun icon berupa gambar. Data story yang memiliki lokasi latitude dan longitude dapat diambil melalui parameter location seperti berikut.

     > [https://story-api.dicoding.dev/v1/stories?**location=1**](https://story-api.dicoding.dev/v1/stories?location=1 "https://story-api.dicoding.dev/v1/stories?location=1").

3. **Paging List**

   Syarat yang harus dipenuhi sebagai berikut.

   - [x] Menampilkan list story dengan menggunakan Paging 3 dengan benar.

4. **Membuat Testing**

   Syarat yang harus dipenuhi sebagai berikut.

   - [x] Menerapkan unit test pada fungsi di dalam ViewModel yang mengambil list data Paging.
   - [x] Berikut skenario yang harus Anda buat. Pastikan setiap skenario tersebut sudah ada kodenya.

     - [x] Ketika berhasil memuat data cerita.

       - [x] Memastikan data tidak null.
       - [x] Memastikan jumlah data sesuai dengan yang diharapkan.
       - [x] Memastikan data pertama yang dikembalikan sesuai.

     - [x] Ketika tidak ada data cerita.

       - [x] Memastikan jumlah data yang dikembalikan nol.

Berikut kerangka tampilan yang bisa Anda gunakan sebagai referensi.

![Layout](https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/academy/dos:2979e516a20f11ebf05f0d735a5b288120220221083443.jpeg)

## Saran Submission

Submission Anda akan dinilai oleh reviewer dengan **skala 1-5** berdasarkan dari parameter yang ada.

Anda dapat menerapkan beberapa saran di bawah ini untuk mendapatkan nilai tinggi.

- [x] Menuliskan kode dengan bersih.
  - [x] Bersihkan comment dan kode yang tidak digunakan.
  - [x] Indentasi yang sesuai.
  - [x] Menghapus import yang tidak digunakan.
- [x] Menambahkan informasi selama proses interaksi dengan API:
  - [x] Loading ketika memuat data
  - [x] Informasi error ketika gagal
  - [x] Pesan informasi ketika tidak ada data
- [x] Menggunakan custom [map style](https://mapstyle.withgoogle.com/) tersendiri pada Google Maps.
- [x] Menggunakan Paging 3 dengan menggunakan RemoteMediator.
- [x] Menambahkan input lokasi saat ini dari GPS yang bersifat opsional (gunakan checkbox atau switch) ketika tambah cerita.
- [x] Menerapkan Android Architecture Component (minimal ViewModel dan LiveData) dengan benar di semua halaman yang mengandung business logic.
- [x] Menerapkan UI test dan idling resources untuk salah satu skenario berikut.
  - [x] Memastikan mekanisme proses login dan logout.
  - [ ] Memastikan mekanisme proses tambah cerita.

Detail penilaian submission.

- **Bintang 1** : Semua ketentuan terpenuhi, namun penulisan kode masih perlu banyak diperbaiki atau terindikasi melakukan plagiat.
- **Bintang 2** : Semua ketentuan terpenuhi, tetapi penulisan kode masih perlu diperbaiki.
- **Bintang 3** : Semua ketentuan terpenuhi, tetapi hanya mengikuti seperti apa yang ada pada modul.
- **Bintang 4** : Semua ketentuan terpenuhi dan menerapkan minimal **empat** **(4) sara\*\***n\*\* yang ada di atas.
- **Bintang 5** : Semua ketentuan terpenuhi dan menerapkan **semua** **saran** yang ada di atas.

> **Catatan:**
> Jika submission Anda ditolak, maka tidak ada penilaian. Kriteria penilaian bintang di atas hanya berlaku jika submission Anda lulus.

## Submission yang Tidak Sesuai Kriteria

Submission akan ditolak jika tidak sesuai kriteria. Berikut poin-poin yang harus diperhatikan.

- Tidak mempertahankan fitur-fitur dari submission sebelumnya.
- Tidak ada tampilan daftar cerita dalam bentuk peta.
- List story tidak dapat mengakses halaman berikutnya secara otomatis.
- Tidak menerapkan Paging 3.
- Paging 3 memuat data terus menerus.
- Maps tampil tapi tidak ada marker.
- Ada marker namun tidak ada data yang ditampilkan.
- Tidak menerapkan Unit Test dengan baik.
- Kelas yang akan diuji menggunakan mock object, seharusnya real object.
- Penamaan test case tidak deskriptif sesuai skenario.
- Project tidak bisa di-build.
- Aplikasi force closed.
- Mengirimkan file selain proyek Android Studio.
- Mengirimkan proyek yang bukan karya sendiri.

## Forum Diskusi

Jika mengalami kesulitan, Anda bisa menanyakan langsung ke forum diskusi. [https://www.dicoding.com/academies/352/discussions](https://www.dicoding.com/academies/352/discussions?query=&query_criteria=&sort=&sort_direction=&title=&tutorial=258&keywords=&creator=).

## Resources

- Story API. API untuk berbagi story seputar Dicoding, mirip seperti post Instagram namun spesial untuk Dicoding. Dokumentasi dapat dilihat di [https://story-api.dicoding.dev/v1/](https://story-api.dicoding.dev/v1/)
- Simpan token yang didapatkan ketika login karena dibutuhkan untuk request lainnya.
- Supaya tetap bersih, data yang dikirimkan akan otomatis hilang setelah satu jam.

## Tips dan Trik

Ketika mengerjakan submission, mungkin Anda mengalami kendala. Oleh karena itu, kami mengumpulkan beberapa kendala yang sering ditemui siswa-siswa lain. Agar kendala tersebut tidak terulang kepada Anda, kami telah mengumpulkan tips untuk menanggulanginya.

Berikut adalah beberapa tips yang dapat Anda gunakan dalam pembuatan submission.

1.  **Kesalahan pemanggilan fungsi (invocation) yang dites.**

    Sebagai contoh, apabila Anda menguji kelas Repository atau bisa dikatakan Repository sebagai SUT(System Under Test), pastikan actual data yang diperiksa berasal dari pemanggilan fungsi di dalam Repository, bukan kelas lain seperti ApiService atau DAO. Ingat bahwa yang ingin kita uji adalah logika di dalam kelas Repository, bukan?

    1.  **Contoh salah**

        ```kotlin
        // Di dalam kelas Repository
        val actualStory = apiService.getStory()
        ```

    2.  **Contoh benar**

        ```kotlin
        // Di dalam kelas Repository
        val actualStory = storyRepository.getStory()
        ```

2.  **Jangan mock kelas yang sedang diuji (SUT).**

    Sebagai contoh, apabila Anda menguji kelas ViewModel, jangan mock kelas tersebut, gunakan object aslinya. Apabila kelas tersebut di-mock, pasti akan berhasil karena fungsi yang di-mock dan fungsi yang diperiksa sama, tidak ada pengujian logika sama sekali di sana. Ingat bahwa Mock hanya digunakan untuk dependency.

    1.  **Contoh salah**

        ```kotlin
        // Di dalam kelas ViewModel
        @Mock
        private lateinit var viewModel: StoryViewModel
        ...
        `when`(viewModel.getStory()).thenReturn(expectedStory)
        val actualStory = storyViewModel.getStory().getOrAwaitValue()
        ```

    2.  **Contoh benar**

        ```kotlin
        // Di dalam kelas ViewModel
        private lateinit var storyViewModel: StoryViewModel

        @Mock
        private lateinit var storyRepository: StoryRepository
        ...
        storyViewModel = StoryViewModel(storyRepository) // di @Before
        ...
        `when`(storyRepository.getStory()).thenReturn(expectedStory)
        val actualStory = storyViewModel.getStory().getOrAwaitValue()
        ```
