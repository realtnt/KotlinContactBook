package org.example
import java.util.UUID

interface Identifiable {
    val id: UUID
}

data class User(
    override val id: UUID = UUID.randomUUID(),
    val name: String,
    val age: Int
) : Identifiable {
    val intId: Int
        get() = id.hashCode()

    override fun toString(): String {
        return "User(id=$intId, name='$name', age=$age)"
    }
}