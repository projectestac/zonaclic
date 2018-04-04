#!/bin/bash

echo 'Install Squid'

sudo apt-get install -y squid &> /dev/null

# Using default port (3128) and host ("default" or "localhost")

# sudo service squid restart

