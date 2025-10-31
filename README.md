# Scrabble PROP

![Java](https://img.shields.io/badge/Java-11+-blue?logo=java)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux-green)
![License](https://img.shields.io/badge/License-GPL%20v3-blue)

A cross‑platform Scrabble application (Windows & Linux) letting you play against a bot or a friend.

---

## 📋 Table of Contents

- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Installation & Running](#installation--running)  
  - [🐧 Linux](#🐧-linux)  
  - [🪟 Windows](#🪟-windows)  
- [Documentation](#documentation)
- [Project Structure](#project-structure)  
- [Screenshots](#📸-screenshots)  
- [Contributing](#🤝-contributing)  
- [Authors](#👥-authors)  
- [License](#📝-license)  


## Features

- Play in **Catalan**, **Spanish**, or **English** 🌍  
- Human vs. **Bot** 🤖 or Human vs. **Human** 👥  
- Profile management with different avatars and personal statistics 📊  
- For a better experience, turn on the sound on your computer 🔊
- Full Swing-based GUI, identical on Linux and Windows 🖥️  


## Prerequisites

- ☕ **Java 11** (or higher)
- 📦 **Gson 2.9** (for JSON load/save)

### 📥 Downloading Required Libraries

Before compiling, make sure to download the necessary `.jar` files:

- On **Linux/macOS**: run `./get_libs.sh`
- On **Windows**: run `get_libs.bat`

This will download Gson libraries into the `src/lib/` directory.


## Installation & Running

> All compile/run commands should be executed from the `src` directory.

#### 🐧 Linux

```bash
# Compile
make

# Launch
./scrabble.sh
```

#### 🪟 Windows

```powershell
# Compile
.\compilar.bat

# Launch
.\scrabble.bat
# (or double‑click the “Scrabble – PROP” shortcut in Explorer)
```

## Documentation

You can browse the full Doxygen-generated source code documentation here:

> 📄 Online documentation generated with Doxygen is available [here](https://rboxvelasco.github.io/Scrabble/).

> **Note**: Most of the source code and comments are written in Catalan, as the project was developed in a trilingual academic environment.


## Project Structure

```
📂
│
├── docs/                     # Project Doxygen documentation.
├── design/                   # UML's & algorithm rationale.
├── EXE/                      # Compiled classes & runtime files
│
├── src/                      # Source code & build scripts
│   ├── domini/               # Domain layer
│   │   ├── auxiliars/
│   │   ├── diccionari/
│   │   ├── excepcions/
│   │   ├── jugadors/
│   │   ├── scrabble/
│   │   └── sessio/
│   │
│   ├── persistencia/         # Persistence layer
│   ├── interficie/           # Presentation layer (Swing)
│   ├── controladors/         # Controllers
│   │
│   ├── lib/                  # Third‑party JARs
│   ├── resources/            # Images, .txt, Doxygen assets
│   ├── data/                 # Persistent data store
│   │
│   ├── Makefile              # Linux build
│   ├── compilar.bat          # Windows build
│   ├── scrabble.sh           # Linux launcher
│   ├── scrabble.bat          # Windows launcher (with debug)
│   ├── scrabble.vbs          # Windows launcher (no debug)
│   └── "Scrabble - PROP.lnk" # Windows shortcut
│
├── get_deps.sh               # Linux dependencies installer
├── get_deps.bat              # Windows dependencies installer
└── Doxyfile                  # Doxygen configuration
```


## 📸 Screenshots

![Main Window](src/resources/screenshots/main_window.png)
![Game Window](src/resources/screenshots/game_window.png)
![Statistics Window](src/resources/screenshots/avatars_statistics.png)

## 🤝 Contributing

Feel free to fork the repository, improve the code, add features or fix bugs!


## 👥 Authors

- Raül Box  
- Aina Serra  
- Ada Peña  
- Martina Cusidó  


## 📝 License

This project is licensed under the **GNU GPL v3**. See [LICENSE](./LICENSE) for details.
