# Shopkeeper
A League of Legends Item Set manager

With the release of the new client for League of Legends, one feature is noticably missing: the Item Sets manager. This program aims to be a stop-gap way for players to edit existing item sets and build new ones, automatically updating with each patch.

This project was developed on Java 1.8. If you have any problems running it, first check that you've updated to Java 1.8.

Current Status:
  - Creates a backup of your current Item Sets on first run
  - Identifies the current League patch
  - Saves static data (item icons, item descriptions, etc.) to local file system
    - For Windows, saves to "%appdata%/LoL_Shopkeeper"
    - For Mac/Unix, saves to "~/Library/Application Support/LoL_Shopkeeper"
  - Pulls item names/descriptions using the default language for your server
    - Most of the GUI uses icons instead of text to be language neutral
  - UI Shows a list of all items (ordered alphabetically)
    - List can be filtered by typing the name into the search bar
    - List can be filtered by typing in common abreviations (like "qss")
  - Item Details added
    - Shows items built from
    - Shows items built into
    - For both from/into items, you can click to swap to that item
    - Details panel displays stats and unique passives/actives.
  - Item Set displayed
    - Different blocks for items
    - Renameable
  - Basic Controls
    - "New" works
    - "Save" currently prints the json to the console, but lacks a way to specify champion it's for   

Next Step:
  - Add controls to Open, Save, Edit and Delete item sets
  - Specify targeted champion(s) for an Item Set
