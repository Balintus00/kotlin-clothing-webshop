#!/bin/bash

mkdir "rootProject"
cp ../settings.gradle.kts rootProject/

cd rootProject
mkdir "gradle"
cp ../../gradle/libs.versions.toml gradle/