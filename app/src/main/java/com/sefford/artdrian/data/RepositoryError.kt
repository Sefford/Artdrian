package com.sefford.artdrian.data

sealed class RepositoryError {
    class NotFound(val id: String = "") : RepositoryError()
    class NetworkingError(val status: Int = 0) : RepositoryError()
}
