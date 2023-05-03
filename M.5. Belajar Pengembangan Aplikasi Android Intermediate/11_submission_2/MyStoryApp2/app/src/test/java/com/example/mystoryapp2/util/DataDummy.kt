package com.example.mystoryapp2.util

import com.example.mystoryapp2.model.data.local.database.Story
import java.time.Instant

object DataDummy {
    fun generateDummyStories(): List<Story> = (0..100).map { i ->
        Story(
            id = i.toString(),
            name = i.toString(),
            description = i.toString(),
            createdAt = Instant.now().toString(),
            photoUrl = "https://avatars.githubusercontent.com/u/61090869?s=400&u=1f43bc8698611e0662fd57a8687c7f5b91b622c2&v=4",
            lon = null,
            lat = null
        )
    }
}