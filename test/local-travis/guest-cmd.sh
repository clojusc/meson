#!/usr/bin/env bash

cd $MESON && lein lint && lein travis
