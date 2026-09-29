package otus.homework.flowcats

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error(val error: Exception) : Result<Nothing>()
}
