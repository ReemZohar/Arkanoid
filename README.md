# Arkanoid

A clean, object‑oriented Java implementation of **Arkanoid** with a twist: **blocks are removed only when the ball’s color is *different* from the block’s color.** On a hit, the ball adopts the block’s color, so you’ll need to **alternate colors** to clear the board.

<p align="center">
  <img width="596" height="451" alt="Screenshot 2025-09-03 214725" src="https://github.com/user-attachments/assets/329e1ca9-4a49-4074-a677-51793a7847d7" />
</p>
---

## Gameplay

- **Goal:** Clear all blocks before you run out of balls.
- **Twist (color rule):**
  - If the **ball color ≠ block color**, the block is removed (+5 points), and the ball **changes** to that block’s color.
  - If the **ball color = block color**, the block is **not** removed (the ball just bounces).
- **Start conditions:**
  - **Board:** 800×600, light‑blue background
  - **Blocks:** 6 rows; row colors are random; row lengths: 12, 11, 10, 9, 8, 7
  - **Paddle:** centered at bottom; step = 5 px; **wraps around** screen edges
  - **Balls:** 3 balls with random positions/speeds; initial color = red
- **Score:**
  - +5 per removed block
  - +100 when all blocks are cleared
  - Score is shown at the top bar
- **Game over:** when **all balls are lost** (fall into the “death region”) or **all blocks are cleared**.

---

## Controls

- **← / →** — move paddle left/right
- Paddle **wraps** to the opposite edge when crossing a border.

---

## Project Structure

```
Arkanoid-main/
├─ biuoop-1.4.jar
├─ src/
│  ├─ Arkanoid.java                # Entry point
│  ├─ Arkanoid/GameAssets/
│  │  ├─ Game.java                 # Main loop, initialization, orchestration
│  │  ├─ GameEnvironment.java      # Collision space (list of Collidable)
│  │  ├─ HitListener.java          # Event interface
│  │  ├─ HitNotifier.java          # Event source interface
│  │  └─ ScoreTrackingListener.java# +5 on block removal
│  ├─ Arkanoid/Geometry/           # Geometry & physics helpers
│  │  ├─ Point.java, Line.java, Rectangle.java, Velocity.java, CollisionInfo.java
│  ├─ Arkanoid/Sprites/
│  │  ├─ Sprite.java, SpriteCollection.java   # Drawable/tickable entities
│  │  ├─ Collidable.java                       # Collision contracts
│  │  ├─ Ball.java                             # Ball, color changes on hit
│  │  ├─ Block.java                            # Removable, notifies listeners
│  │  ├─ Paddle.java                           # Player paddle + wrap logic
│  │  ├─ BlockRemover.java / BallRemover.java  # Event listeners
│  │  └─ ScoreIndicator.java                   # Top UI with score
│  └─ Arkanoid/Utils/
│     ├─ Counter.java                          # Simple counter
│     └─ Operations.java                       # Epsilon compare, random color, etc.
└─ README.md
```

---

## Build & Run

### Prerequisites
- **JDK 8+**
- No external build tool required (Gradle/Maven not used)
- The repository includes **`biuoop-1.4.jar`**

### Compile (Linux/macOS)
```bash
cd Arkanoid-main
mkdir -p out
javac -cp biuoop-1.4.jar -d out $(find src -name "*.java")
```

### Run (Linux/macOS)
```bash
java -cp "out:biuoop-1.4.jar" Arkanoid
```

### Compile (Windows, PowerShell)
```powershell
cd Arkanoid-main
mkdir out
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -cp "biuoop-1.4.jar" -d out $files
```

### Run (Windows)
```powershell
java -cp "out;biuoop-1.4.jar" Arkanoid
```

---

## Tuning & Constants (edit in `Game.java`)

In `initialize()`:
- `int width = 50, height = 15` — block dimensions
- `int numBlocks = 12, numRows = 6` — grid size (row length decreases by 1 each row)
- `int numBalls = 3, r = 5` — number of balls and radius
- Screen: `800×600` (also referenced in bounds/death region)
- Score bonus on level clear: `levelPassed = 100` (in `run()`)

---

## Key Design Ideas

- **Sprite / Collidable** interfaces decouple rendering/ticking from collision logic.
- **GameEnvironment** computes closest collisions for a trajectory.
- **Event system** (`HitNotifier`/`HitListener`) distributes reactions:
  - `BlockRemover`: removes block & decrements remaining‑blocks counter
  - `BallRemover`: removes ball on hitting the death region
  - `ScoreTrackingListener`: increments score on removals
  - `Ball` also listens to hits to **adopt the hit block’s color**
- **ScoreIndicator** draws a dedicated top bar with live score.

---

## Dependencies

- [`biuoop-1.4.jar`](biuoop-1.4.jar): simple GUI, keyboard, surface drawing
  - Classes used: `GUI`, `DrawSurface`, `Sleeper`, `KeyboardSensor`
