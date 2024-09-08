package org.example
import java.util.UUID

interface UserRepositoryDao<T : Identifiable> {
    fun save(item: T)
    fun findById(id: UUID): T?
    fun update(id: UUID, item: T)
    fun delete(id: UUID)
    fun count(): Int
    fun findAll(): MutableList<T>
}

class UserRepositoryDaoImpl<T : Identifiable>(private val items: MutableList<T>) : UserRepositoryDao<T> {
    override fun save(item: T) { items.add(item) }
    override fun findById(id: UUID): T? = items.find { it.id == id }
    override fun update(id: UUID, item: T) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            items[index] = item
        }
    }
    override fun delete(id: UUID) { items.removeIf { it.id == id } }
    override fun count(): Int = items.count()
    override fun findAll(): MutableList<T> = items
}