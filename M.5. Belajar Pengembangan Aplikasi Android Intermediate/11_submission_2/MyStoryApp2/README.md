```text
MyStoryApp2
└── app
    └── src
        └── main
            └── java
                └── com
                    └── example
                        └── mystoryapp2
                            ├── model
                            │   ├── data
                            │   │   ├── local
                            │   │   │   ├── database
                            │   │   │   │   ├── RemoteKeyDao.kt
                            │   │   │   │   ├── RemoteKey.kt
                            │   │   │   │   ├── StoryDao.kt
                            │   │   │   │   ├── StoryDatabase.kt
                            │   │   │   │   └── Story.kt
                            │   │   │   └── pref
                            │   │   │       ├── User.kt
                            │   │   │       └── UserPreferences.kt
                            │   │   └── remote
                            │   │       ├── response
                            │   │       │   ├── LoginResponse.kt
                            │   │       │   ├── NewStoryResponse.kt
                            │   │       │   ├── RegisterResponse.kt
                            │   │       │   ├── StoryDetailResponse.kt
                            │   │       │   └── StoryListResponse.kt
                            │   │       └── retrofit
                            │   │           ├── ApiConfig.kt
                            │   │           └── ApiService.kt
                            │   ├── di
                            │   │   ├── Injection.kt
                            │   │   └── TokenHolder.kt
                            │   └── repository
                            │       ├── AppRepository.kt
                            │       ├── RequestResult.kt
                            │       └── StoryRemoteMediator.kt
                            ├── ui
                            │   ├── activity
                            │   │   ├── LoginActivity.kt
                            │   │   ├── MainActivity.kt
                            │   │   ├── MapsActivity.kt
                            │   │   ├── RegisterActivity.kt
                            │   │   ├── StoryAddActivity.kt
                            │   │   └── StoryDetailActivity.kt
                            │   ├── adapter
                            │   │   ├── LoadingStateAdapter.kt
                            │   │   └── StoryListAdapter.kt
                            │   ├── customview
                            │   │   ├── EmailInputView.kt
                            │   │   └── PasswordInputView.kt
                            │   └── viewmodel
                            │       ├── LoginViewModel.kt
                            │       ├── MainViewModel.kt
                            │       ├── MapsViewModel.kt
                            │       ├── RegisterViewModel.kt
                            │       ├── StoryAddViewModel.kt
                            │       ├── StoryDetailViewModel.kt
                            │       └── ViewModelFactory.kt
                            └── util
                                ├── EspressoIdlingResource.kt
                                └── ImageFileHelper.kt

24 directories, 38 files
```
