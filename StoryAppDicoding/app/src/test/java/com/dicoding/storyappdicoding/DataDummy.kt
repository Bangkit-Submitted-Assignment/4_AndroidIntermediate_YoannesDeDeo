package com.dicoding.storyappdicoding

import com.dicoding.storyappdicoding.api.ListStoryItem

class DataDummy {
    fun generateDummyResponse(): List<ListStoryItem> {
        val listStory: MutableList<ListStoryItem> = mutableListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "photoUrl $i",
                createdAt = "createdAt $i",
                name = "name $i",
                description = "description $i",
            )
            listStory.add(story)
        }
        return listStory
    }


    fun dummyToken(): String ="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWM0NUlQYld0MkpseDBRSVciLCJpYXQiOjE3MDA0MTg0NTJ9.UStQl7X54gvE5EDTeflvfYkKwPCIVC1sfdKzF0mKHNY"
}