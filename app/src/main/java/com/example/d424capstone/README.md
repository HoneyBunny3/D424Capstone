# D424Capstone

D424Capstone is a cat care application mobile application designed to debunk stereotypes about cats and showcase their unique personalities, 
emphasizing their need for affection and care. The application includes a storefront for cat supplies, wellness resources tailored for cats, 
artisanal goods crafted by registered users, and a social media component to facilitate connections among cat families. 

This project will culminate in a demonstrable prototype.

## Features

- **User Roles**:
    - Guest (No Log-in) User
    - Regular (Logged-in) User
    - Premium (Logged-in) User
    - Admin (Logged-in) User

- **Cat Profiles**: Users can create and manage profiles for their cats, including images and descriptions.

- **Storefront**: Premium users can create storefronts to sell artisanal goods.

- **Social Media**: Users can connect and share posts about their cats.

## Project Structure

- `app`: Contains the main application code.
    - `src`: Source code for the application.
        - `main`: Main source directory.
            - `java`: Java source files.
            - `res`: Resources such as layouts, drawables, and strings.

- `build.gradle`: Build configuration for the app.
- `gradle`: Gradle wrapper files.
- `gradlew` / `gradlew.bat`: Scripts to run the Gradle wrapper.
- `settings.gradle`: Gradle settings file.

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- Java Development Kit (JDK) 8 or higher
- Gradle

### Installation

1. **Clone the repository**:
   ```sh
   git clone <repository-url>
   cd d424-software-engineering-capstone-working_branch
   ```

2. **Open the project in Android Studio**:
    - Open Android Studio.
    - Select `Open an existing project`.
    - Navigate to the cloned repository and select it.

3. **Build the project**:
    - Click on `Build` in the top menu.
    - Select `Rebuild Project`.

4. **Run the project**:
    - Click on `Run` in the top menu.
    - Select `Run 'app'`.

## Usage

### User Authentication

- **Sign Up**: Users can sign up using their email address.
- **Log In**: Users can log in using their email address.

### Cat Profiles

- **Create Profile**: Users can create profiles for their cats.
- **Edit Profile**: Users can edit existing profiles.
- **Delete Profile**: Users can delete profiles.

### Storefront

- **Create Storefront**: Premium users can create a storefront.
- **Manage Storefront**: Users can manage their storefronts, including adding and removing products.

### Social Media

- **Post**: Users can create posts about their cats.
- **Like**: Users can like posts. Each user can only like a post once.
- **View**: Users can view posts from other users.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature-branch-name`
3. Make your changes.
4. Commit your changes: `git commit -m 'Add some feature'`
5. Push to the branch: `git push origin feature-branch-name`
6. Open a pull request.

## License

This project is unlicensed.

## Acknowledgements

- Thanks to all contributors and users for their support.