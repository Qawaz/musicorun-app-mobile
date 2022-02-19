package com.musicorumapp.mobile.api.models

interface BaseEntity {
    val name: String
    val entity: LastfmEntity
    val imageURL: String?
}