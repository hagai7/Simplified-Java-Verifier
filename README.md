# **Simplified Java Verifier**
## **Description:**
Sjava (Simplified Java) is a restricted subset of the Java programming language with a simplified syntax and reduced set of features. This program verifies whether a `.sjava` source file conforms to the defined rules of the subset.

The verifier takes a `.sjava` file as input and returns:
- `0` – if the code is legal.
- `1` – if the code is illegal.
- `2` – in case of I/O errors.

## **Prerequisites:**
- Java (JDK 8 or higher)
- A text editor or IDE (e.g. VSCode, IntelliJ)

## **Setup Instructions:**

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/simplified-java-verifier.git
    cd simplified-java-verifier
    ```

2. **Compile the Code:**
    ```bash
    javac main/Sjavac.java
    ```

3. **Run the Verifier:**
    ```bash
    java main.Sjavac path/to/file.sjava
    ```

## **Project Structure:**
The project includes the following Java packages:
- `main` – Contains the main verifier class (`Sjavac.java`)
- `parsing` – Handles parsing logic and input-related exceptions
- `scope` – Validates the structure and syntax of code scopes (includes relevant exceptions)
- `tests` – Contains example `.sjava` files with both valid and invalid code samples for testing

## **Example Usage:**
To verify an input file:
```bash
java main.Sjavac tests/ValidExample.sjava
