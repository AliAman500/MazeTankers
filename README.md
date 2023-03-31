# Maze Tankers - Java3D Game


## Description
The main focus of the game is to traverse a pre-generated maze and defeat the other player before they defeat you. In the game, there are multiple tanks which face off against each other by shooting at each other. One shot is all it takes to destroy a tank! So be careful with positioning and aim, try to make the most out of each shot! Projectiles generated from shooting can also bounce off of walls and your own bullet can damage you, making up for an interesting gameplay. 
<br/>

### Built With
- Java3D for 3d models and awt for graphics
- Networking using java.net library, UDP-based networking
- 3D Models were built with Blender or taken from creative commons, no licensed assets used
<hr/>

## Controls
'W' - move forward
<br/>
'S' - move backward
<br/>
'A' - turn left
<br/>
'D' - turn right
<br/>
Move mouse to aim
<br/>
Left click to shoot bullet

## Installation and starting the game
MazeTankers can be installed at https://github.com/AliAman500/Tank3D. Pull the project in Eclipse using the link. To play the game on local network, run the Server.java file. Only one machine can have the Server running on it. Copy the Local IP address mentioned in the Server program. Run Game.java and paste the ip address that was copied in the ip address text field, type in a username and click "Multiplayer". You will now be connected to the server and a lobby will be generated. To play with friends and make them join the lobby, send them the ip address that you copied and they can use it to join. To make your friends join from other networks, like their homes, you must forward port 9888 on your router and send them the public ip of server, but people on the same network as the Server will still use the local IP to join.

**NOTE:** Server will only run on one machine, and players will only use the Game.java file to connect to the server and play the game.
**NOTE:** Playing the game from multiple networks is still in progress as issues persist. In the meantime, players on local network can play without any issues.

#### Troubleshooting and Errors
If you receive an ArrayIndexOutOfBoundsException when you try to play, you will need to add the following vm arguments

--add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED

## Licensing
Our project is open-source, we believe that allowing the community to reuse our code, we can help individuals better understand key concepts in programming and potentially work on their own projects to create valuable software for people. Open Source helps the programming community by removing barriers between innovators. It promotes a free exchange of ideas within a community driven to learn and better themselves and the industry as a whole. If you want to add more features to the game, contact Ali at burkia@uwindsor.ca to get collobaration permission.

**Contribution**

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**. If you have a suggestion that would make this better, please email Ali at burkia@uwindsor.ca for collaboration permissions.

**Licensed Libraries**
###### 1 - Java3D licensed under the GNU LGPL version 2.1 for public use.
###### 2 - Java AWT: Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  Public use allowed.


## Roadmap
###### [ ] Work on procedurally generated mazes
###### [ ] Work on more than 9 players
###### [ ] Prevent and handle server overloadingg
###### [ ] Work on different game modes
