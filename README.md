TransperenC

Redefining NGO Accountability Through Real-Time Transparency

TransperenC is an Android application designed to strengthen trust between Non-Profit Organisations (NPOs) and donors through financial transparency, compliance monitoring, and project reporting.

The application aims to make organisational information more accessible and help donors understand how contributions are managed and used.

Key Features

* Live Financial Ledger: Provides a view of project budgets and actual expenditure to support financial transparency.
* NPO Compliance Score: Evaluates transparency using available organisational information, data completeness, and financial indicators.
* Donor Portal: Provides an interface for donors to explore projects and targeted contributions.
* Section 18A Certificate Simulation: Demonstrates the concept of generating donation tax certificates. Simulated certificates are not official tax certificates.
* Transparency Warriors: Uses experience points (XP) and levels to encourage organisations to maintain accurate, up-to-date reports.
* Statutory Deadline Alerts: Helps users monitor important governance and compliance deadlines.
* Financial and Project Reporting: Supports visibility into organisational activities, project progress, and financial information.

Technology Stack

Component	Technology
Platform	Android
Programming Language	Kotlin
User Interface	Jetpack Compose
Design System	Material 3
Backend and Database	Firebase Firestore
Authentication	Firebase Authentication
Architecture	Model-View-ViewModel (MVVM)

Project Structure and Architecture

TransperenC follows the MVVM architectural pattern to separate the user interface, application logic, and data-related responsibilities.

* Model: Represents application data and associated data operations.
* View: Displays application screens using Jetpack Compose.
* ViewModel: Manages UI-related state and coordinates interactions between the interface and application logic.

Firebase services support authentication and cloud-based data storage.

Installation and Setup

Prerequisites

Before setting up the project, ensure that you have:

* Android Studio installed.
* A compatible Java Development Kit (JDK) for the project’s Gradle configuration.
* Git installed.
* Access to the Firebase project and configuration required by the application.

Clone the Repository

git clone https://github.com/ST10442256/TransperenC.Term2.git

Open the cloned project in Android Studio and allow Gradle synchronisation to complete.

Firebase Configuration

Configure Firebase according to the project’s existing setup. Ensure that the required Firebase configuration file and services are correctly configured before running the application.

Do not commit private credentials, service-account keys, or other sensitive configuration values to source control.

Build and Run

1. Open the project in Android Studio.
2. Allow Gradle synchronisation to finish.
3. Connect a compatible Android device or start an emulator.
4. Select the application run configuration.
5. Build and run the application.
6. Verify the available screens and features on the device.

Exact build requirements may depend on the project’s Gradle and Android SDK configuration.

Testing and Quality Assurance

Testing should verify the application’s main workflows, including:

* Navigation and user interface behaviour.
* Authentication and access control.
* Financial and project reporting.
* Data retrieval and updates through Firebase.
* Compliance score calculations.
* Gamification functionality.
* Deadline notification behaviour.
* Handling of invalid inputs, missing data, and service failures.

Record actual test results, defects, and retests as part of the project’s quality assurance evidence. A successful build alone does not establish that every feature works correctly.

Current Limitations

* SMS gateway integration is not yet implemented.
* Section 18A certificate generation is a simulation and must not be treated as official tax certification.
* Actual functionality depends on the current implementation and Firebase configuration.

Future Roadmap

Phase 2: Blockchain Integration

Explore a blockchain-based ledger to strengthen the traceability and integrity of financial records.

Phase 3: Beneficiary Verification and SMS Integration

Investigate beneficiary verification and SMS gateway integration to support notifications and communication where appropriate.

Further Improvements

* Improve reporting and analytics.
* Strengthen automated testing and error handling.
* Enhance security and access control.
* Improve documentation and deployment procedures.

Repository

Mobile Application:
https://github.com/ST10442256/TransperenC.Term2

Project Vision

TransperenC aims to make NPO accountability more accessible by improving financial visibility, encouraging timely reporting, and strengthening donor confidence.

“Transparency isn’t just a feature; it’s the foundation of impact.”
