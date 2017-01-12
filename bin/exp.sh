#!/usr/bin/env bash
echo "you will see the export person data at ../samples-data/person-export.xlsx"

exec mvn \
    -f ../pom.xml \
    clean \
    compile \
    exec:java \
    -Dexec.mainClass="spreadsheet.mapper.samples.SimpleExportApp" \
    -Dexec.args="../samples-data/person-export.xlsx"