package com.prayatna.lookiesapp.utils

sealed class DataResult<out R> private constructor(){
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val error: String) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
    data object Idle : DataResult<Nothing>()
}

inline fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> {
    return when (this) {
        is DataResult.Success -> DataResult.Success(transform(data))
        is DataResult.Error -> DataResult.Error(error)
        is DataResult.Loading -> DataResult.Loading
        is DataResult.Idle -> DataResult.Idle
    }
}