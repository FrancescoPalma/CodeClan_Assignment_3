# :whale:  KittyTheWhale®  
  
This is my second Solo Project at CodeClan, Cohort3.  
We have to choose [_**one**_ among the following ideas](https://github.com/FrancescoPalma/CodeClan_Assignment_3/blob/master/project_definitions.md) and build an application according to the specs of the related project.  
I have chosen the **Underwater Exploration** game app in **Android** as I really want to build a game before finishing the course and I think the right time has finally come. This assignment comes after two weeks of classes on **Java** and the **Android** OS. We haven't covered anything about graphics and games, so this is really a 'wild' choice where one is free to truly experiment and flow with their own creativity.  
As soon as I heard the word 'wild' I said to myself, 'Go for it!'.  
I am not sure yet what the outcome would be but I want to make a working prototype and expand it step by step. I am keen to learn, experiment and welcome this challenge :sunglasses:  

**Time:** From Friday 29/04/16 to Thursday 05/05/16  
  
**Requirements for MVP:**  
Based on the popular video game [Flappy Bird](https://en.wikipedia.org/wiki/Flappy_Bird), design and implement a game that allows you to play as either a diver or fish to swim underwater, avoiding obstacles to reach their destination.
User presses up arrow or space bar (or tap screen in Android) to make diver or fish swim upwards. The diver or fish sinks toward the bottom between keypresses. 
  
**Potential Further functionalities to build:**  
* Diver must resurface for air before oxygen supply runs out
* Fish must avoid sharks and eat algae to score extra points 

## My App Journal | Your App Guide  
  
#### Day 1  
Today I've been working on the planning of the app, drawing the screens and using the white board to think about the structure of the code, the assets for the game, the main functionalities and the Java Classes represented in each file.  
My main concern was to understand what class would have responsible for.
I've focused a lot on the story and the meaning of the game as I am always oriented in creating a meaningful UX of any software I build, even if it's small or just for educational purposes I still work on it as if it were something to deliver to a client. In this case the 'client' is the degree of happiness and commitment I feel while building it :)  
I found really beneficial to share thoughts with other coworkers who are going to build the same app.  
Can't wait to start tomorrow, I'm fired up! :fire:  
  
#### Day 2 - 3  
During the weekend I enjoyed spending some time off, especially Sunday afternoon. Nevertheless, I focused on the design of the game.  
I followed a few tutorials on how to structure a little android game - in this case how to create a 'Flappy Bird' clone - from design to code, from graphics to sprites and animations.  
I understand how much time it takes to create a complete game.  
I am currently spending a lot of time on searching for the 'Assets' such as fonts, pictures (make them 'responsive' by storing them in xxhdpi, xhdpi, hdpi, mdpi and ldpi extensions. So, in a few words, a lot of layout design, Photoshop. Now the graphics and the UI are pretty clear in my head and my layout files are done.  
At the end of the weekend, I've decided to structure my **assets** and **res** main folders as follows: ![Alt img](https://github.com/FrancescoPalma/CodeClan_Assignment_3/blob/master/screenshots/screenshot-assets_res.png?raw=true)
- **assets** -> where the **fonts** are stored with .ttf extension
- **res** -> **drawable** with all the pictures stored with .png extension inside folders where you can find the different sizes (ldoi, mdpi, hdpi, xhdpi, xxhdpi) so that the assets should scale according to the size of the device.  
**layout** where  I've got 'maingame.xml' which is the layout responsible of displaying the whole game and 'splash_screen.xml' which displays the initial screen when the user clicks on the app icon.  
**raw** is the folder in which I store the sounds saved with the .ogg extension.  
Finally, the **values** folder in which I store style, color, strings etc. Although the most important file inside this folder is 'dashboard.xml' in which I can update the main settings of the game such as the overall speed of the game, the height of the jump performed by the whale, the acceleration, etc.  
  
#### Day 4  
Today I've been spending the whole day working on the 'GameThread' and 'GameView'.  
I feel pretty confident and I am enjoying a lot even though I understand how difficult and time-consuming is to make a game compared to an app.  
This is the Classes structure of my code: ![Alt img](https://github.com/FrancescoPalma/CodeClan_Assignment_3/blob/master/screenshots/screenshot-java.png?raw=true)  
- **Devices** is an open-source small library to get the market (consumer friendly) name of a device.  
- **GameThread** has been the most difficult to understand and thanks to documentation and tutorials I managed to make it work and actually write it. The 'thread' is a concurrent unit of execution which is used as the main engine that runs when the application is started.  
- **GameView** it's the one responsible of drawing the assets and the BitMaps on the screen through the Android Canvas.  
- **Ground** is the class responsible of creating an invisibile ground of the screen that will move the obstacles (in this case, the top and bottom pipes) from the right to the left of the screen as long as the user keeps tapping on the screen to make the whale jump to avoid the pipes and eating the plankton to increase the score.  
- **SoundClass** is the class responsible to manage the sounds effects on the game (i.e. when the whale touches a pipe, it dies and its related sound is being executed).  
- **SplashScreen** is the class responsible of 'when' and 'how' starting the initial screen when the app icon has been clicked by the user.
- **Start** is the class responsible of handling when the GameView must be executed, when the game is paused/resumed and if the user tries to exit the app it displays an alert dialog which says, 'Are you sure you want to exit?' then the user can click on 'Yes' or 'No'.
- **Wall** is the class responsible of setting the behaviour of the wall and the detection of the collision between the whale and the pipes(the whale will eventually die) and the plankton(the score will increase). What does it mean 'wall'? In this game the obstacles are represented by top and bottom pipes and between them there is a gap in which the whale must pass in order to avoid the collision with the pipes and to get a higher score by eating the plankton (which stays always in the gap between the pipes). In terms of code and design decisions, I thought it would have been a good idea to group the pipes and the plankton together since they share a similar behaviour in terms of 'collision' with the whale the difference is just the result: if the whale collides with the pipes, it dies and if the whale eats the plankton the score increases.
- **WhaleSprite** is the class responsible of setting the animation and the movement of the whale, especially the 'gravity simulation' which is just created by setting an acceleration (which is constant), a speed (at which the whale will fall if user doesn't tap on screen) and a jump (how high the whale will jump when the user taps on screen) variables. 
  
#### Day 5 - 6  
During the last two days I've been working on the whole structure and design of the Classes set during Day 4. It's been truly exhausting but I am really proud of the work done and commitment shown on this project. I can now consider it a true achievement as I managed to finish it by respecting the deadline.
  
## Ready to Launch! :rocket:  
The App is available for free [here](https://play.google.com/store/apps/details?id=com.francesco_p.kitty_whale_game)
    
2016 © Francesco Palma - This project is registered under the [MIT License](https://github.com/FrancescoPalma/CodeClan_Assignment_3/blob/master/License)
