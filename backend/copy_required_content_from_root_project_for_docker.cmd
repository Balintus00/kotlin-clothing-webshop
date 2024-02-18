@echo off

mkdir rootProject
cd rootProject
mkdir gradle
cd ..
cd ..

copy "settings.gradle.kts" backend
cd backend
move "settings.gradle.kts" rootProject
cd ..

cd gradle
copy "libs.versions.toml" ..
cd ..
move "libs.versions.toml" backend
cd backend
move "libs.versions.toml" rootProject
cd rootProject
move "libs.versions.toml" gradle