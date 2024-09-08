package org.example
import java.util.UUID

// Repository Layer
interface UserRepository {
    fun findById(userId: UUID): User?
    fun findAll(): List<User>
    fun count(): Int
    fun save(user: User)
    fun update(userId: UUID, user: User)
    fun delete(userId: UUID)
}

class UserRepositoryImpl(private val dao: UserRepositoryDao<User>) : UserRepository {
    override fun findById(userId: UUID): User? = dao.findById(userId)
    override fun findAll(): List<User> = dao.findAll()
    override fun count(): Int = dao.count()
    override fun save(user: User) = dao.save(user)
    override fun update(userId: UUID, user: User) = dao.update(userId, user)
    override fun delete(userId: UUID) = dao.delete(userId)
}