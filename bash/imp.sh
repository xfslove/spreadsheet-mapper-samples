#!/usr/bin/env bash
echo "read person information from ../samples-data/person.xlsx"

exec mvn \
    -f ../pom.xml \
    clean \
    compile \
    exec:java \
    -Dexec.mainClass="spreadsheet.mapper.samples.SimpleProcessApp" \
    -Dexec.args="../samples-data/person.xlsx"