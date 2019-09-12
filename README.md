# Less Boilerplate Code For Spigot (lbcfs)
[![Build Status](https://travis-ci.com/SeineEloquenz/lbcfs.svg?branch=master)](https://travis-ci.com/SeineEloquenz/lbcfs)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/85c77b4180894c749fe2ad2c1d2965b2)](https://www.codacy.com/app/alexander-linder/lbcfs?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SeineEloquenz/lbcfs&amp;utm_campaign=Badge_Grade)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This project aims to create a framework for spigot plugin development to allow for much easier and faster
creation of new content by providing easy access to necessary, but often tedious, ugly or annoying parts of spigot
plugin development including but not limited to, command param validation, delegating to subcommands, access to other apis, registering of commands and listeners, creation of plugin.yml, etc...

# Features

## Tab Completion
Lbcfs supports easy creation of tab completion information. For every subcommand that belongs to a command, the tab
completion option is created automatically. For your own tab completion options you have to provide a two dimensional 
string array per (sub)command. You do this by overriding the method `getTaboptions()` inherited from `LbcfsCommand` .
Example for a command:
```java
    @Override
    protected String[][] getTabOptions() {
        return new String[][] { {"square", "circle"}, {"low", "medium", "high"}, {"cool"}};
    }
```
The new syntax is needed because java demands it, I can't circumvent that
This would result in a 3 level command, where the first argument can either be square or circle, the second low, medium
or high and the third argument cool.
