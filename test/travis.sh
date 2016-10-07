#!/usr/bin/env bash

lein compile
lein uberjar
make check
