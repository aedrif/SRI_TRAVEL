"# SRI_TRAVEL" 

## Instructions for Setup

Before running the application, please follow the steps below to ensure the necessary data is properly set up in your database.

### 1. Import Filtering Tokens

The filtering tokens related to travelling are stored in the `/Documents/sri_tokens.sql` file. To import them into your database:

1. Open your database management tool (e.g., MySQL Workbench, phpMyAdmin, etc.).
2. Connect to your database.
3. Locate and execute the SQL script found at:
   ```
   /Documents/sri_tokens.sql
   ```
   This will populate the database with the required filtering tokens for travelling.

### 2. Add Documents

You can add documents to the system in two ways:

#### Option 1: Add Documents Manually

1. Open the client application.
2. Use the provided interface to add documents manually.

#### Option 2: Import Documents Using SQL Script

1. Open your database management tool.
2. Locate and execute the SQL script found at:
   ```
   /Documents/sri_document.sql
   ```
   This will populate the database with a predefined set of documents.

### 3. Run the Frontend Application

To run the frontend application:

1. Navigate to the frontend application directory:
   ```
   cd ./SRI_frontend
   ```
2. Install the necessary dependencies:
   ```
   npm install
   ```
3. Start the application:
   ```
   npm start
   ```

The frontend application should now be running on your local development server (e.g., `http://localhost:3000`).

---

By completing the steps above, your database and frontend application will be ready to run smoothly. For further instructions or troubleshooting, please refer to the application documentation or contact the support team.
