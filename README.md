# Social Network Console Application

This Java application is a console-based social network manager that allows users to interact with a simulated social network. It provides functionalities for managing users, friendships, and communities. Users can add and remove individuals, create friendships, and analyze community structures in the network.

## Features

The application includes the following functionalities:
- **Add User**: Register a new user by providing a unique ID and name.
- **Remove User**: Delete an existing user by their unique ID.
- **Add Friendship**: Establish a friendship between two users by their IDs.
- **Remove Friendship**: Delete an existing friendship between two users.
- **Print Number of Communities**: Display the total number of communities (disconnected user groups) in the network.
- **Print Most Sociable Community**: Identify and display the community with the highest number of friendships.

## Getting Started

### Prerequisites

Ensure that you have the following installed:
- [Java 17+](https://www.oracle.com/java/technologies/javase-downloads.html)

### Running the Application

1. Clone the repository and navigate to the project directory:
    ```bash
    git clone <repository-url>
    cd <project-directory>
    ```

2. Compile the project:
    ```bash
    javac -d bin src/ubb/scs/map/*.java src/ubb/scs/map/ui/*.java src/ubb/scs/map/service/*.java src/ubb/scs/map/domain/*.java
    ```

3. Run the application:
    ```bash
    java -cp bin ubb.scs.map.Main
    ```

### Usage

Upon running, the application presents the following menu options:

1. Add user
2. Remove user
3. Add friendship
4. Remove friendship
5. Print number of communities
6. Print the most sociable community 
q. Quit


Select an option by entering the corresponding number or letter and pressing Enter. Follow the prompts to complete each action.

## Code Overview

- **Console**: The main class that provides a command-line interface for users to interact with the social network.
- **UserService**: Manages user-related operations such as adding and deleting users.
- **FriendshipService**: Manages friendship-related operations, calculates the number of communities, and identifies the most sociable community.

## Example

Here is a sample session:

```plaintext
1. Add user
Give the user's id: 
1
Give the user's name (separated by space): 
John Doe
User added: User{id=1, firstName='John', lastName='Doe'}

1. Add user
Give the user's id: 
2
Give the user's name (separated by space): 
John Bovie
User added: User{id=1, firstName='John', lastName='Bovie'}

3. Add friendship
Give the first user's id: 
1
Give the second user's id: 
2
Friendship added: Friendship{user1Id=1, user2Id=2}

5. Print number of communities
Number of communities: 2
```

## Future improvements

Some potential enhancements include:

- Adding support for user authentication and authorization.
- Extending community analysis tools.
- Implementing a graphical user interface.

## Contribution Guidelines

- Contributions are welcome from anyone interested in improving the application.
- Contribution avenues include:
- Writing tests to ensure application stability and reliability.
- Enhancing specifications for better understanding and maintainability.
- Identifying and resolving bugs to improve the overall quality.
- All pull requests must undergo review by the repository owner before merging.

## Contact Information

- For inquiries or support related to the Social Network App, please contact:
- Email: alesiopuf@yahoo.com
- Email title: 'Social Network App Support'

---

Ensure to follow the setup instructions carefully to deploy the application successfully.



