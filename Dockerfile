# Use the official OpenJDK 17 slim image as the base image
FROM openjdk:17-slim

# Install necessary packages
RUN apt-get update && \
    apt-get install -y wget unzip dos2unix && \
    rm -rf /var/lib/apt/lists/*

# Download and install Android SDK Command Line Tools
RUN mkdir -p /opt/android-sdk/cmdline-tools && \
    cd /opt/android-sdk/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O commandlinetools.zip && \
    unzip commandlinetools.zip && \
    mkdir -p /opt/android-sdk/cmdline-tools/latest && \
    mv cmdline-tools/* /opt/android-sdk/cmdline-tools/latest && \
    rm commandlinetools.zip

# Set environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$PATH
ENV PATH=$ANDROID_HOME/platform-tools:$PATH

# Accept licenses
RUN yes | sdkmanager --licenses

# Install essential SDK packages
RUN sdkmanager "platform-tools" "platforms;android-30" "build-tools;30.0.3"

# Copy the project files into the Docker container
COPY . /app

# Set the working directory
WORKDIR /app

# Convert line endings of gradlew to Unix format and make it executable
RUN dos2unix /app/gradlew && chmod +x /app/gradlew

# Build the Android project
RUN /app/gradlew assembleDebug

# Define the entry point to keep the container running
ENTRYPOINT ["tail", "-f", "/dev/null"]