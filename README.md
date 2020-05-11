# PHP code generator + ![JetBrains IntelliJ plugins](https://img.shields.io/jetbrains/plugin/d/12590-php-code-generator-.svg)  [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

PhpStorm plugin that lets you generate even more PHP code.

Plugin provides the following features:
- Generates calls for the getters of the declared variable, if getters are fluent it will chain them (Shift +Ctrl + NUMPAD1)
- Generates calls for the getters of the declared variable (Shift +Ctrl + NUMPAD2)

Key                  | Value
-------------------- | --------------------
Plugin Url           | https://plugins.jetbrains.com/plugin/12590-php-code-generator-
ID                   | PHPCodeGenerator+

## Install
- Install the plugin by going to `Settings -> Plugins -> Marketplace` and then search for `PHP code generator`

## Usage

By placing your caret at an initialized variable and pressing `ALT + Insert` a PhpStorm code generator
menu will popup you can then choose `Get getters` or `Get setters` option.
Actions also have shortcuts `Shift + Ctrl + NUMPAD1` and `Shift + Ctrl + NUMPAD2`.

## Authors
- Kristijan Kanalaš (kanalaskristijan@gmail.com)
