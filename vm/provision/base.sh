#!/bin/bash

export DEBIAN_FRONTEND=noninteractive #http://serverfault.com/questions/500764/dpkg-reconfigure-unable-to-re-open-stdin-no-file-or-directory

echo 'Update packages'

sudo apt-get update &> /dev/null
sudo apt-get autoremove -y  &> /dev/null

echo 'Install base packages'
#sudo apt-get install -y --force-yes  ia32-libs texlive ghostscript imagemagick vsftpd &> /dev/null

echo 'Log permissions'
sudo chmod -R 777 /var/log

echo 'Install locales'
sudo locale-gen ca_ES  &> /dev/null
sudo locale-gen ca_ES.UTF-8  &> /dev/null
sudo locale-gen es_ES  &> /dev/null
sudo locale-gen es_ES.UTF-8  &> /dev/null
sudo dpkg-reconfigure locales &> /dev/null

echo 'Set Timezone'
sudo echo "Europe/Andorra" | sudo tee /etc/timezone  &> /dev/null
sudo dpkg-reconfigure -f noninteractive tzdata &> /dev/null

# output results to terminal
#df -h
#cat /proc/swaps
#cat /proc/meminfo | grep Swap
