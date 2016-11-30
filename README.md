# Shopkeeper
A League of Legends Item Set manager

With the release of the new client for League of Legends, one feature is noticably missing: the Item Sets manager. This program aims to be a stop-gap way for players to edit existing item sets and build new ones, automatically updating with each patch.

This project was developed on Java 1.8. If you have any problems running it, first check that you've updated to Java 1.8.

Current Status:
  - Identifies the current League patch
  - Saves static data (item icons, item descriptions, etc.) to local file system
    - For Windows, saves to "%appdata%/LoL_Shopkeeper"
    - For Mac, saves to "~/Library/Application Support/LoL_Shopkeeper"
    - For Unix, saves to "~/.LoL_Shopkeeper"
  - UI Shows a list of all items (ordered alphabetically)
  - Item Details added
    - Shows items built from
	- Shows items built into
	- For both from/into items, you can click to swap to that item
	- Details panel displays stats and unique passives/actives.
  - Item Set displayed
    - Different blocks for items

Next Step:
  - Add controls to create, open, save, and edit item sets
