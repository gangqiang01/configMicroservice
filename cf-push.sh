#!/bin/bash
cf push -f manifest.yml  --no-start
cf restart SecrecyConfig-Server
