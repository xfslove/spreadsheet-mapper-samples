#!/usr/bin/env bash
echo "see the generated import template at ../samples-data/person-template.xlsx"

exec mvn \
    -f ../pom.xml \
    exec:java \
    -Dexec.mainClass="spreadsheet.mapper.samples.SimpleTemplateGenerateApp" \
    -Dexec.args="../samples-data/person-template.xlsx"