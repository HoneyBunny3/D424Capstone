# D424 Capstone App

## Overview
The **D424 Capstone App** is a project developed as part of the WGU Bachelor of Software Engineering. 
This application demonstrates the integration of modern mobile development practices with cloud-based services. 
The app is designed to be lightweight, scalable, and user-friendly.

## Features
- **Firebase Integration**: Seamless backend integration for authentication, database, and analytics.
- **Mobile Accessibility**: Android application designed with user experience in mind.
- **Scalability**: Configured with a Dockerfile for deployment flexibility.
- **Secure Development**: Followed best practices for secure app configuration and debugging.

## Technologies Used
- **Programming Language**: Java.
- **Firebase**: Authentication, Realtime Database, and Analytics.
- **Gradle**: Dependency and build management.
- **Docker**: For containerized deployment.
- **Version Control**: Managed using Git.

## Installation
### Prerequisites
- Android Studio (latest version)
- Firebase account and project setup
- Docker (if deploying using containers)

### Steps to Build and Run
1. Clone the repository from the completed_capstone branch:
   ```bash
   git clone [repository_url]
   cd D424Capstone
   ```
2. Open the project in Android Studio.
3. Sync Gradle by clicking **Sync Now**.
4. Update `google-services.json` with your Firebase project configuration.
5. Build and run the app on an emulator or physical device.

### Deploying with Docker
1. Build the Docker image:
   ```bash
   docker build -t d424-capstone-app .
   ```
2. Run the container:
   ```bash
   docker run -p 8080:8080 d424-capstone-app
   ```

## How to Use
Download and Install:

    Use the APK in the Main branch
    Clone the repository, use the completed_capstone branch, build, and install the app manually using the instructions in the Installation section.

Sign Up / Log In:

    Open the app on your device.
    Use the Sign Up option to create a new account.
    Authentication is powered by Firebase for a secure and seamless experience.

Main Features:

    Dashboard: After logging in, you'll land on the dashboard, which provides quick links to most used pages.
    [Feature 1]: Interact with the customer shopping page to make purchases.
    [Feature 2]: Setup a premium account to host a storefront and sell artisnal goods.
    [Feature 3]: Add a user profile for your loving cat.
    [Feature 4]: Connect with other users on the social page.
    [Feature 5]: Read the cat health and care tips on the cat love page.
    Notifications: Stay updated with real-time notifications for shipment tracking.

Navigation:

    Use the top navigation menu to switch between key application sections:
        Home: App landing page.
        Account Login: Log into your account.
        Account Sign Up: Sign up for a user account.
        Profile: View and manage your user profile.
        Cat Social: Socialize with other users.
        Cat Shopping: Purchase cat supplies.
        Love Your Cat: View cat care tips.
        Premium Subscription Sign Up: Sign up for a premium storefront account.
        Contact Us: Contact us page.
        Logout: Sign out of your user profile.

## Contribution Guidelines
Contributions are welcome now that I have completed my Capstone! Please follow these steps:
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/your-feature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push the branch:
   ```bash
   git push origin feature/your-feature
   ```
5. Submit a pull request.

## Acknowledgments
- Deborah Loring: Developer and Project Owner.
- [Acknowledgments for mentors, collaborators, or resources.]

## License
This project is not licensed.
