package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
        // Initialize the DAO with an empty mutable list of User objects
        val userDao: UserRepositoryDao<User> = UserRepositoryDaoImpl(mutableListOf())

        // Initialize the repository with the DAO
        val userRepository = UserRepositoryImpl(userDao)

        // Initialize the MainOperations with the repository
        val mainMenu = MainMenuUI(userRepository)

        // Display the current menu
        mainMenu.menuLoop()
}