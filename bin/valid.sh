#!/usr/bin/env bash
echo "you will see the valid results at ../samples-data/person.xlsx"

exec mvn \
    -f ../pom.xml \
    clean \
    compile \
    exec:java \
    -Dexec.mainClass="spreadsheet.mapper.samples.SimpleValidationApp" \
    -Dexec.args="../samples-data/person.xlsx"