# 🌍 GeoGenius

GeoGenius is a Java-based geography quiz game developed using **Java Swing**. Players answer geography questions across multiple difficulty levels while racing against the clock. The game features a graphical user interface, sound effects, lives, and a high-score system to create an engaging learning experience.

## ✨ Features

- 🎮 Interactive Java Swing GUI
- 🌎 Three difficulty levels:
  - Easy
  - Medium
  - Hard
- 🔀 Randomized questions for every game
- ❤️ Lives system
- ⏱️ Countdown timer for each question
- 🏆 High-score tracking
- 🔊 Background music and sound effects
- 📚 Question bank loaded from external text files

## 🛠️ Technologies Used

- Java
- Java Swing
- Object-Oriented Programming (OOP)
- File Handling
- Collections (ArrayList)
- Event-Driven Programming

## 📁 Project Structure

```
geographicGame/
│
├── src/
│   └── geographicGame/
│       ├── GameManagerGUI.java
│       ├── Question.java
│       ├── QuestionBank.java
│       ├── background.wav
│       ├── correct.wav
│       └── incorrect.wav
│
├── easy_questions.txt
├── medium_questions.txt
├── hard_questions.txt
├── highscore.txt
├── .project
├── .classpath
└── README.md
```

## 🚀 How to Run

1. Clone the repository.

```bash
git clone https://github.com/YOUR_USERNAME/GeoGenius.git
```

2. Open the project in Eclipse (or any Java IDE).

3. Ensure the question files are located in the project root:
   - `easy_questions.txt`
   - `medium_questions.txt`
   - `hard_questions.txt`
   - `highscore.txt`

4. Run:

```
GameManagerGUI.java
```

## 🎯 Gameplay

- Choose a difficulty level.
- Answer geography questions before the timer expires.
- Earn points for correct answers.
- Avoid losing all your lives.
- Try to beat your previous high score!

## 📸 Screenshots

You can add screenshots of the game here.

Example:

```
screenshots/
├── menu.png
├── gameplay.png
├── correct-answer.png
├── game-over.png
```

Then display them like this:

```markdown
### Main Menu

![Main Menu](screenshots/menu.png)

### Gameplay

![Gameplay](screenshots/gameplay.png)
```

## 📈 Future Improvements

- Online leaderboard
- Additional question categories
- User profiles
- Multiplayer mode
- Animated transitions
- Improved UI theme

## 👨‍💻 Author

**Ankit Aryal**

Computer Science Student

University of Central Missouri

GitHub: https://github.com/aryalankit121
