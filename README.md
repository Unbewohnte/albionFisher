# albionFisher - albion online fisher bot

# Compiling
`make jar` if you're on GNU/Linux, else - `javac src/xyz/unbewohnte/albionFisher/*.java`

# Launch
`java -jar albionFisher.jar`

# Usage
1. Open the game, make it to the water source
2. Set your cursor at the exact position where you want to drop the bobber
3. Look at the coordinates on the fisher's window and put them in the xy fields
4. By trial and error specify needed hold time in miliseconds so you could see ingame bobber being in the center of fisher's sight box (by clicking "fish" button)
5. With the same method set difference threshold (in most cases it is somewhere around 3-4 if you fish horizontally, 1-2 if vertically)
6. Start the bot again and leave the computer as is

Moving the mouse automatically (though not very reliably) stops the bot so you can move onto the next spot or entirely stop fishing without manual click on "stop" button