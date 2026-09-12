# Overall documentation

1. Components:
   - rasa opensource for initial Natural Language Processing Unit and python actions
   - chroma database for Vector database
   - fastembedembeddings for embeddings
   - groq API for LLM
2. Setup:
   rasa
   install rasa using python virtual environment with python3.9.
   https://rasa.com/docs/rasa/installation/installing-rasa-open-source/

   - NLU repo:

     - https://git.razgar.io/razgar/interface/raz-ai/raz-ai-nlu
     - contains training & general configuration data:
       - branch development (old)
         - Contains a comprehensive amount of information formatted into YML files (before RAG)
       - branch featx (current)
         - Contains a setup with only essentail NLU components, so that the NLU is outsourced with RAG. (after RAG)
     - structure:
       - .gitignore
         - made to focus the repo only on the essentials files. Makes the repo clean, small, effective. Makes sure that models and other data is ignored to save space.
       - yml files in the root
         - config.yml
           - default things from rasa
           - FallbackClassifier. Triggers fallback intent when a given level of uncertainty about the request is reached.
         - endpoints.yml
           - connection to Actions server
         - credentials.yml
           - setup for socket.io, used for the chatbox-api on the website.
       - data/
         - stores training data: how the rasa behaves. It is a bit different than the default rasa configuration only in such way that it allows to have multiple domain/nlu/rules/story files in the correspondent subdirectories.
         - **Note:** due to the different file structure than the default rasa, commands such as train must be modified to use the "data/domain" directory for training, e.g:
           - "rasa train --domain data/domain"
         - in development branch, data/ stores in YML a big amount of NLU/responses data
         - in featx branch, essentails only, such as greeting, action triggers
         - Overview of subdirectories:
           - nlu: categorisation of what users may request from the bot (intents) with examples.
           - domain:
             - list of intents available,
             - list of responses availble,
             - other things that the chat must be capable of related to actions/recognition, such as forms, slots, and entities.
           - stories: connecting intents with responses.
           - rules: if-then relations between events/responses within for the bot
     - Creating bot workflow:
       - 1. Intent for triggering 'form'
       - 2. slots - these are the correspondent variables (input fields) that are needed to create a bot
         - the chatbot will keep asking for values for these slots, so that it can make the next step in the form
       - 3. form completion - returns the bot to the default state
       - Other things are handled directly on the action server with python code.

   - Action repo:

     - contains scripts to handle actions requests made from the NLU, such as requests to the RAG/ making bot.
     - actions/

       - actions_base.py - just a sample file.
       - actions_make_bot.py - documentation below
       - actions_asnwer_with_llm.py
         - handle NLU action trigger by making API call to the RAG. In this case, it is made by a function call `query_rag(...)`, which acts as the application programming interface
       - classes.py

         - helper methods moved to a separate class to keep clean the action classes.

       - rag-api-files, see documentation below
         - query_llm.py
         - query_rag.py
         - prompt_template.txt
         - conf.txt

     - documents/
       - a folder containing MD files with data, used for making a database
     - other-scripts/
       - db script
         - initialises a database from zero using the documents/ markdown files
       - query test script - just a sample script to review if the database queries are working well

   - Configuration on bh03

     - daemons are configured to start/stop/monitor rasa's NLU and Actions
       - rasa_action_server.service
       - rasa_nlu_server.service
     - there are currently no working automated pull scripts to syncronise the repository with the remotre repos
     - files are placed in folder: /var/www/html/rasa/GIT
     - VENV: stores installed dependencies as a python virtual environment.
       - enter it with:
         - "source ./VENV/bin/activate"
     - ACTIONSERVER - clone of actionrepo
     - NLUCORE2 - clone of nlu repo
       - also stores models in models/ folder
       - when a new configuration of the NLU is made and committed:
         - 1. it needs to be pulled
         - 2. the NLU needs to be re-trained to get latest model from the changes in the NLU, e.g:
           - "rasa train --domain data/domian"

   - chat widget
     - implemented in the Help drawer on the right of the webpage

     - script for it is in footer.inc.php

     - uses Dependencies/rasa-widget2.js

     - the js library used is from botfront widget repository, and it is modified in such way, that the chat widget is injected in the help drawer (view the last lines of the file).

     - due issues with the socketio/proxy server setup, the bot is made to clear chat history when the page loads, in order to preserve slots for user-id and environment. These slots are essential to create a bot for the user

# Action-make-bot

## Documentation

This module defines several classes and actions used in a Rasa chatbot to handle the "Make Bot" process, including creating a new bot, validating user inputs, and managing the flow of the conversation based on user responses. The classes and methods provided enable interactions with the user to create a trading bot, confirm or abort the process, and handle various scenarios like API errors and user input validation.

### Classes

#### 1. `ValidateFRMMakeNewBot`

This class inherits from `FormValidationAction` and is responsible for validating the user's inputs for various slots during the "Make Bot" process. The slots validated by this class are `SL_bot_name`, `SL_xconn_name`, `SL_xinstrument_name`, `SL_start_bot_flag`, and `SL_abort_make_bot_flag`.

**Methods:**

- **`name`**: Returns the name of the validation form action as `"validate_FRM_make_new_bot"`.

- **`validate_SL_bot_name`**: Validates the user's input for the bot name slot. If the user intends to abort the process, it returns a response indicating that the process should be aborted. Otherwise, it returns the provided bot name slot value.

- **`validate_SL_xconn_name`**: Validates the user's input for the exchange connector name slot. If the user wants to abort the process, it returns the appropriate response. If the connector is valid, it sets the exchange connector slot; otherwise, it shows an error message.

- **`validate_SL_xinstrument_name`**: Validates the user's input for the exchange instrument name slot. Handles scenarios where the user wants to abort, or when the provided exchange instrument name is invalid or valid. If the bot is successfully created, it reuses the `SLOT_BOT_NAME` to store the generated bot ID.

- **`validate_SL_start_bot_flag`**: Validates the user's decision to start the bot after it has been created. It checks for user intentions like aborting, affirming, or denying, and handles each accordingly by either starting the bot or stopping the process.

- **`validate_SL_abort_make_bot_flag`**: Validates the user's intention to abort the "Make Bot" process. Handles cases where the user wants to abort, confirms, denies, or continues the process.

#### 2. `ActionAskSLXConnName`

This action prompts the user to select an exchange connector from a list of available options.

**Methods:**

- **`name`**: Returns the name of the action as `"action_ask_SL_xconn_name"`.

- **`run`**: Executes the action by fetching the user's exchange connectors using `RazgaRUI_factory`. If the user has no exchange connectors, it displays an error message and stops the "Make Bot" process. If connectors are available, it formats them as buttons and displays them to the user.

#### 3. `ActionAskSLXInstrumentName`

This action prompts the user to select an exchange instrument from a list of options based on the previously selected exchange connector.

**Methods:**

- **`name`**: Returns the name of the action as `"action_ask_SL_xinstrument_name"`.

- **`run`**: Executes the action by fetching the list of exchange instruments available for the selected exchange connector. If the list is retrieved successfully, it formats them as buttons and displays them to the user. If any error occurs, it shows an error message and stops the "Make Bot" process.

#### 4. `ActionAskSLStartBotFlag`

This action prompts the user to confirm whether they want to start the bot after it has been created.

**Methods:**

- **`name`**: Returns the name of the action as `"action_ask_SL_start_bot_flag"`.

- **`run`**: Displays a prompt with "Yes" and "No" buttons asking if the user wants to start the bot. Based on the user's response, it updates the state of the conversation.

#### 5. `ActionAskSLAbortMakeBotFlag`

This action prompts the user to confirm whether they want to abort the "Make Bot" process.

**Methods:**

- **`name`**: Returns the name of the action as `"action_ask_SL_abort_make_bot_flag"`.

- **`run`**: Displays a prompt with "Yes" and "No" buttons asking if the user wants to abort the process. Based on the user's response, it updates the state of the conversation.

#### 6. `ActionCleanMakeBotSlots`

This action resets the slots related to the "Make Bot" functionality in the Rasa chatbot.

**Methods:**

- **`name`**: Returns the name of the action as `"action_clean_make_bot_slots"`.

- **`run`**: Clears the slots that store information related to the "Make Bot" process, such as bot name, exchange connector name, exchange instrument name, and flags indicating whether the bot should be started or the process should be aborted.

### Dependencies

- **`requests`** and **`json`**: Used for making HTTP requests and handling JSON data.
- **`typing`** module types: For type hinting such as `Any`, `Text`, `Dict`, `List`.
- **`rasa_sdk`** modules: Importing `Tracker`, `FormValidationAction`, `Action`, `CollectingDispatcher`, `EventType`, `SlotSet`, and `DomainDict` for Rasa-specific functionalities.

### Usage

These classes are used in the Rasa framework for building and managing conversations. The actions and validations are invoked based on user inputs, slots, and intents detected by Rasa's Natural Language Understanding (NLU) component. The validations ensure that the user inputs are correct, and the actions handle user interactions by guiding them through the bot creation process.

# RAG API

Here's the documentation for the files you provided:

### `query_llm.py`

This file defines a function to interact with a language model using the Groq API.

#### Functions:

- **`query_llm(query_text)`**: Sends a query to the language model and returns the response.
  - **Parameters:**
    - `query_text` (str): The text of the query to be sent to the language model.
  - **Returns:**
    - `str`: The content of the response message from the language model.
  - **Details:**
    - Initializes a Groq client using an API key.
    - Sends the user query as a message to the language model "llama3-8b-8192".
    - Returns the response from the model as a string.

### `query_rag.py`

This file is responsible for handling retrieval-augmented generation (RAG) queries by combining the use of vector stores and a language model.

#### Imports:

- `argparse`, `os`, `sys`: Python standard libraries for command-line argument parsing, file and system operations.
- `Chroma` from `langchain_community.vectorstores`: Handles vector store operations for retrieving relevant context.
- `FastEmbedEmbeddings` from `langchain_community.embeddings`: Provides an embedding function for text.
- `ChatPromptTemplate` from `langchain.prompts`: For formatting chat prompts.
- `query_llm` from `query_llm.py`: Imports the function to query the language model.
- Patches `sqlite3` to use `pysqlite3` for compatibility.

#### Global Configuration:

- **Constants:**
  - `PRINT_PROMPT_DEBUG`, `CONTEXT_K_FACTOR`, `CHROMA_PATH`, `PROMPT_TEMPLATE_PATH`, `CONFIG_PATH`: Default configuration values and paths.
- **Functions:**

  - **`load_prompt_template(file_path)`**: Loads a prompt template from a specified file.
    - **Parameters:** `file_path` (str)
    - **Returns:** Template string
  - **`load_config(config_path)`**: Loads configuration settings from a file.
    - **Parameters:** `config_path` (str)
    - **Modifies:** Updates global configuration variables.

- **Main Functionality:**
  - **`query_rag(query_text)`**: Retrieves relevant context from a vector store and sends a query to the language model.
    - **Parameters:**
      - `query_text` (str): The text of the user's query.
    - **Returns:**
      - `str`: The response from the language model.
    - **Details:**
      - Loads embeddings and vector store.
      - Searches for relevant documents and constructs context.
      - Formats a prompt using a loaded template.
      - Calls `query_llm` to get a response from the language model and returns it, potentially with debugging information.

### `prompt_template.txt`

This file contains a template used to format prompts for querying the language model.

#### Instructions for the Model:

- Focuses on matching the question to examples provided in the context.
- Restricts the model's knowledge and responses to specific domains (Razgar and cryptocurrency).
- Ensures strict adherence to using context information only.
- Provides guidelines for handling cases where the question is outside the model's knowledge or context.

### `conf.txt`

This file holds the configuration settings used by `query_rag.py`.

#### Settings:

- **`PRINT_PROMPT_DEBUG`**: Controls whether debug information is printed.
- **`CONTEXT_K_FACTOR`**: Determines the number of relevant documents to retrieve from the vector store.

These files work together to support a retrieval-augmented generation approach, where a language model is guided by relevant context extracted from a vector store to provide accurate and context-aware responses.
