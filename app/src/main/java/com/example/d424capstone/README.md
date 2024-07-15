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
- **Share**: Users can share posts.

### Love Your Cat Page

- **Cat Care Tips**: Information about proper nutrition, grooming, health, exercise, and litter box maintenance. (Source: American Society for the Prevention of Cruelty to Animals (ASPCA), Humane Society of the United States)
- **Cat Socialization Tips**: Tips on introducing new cats, interacting with humans, interacting with other pets, and environmental enrichment. (Source: International Cat Care, American Association of Feline Practitioners (AAFP))
- **Promoting Positivity Towards Felines**: Address common misconceptions about cats, share success stories, and encourage community engagement. (Source: Cat Behavior Associates)

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


# Test Plan for Validator Unit Tests

## Objectives

The objective of these tests is to verify the correctness of validation logic implemented in the Validator, UserValidator, CatValidator, StorefrontValidator, and PostValidator classes.

## Scope

The scope of these tests includes:

- Email validation
- Alphabetic character validation
- Password validation
- Cat profile validation
- Storefront validation
- Social media post validation

## Test Approach

Unit tests will be written using JUnit to validate the logic in isolation from the rest of the application. Each method will be tested with various inputs to ensure correct behavior.

## Test Cases

### Test Case 1: Validate Email

- Objective: Verify that the email validation logic works correctly.
- Method: Validator.isValidEmail
- Inputs:
- - "test@example.com" (valid)
- - "test@" (invalid)
- - "test.com" (invalid)
- Expected Outputs:
- - true for "test@example.com"
- - false for "test@"
- - false for "test.com"

### Test Case 2: Validate Alphabetic Characters

- Objective: Verify that the alphabetic character validation logic works correctly.
- Method: Validator.isAlphabetic
- Inputs:
- - "John" (valid)
- - "John123" (invalid)
- Expected Outputs:
- - true for "John"
- - false for "John123"

### Test Case 3: Validate Password

- Objective: Verify that the password validation logic works correctly.
- Method: Validator.isPasswordValid
- Inputs:
- - "Password1!" (valid)
- - "pass" (invalid)
- Expected Outputs:
- - true for "Password1!"
- - false for "pass"

### Test Case 4: Validate User Sign-Up Input

- Objective: Verify that the user sign-up input validation logic works correctly.
- Method: UserValidator.validateSignUpInput
- Inputs:
- - ("test@example.com", "John", "Doe", "Password1!") (valid)
- - ("test@", "John", "Doe", "Password1!") (invalid email)
- - ("test@example.com", "John123", "Doe", "Password1!") (invalid first name)
- - ("test@example.com", "John", "Doe!", "Password1!") (invalid last name)
- - ("test@example.com", "John", "Doe", "pass") (invalid password)
- Expected Outputs:
- - true for ("test@example.com", "John", "Doe", "Password1!")
- - false for ("test@", "John", "Doe", "Password1!")
- - false for ("test@example.com", "John123", "Doe", "Password1!")
- - false for ("test@example.com", "John", "Doe!", "Password1!")
- - false for ("test@example.com", "John", "Doe", "pass")

### Test Case 5: Validate Cat Profile

- Objective: Verify that the cat profile validation logic works correctly.
- Method: CatValidator.validateCatProfile
- Inputs:
- - ("Whiskers", 2, "A friendly cat") (valid)
- - ("", 2, "A friendly cat") (invalid name)
- - ("Whiskers", -1, "A friendly cat") (invalid age)
- - ("Whiskers", 2, "") (invalid description)
- Expected Outputs:
- - true for ("Whiskers", 2, "A friendly cat")
- - false for ("", 2, "A friendly cat")
- - false for ("Whiskers", -1, "A friendly cat")
- - false for ("Whiskers", 2, "")

### Test Case 6: Validate Storefront

- Objective: Verify that the storefront validation logic works correctly.
- Method: StorefrontValidator.validateStorefront
- Inputs:
- - ("Cat Store", "A store for cat lovers") (valid)
- - ("", "A store for cat lovers") (invalid name)
- - ("Cat Store", "") (invalid description)
- Expected Outputs:
- - true for ("Cat Store", "A store for cat lovers")
- - false for ("", "A store for cat lovers")
- - false for ("Cat Store", "")

### Test Case 7: Validate Post

- Objective: Verify that the social media post validation logic works correctly.
- Method: PostValidator.validatePost
- Inputs:
- - "This is a post about my cat" (valid)
- - "" (invalid)
- Expected Outputs:
- - true for "This is a post about my cat"
- - false for ""

## Test Environment

   The tests will be executed in the local development environment using Android Studio.
   The JUnit framework will be used for writing and running the tests.

## Criteria for Passing

   All test cases must pass successfully.
   The validation methods should correctly identify valid and invalid inputs as per the defined expected outputs.

## Screenshots (Optional)

Include screenshots of the test results in Android Studio to demonstrate the successful execution of the tests. To take a screenshot:

    Run the tests.
    Wait for the results to appear in the test result window.
    Capture a screenshot of the results.

## Summary of Changes from Testing

### Identified Bugs: 
- No bugs identified
- There are several areas for improvement that were identified during testing

### Improved Logic: 
- CatDetails.java page improvements: added missing validation for empty cat bio field, cat age negative integer, cat profile updated successfully, also found cat details page was not editable.
- CatProfileScreen.java page improvements: added missing validation for empty cat bio field, cat age negative integer, cat profile saved successfully.
- 

### Added Tests: 
- Mention any additional tests added to cover edge cases or newly identified scenarios.