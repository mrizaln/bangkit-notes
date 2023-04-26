```text
MyNewsApp2
└── app
    └── src
        └── main
            ├── assets
            └── java
                └── com
                    └── dicoding
                        └── newsapp
                            ├── data
                            │   ├── local
                            │   │   ├── entity
                            │   │   │   └── NewsEntity.kt
                            │   │   └── room
                            │   │       ├── NewsDao.kt
                            │   │       └── NewsDatabase.kt
                            │   ├── NewsRepository.kt
                            │   ├── remote
                            │   │   ├── response
                            │   │   │   └── NewsResponse.kt
                            │   │   └── retrofit
                            │   │       ├── ApiConfig.kt
                            │   │       └── ApiService.kt
                            │   └── Result.kt
                            ├── di
                            │   └── Injection.kt
                            ├── ui
                            │   ├── detail
                            │   │   ├── NewsDetailActivity.kt
                            │   │   └── NewsDetailViewModel.kt
                            │   ├── list
                            │   │   ├── HomeActivity.kt
                            │   │   ├── NewsAdapter.kt
                            │   │   ├── NewsFragment.kt
                            │   │   ├── NewsViewModel.kt
                            │   │   └── SectionsPagerAdapter.kt
                            │   └── ViewModelFactory.kt
                            └── utils
                                └── DateFormatter.kt

21 directories, 18 files
```

For some unknown reason, I can't advance through fragment test. The IDE keeps complaining that "Syntax highlighting has been temporarily turned off in file xxxx.kt because of an internal error". Without said function, I virtually can not do anything.

That said, the next part of the test also can't be finished.
