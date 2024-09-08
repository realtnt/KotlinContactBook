package org.example

import java.util.UUID

enum class Operation {
    LIST, ADD, DISPLAY, COUNT, SEARCH, EXIT,
    EDIT, DELETE, BACK, SELECT
}

enum class Menu(val allowedOperations: List<Operation>) {
    MAIN(listOf(Operation.LIST, Operation.ADD, Operation.DISPLAY, Operation.COUNT, Operation.SEARCH, Operation.EXIT)),
    LIST(listOf(Operation.SELECT, Operation.BACK)),
    DISPLAY(listOf(Operation.EDIT, Operation.DELETE, Operation.BACK))
}

class MainMenuUI(
    private val repository: UserRepository,
    private var currentMenu: Menu = Menu.MAIN,
    private var currentUser: User? = null,
    private var searchResults: List<UUID> = emptyList()
) {
    private fun displayUserList(users: List<User>) {
        users.forEachIndexed { index, user ->
            println("${index + 1}) Name: ${user.name}, Age: ${user.age}")
        }
    }

    private fun displayAllUsers() {
        val users = repository.findAll()
        displayUserList(users)
        saveSearchResults(users)
        currentMenu = Menu.LIST
    }

    private fun searchUsers() {
        print("Search for: ")
        val searchString = readln()
        val users = repository.findAll()
        val filteredUsers = users.filter { it.name.contains(searchString, ignoreCase = true) }
        displayUserList(filteredUsers)
        saveSearchResults(filteredUsers)
        currentMenu = Menu.LIST
    }

    private fun saveSearchResults(users: List<User>) {
        searchResults.toMutableList().clear()
        users.forEach { searchResults += it.id }
    }

    private fun addUser() {
        val user = getUserDetails()
        repository.save(user)
    }

    private fun selectUser() {
        if (searchResults.isEmpty()) return
        print("Select user: ")
        val userIndex = readln().toInt()

        currentUser = repository.findById(searchResults[userIndex - 1])
        currentMenu = Menu.MAIN
    }

    private fun editUser() {
        when (val currentUser = currentUser) {
            is User -> {
                val user = getUserDetails()
                repository.update(currentUser.id, user)
            }
            else -> {
                println("No user selected.")
            }
        }
    }

    private fun displayUser(userId: UUID?) {
        userId ?: return
        val user = repository.findById(userId)
        user ?: return

        currentUser = user
        println("Name: ${user.name}")
        println("Age: ${user.age}")

        currentMenu = Menu.DISPLAY
    }

    private fun displayUserCount() {
        println("There are ${repository.count()} users.")
    }

    private fun deleteUser(userId: UUID?) {
        userId ?: return
        println("Deleting user: ${currentUser?.name}")
        repository.delete(userId)

        currentMenu = Menu.MAIN
    }

    private fun displayCurrentMenu() {
        print("[${currentMenu.name.lowercase()}]")
        currentMenu.allowedOperations.forEach { operation ->
            print(" ${operation.name.lowercase()}")
        }
        println()
    }

    private fun getUserDetails(): User {
        print("Enter name: ")
        val name = readln()
        print("Enter age: ")
        val age = readln().toInt()

        return User(name = name, age = age)
    }

    private fun getUserInput(): Operation {
        displayCurrentMenu()

        while (true) {
            print("Enter your choice: ")
            val userInput = readln()
            val upperCaseInput = userInput.uppercase()
            val selectedOperation = currentMenu.allowedOperations.find { it.name == upperCaseInput }

            if (selectedOperation != null) {
                return selectedOperation
            } else {
                println("Invalid choice. Please try again.")
            }
        }
    }

    fun menuLoop() {
        while (true) {
            val selectedOperation = getUserInput()

            when (selectedOperation) {
                Operation.EXIT -> break
                Operation.BACK -> {
                    currentMenu = when (currentMenu) {
                        Menu.MAIN -> Menu.MAIN
                        else -> Menu.MAIN
                    }
                }
                Operation.LIST -> displayAllUsers()
                Operation.ADD -> addUser()
                Operation.DISPLAY -> displayUser(currentUser?.id)
                Operation.COUNT -> displayUserCount()
                Operation.SELECT -> selectUser()
                Operation.SEARCH -> searchUsers()
                Operation.EDIT -> editUser()
                Operation.DELETE -> deleteUser(currentUser?.id)
            }
            println()
        }
    }
}