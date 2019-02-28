#!/bin/bash
apt install curl
curl -sL https://deb.nodesource.com/setup_8.x | sudo bash -
apt update
apt install nodejs
npm install -g @angular/cli
npm install
ng serve
