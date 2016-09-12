# colourshift-solver
This is a solver for Colourshift game.

The game is available here: http://www.kongregate.com/games/mrsneeze/colourshift

The application has GUI that can be used to create a board to solve. GUI also simulates the colour flow just like the original game's GUI.

Technologies used:
- Java 8
- Maven
- Spring 4.0
- JavaFX (GUI)

The solving algorithm is a variation of Branch and Bound. The state space is the collection of all available configurations of blocks rotations. 
In the bound step, a set of rules is applied to remove as many points for the state space as possible - e.g. 'target' blocks should not point to each other.
In the branch step, any block, that still has more than one rotation in state space is selected and a branch of board is created for each rotation.

The application can solve most of small boards (10 blocks wide or less) within a few seconds.
The biggest board tested was 20 blocks wide.

If used for big boards (width > 10), allocate at least 512MB of stack space.
