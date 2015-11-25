WoodCutter
==========

Tree felling plugin for the Minecraft server software Bukkit.

WoodCutter on BukkitDev: http://dev.bukkit.org/bukkit-plugins/woodcutter/

Note: Some ugly code in here. Just quickly updated this to work with MC 1.7.2

# Building

## Command line (Win/Linux)

*Assuming maven is installed as in PATH*

1. Clone this repository using your git client (e.g. `git clone https://github.com/enjikaka/WoodCutter.git`)
2. Go into repository directory
3. Execute `mvn clean package`
4. Built Jar file will be located in the new `target` directory

## Eclipse (Mars)

1. [Clone this repository using your git client](http://i.imgur.com/bC4jeXD.png)
2. In a new or blank Eclipse Workspace, go to `File > Import`
3. [Under "Maven", select "Existing Maven Projects" and go Next](http://i.imgur.com/bpJWkZR.png)
4. [Set "Root Directory" to the cloned repository directory, click "Refresh", ensure the
`pom.xml` file is checked and go Finish](http://i.imgur.com/ZtQfG0a.png)
5. Go to `Run > Run Configurations...`
6. [Right-click "Maven Build" and click "New", then configure as such:](http://i.imgur.com/hcvONpN.png)
    * Set Name to "Build WoodCutter"
    * Set "Base Directory" to the repository's directory
    * Set "Goals" to `clean package` - This will make Maven clean the workspace, and then
    build the jar, on each build.
7. Click "Apply", and then "Run"
8. Built Jar file will be located in the new `target` directory
9. For subsequent builds, go to `Run > Run History > Build WoodCutter`

## IntelliJ

1. [Clone this repository using your git client](http://i.imgur.com/bC4jeXD.png)
2. In IntelliJ, go to `File > Open`
3. [Navigate to the repository and open the `pom.xml` file](http://i.imgur.com/FL8fa0L.png)
4. [Look for and open the "Maven Projects" tab](http://i.imgur.com/P76KyrE.png)
5. [Expand "WoodCutter" and then "Lifecycle"](http://i.imgur.com/afAlzT9.png)
6. [Double-click "Clean" and wait for the process to finish.](http://i.imgur.com/NHNd6rU.png)
This will ensure there are no left-over files from previous Maven builds that may
interfere with the final build.
7. [Double-click "Package" and wait for the process to finish](http://i.imgur.com/LX6PjAO.png)
8. Built Jar file will be located in the new `target` directory